package ru.axothy.airline.spark;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.ForeachWriter;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.Trigger;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.TimeoutException;

@Service
public class FileToKafkaStreamer {

    @Value("${app.monitorDir}")
    private String monitorDir;

    @Value("${kafka.bootstrap.servers}")
    private String kafkaBootstrapServers;

    @Value("${kafka.topic}")
    private String kafkaTopic;

    private SparkSession sparkSession;
    private StreamingQuery streamingQuery;
    private Producer<String, String> producer;



    //fixme не забыть добавить vm options
    //--add-exports=java.base/sun.nio.ch=ALL-UNNAMED, --add-opens=java.base/java.lang=ALL-UNNAMED, --add-opens=java.base/java.lang.reflect=ALL-UNNAMED, --add-opens=java.base/java.io=ALL-UNNAMED, --add-opens=java.base/sun.security.action=ALL-UNNAMED, --add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED,
    @PostConstruct
    public void startStreaming() throws TimeoutException {
        sparkSession = SparkSession.builder()
                .appName("FileToKafkaStreamer")
                .config("spark.master", "local")
                .getOrCreate();

        sparkSession.sparkContext().setLogLevel("WARN");

        // Todo impl KafkaProducer
//        Properties props = new Properties();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        producer = new KafkaProducer<>(props);

        // Определяем схему для текстовых файлов
        StructType schema = new StructType().add("value", "string");

        // Создаем DataFrame, который следит за добавлением новых файлов
        Dataset<Row> lines = sparkSession.readStream()
                .format("text")
                .schema(schema)
                .load(monitorDir);

        // Обработка каждой строки
        streamingQuery = lines.writeStream()
                .foreach(new ForeachWriter<Row>() {
                    @Override
                    public boolean open(long partitionId, long epochId) {
                        // Метод вызывается при начале обработки каждой партиции
                        return true;
                    }

                    @Override
                    public void process(Row value) {
                        String message = value.getString(0);
                        // TODO impl send to kafka
                       /// sendToKafka(message);
                        System.out.println("RECEIVED MESSAGE: " + message);
                    }

                    @Override
                    public void close(Throwable errorOrNull) {
                        // Метод вызывается при завершении обработки партиции
                    }
                })
                .trigger(Trigger.ProcessingTime("10 seconds")) // Настройка триггера по необходимости
                .start();
    }

    private void sendToKafka(String message) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        Producer<String, String> producer = new KafkaProducer<>(props);
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(kafkaTopic, message);
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    System.err.println("Ошибка при отправке сообщения в Kafka: " + exception.getMessage());
                } else {
                    System.out.println("Сообщение отправлено в Kafka topic " + kafkaTopic + " с offset " + metadata.offset());
                }
            });
        } finally {
            producer.close();
        }
    }

    @PreDestroy
    public void stopStreaming() {
        try {
            if (streamingQuery != null) {
                streamingQuery.stop();
            }
            if (sparkSession != null) {
                sparkSession.stop();
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
