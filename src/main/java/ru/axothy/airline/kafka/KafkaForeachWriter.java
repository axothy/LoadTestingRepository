package ru.axothy.airline.kafka;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.spark.sql.ForeachWriter;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaForeachWriter extends ForeachWriter<Row> {
    private static final Logger logger = LoggerFactory.getLogger(KafkaForeachWriter.class);
    private transient Producer<String, String> producer;
    private final String kafkaTopic;
    private final String kafkaBootstrapServers;

    public KafkaForeachWriter(String kafkaTopic, String kafkaBootstrapServers) {
        this.kafkaTopic = kafkaTopic;
        this.kafkaBootstrapServers = kafkaBootstrapServers;
    }

    @Override
    public boolean open(long partitionId, long epochId) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(props);
        return true;
    }

    @Override
    public void process(Row value) {
        String message = value.getAs("value").toString();
        ProducerRecord<String, String> record = new ProducerRecord<>(kafkaTopic, message);
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                System.out.println();
                logger.error("Ошибка при отправке сообщения в Kafka: {}", exception.getMessage(), exception);
            } else {
                logger.info("Сообщение отправлено в Kafka topic {} с offset {}", kafkaTopic, metadata.offset());
            }
        });
    }

    @Override
    public void close(Throwable errorOrNull) {
        if (producer != null) {
            producer.close();
            logger.info("KafkaProducer закрыт");
        }
        if (errorOrNull != null) {
            logger.error("Ошибка в ForeachWriter: {}", errorOrNull.getMessage(), errorOrNull);
        }
    }
}
