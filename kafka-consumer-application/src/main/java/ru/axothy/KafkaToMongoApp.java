package ru.axothy;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.bson.Document;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaToMongoApp {

    public static void main(String[] args) {
        // Настройки Kafka Consumer
        String topicName = "airline";
        String groupId = "ticket_consumer_group";
        String bootstrapServers = "localhost:9092";

        Properties props = new Properties();

        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", groupId);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // Подключение к MongoDB
        String mongoHost = "localhost";
        int mongoPort = 27017;
        String mongoDatabaseName = "airline";
        String mongoCollectionName = "ticket_statistics";

        MongoClient mongoClient = MongoClients.create("mongodb://" + mongoHost + ":" + mongoPort);
        MongoDatabase database = mongoClient.getDatabase(mongoDatabaseName);
        MongoCollection<Document> collection = database.getCollection(mongoCollectionName);

        consumer.subscribe(Collections.singletonList(topicName));

        System.out.println("Подписан на топик " + topicName);

        try {
            while (true) {
                // Получаем новые сообщения
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, String> record : records) {
                    String message = record.value();

                    System.out.println("Получено сообщение: " + message);

                    if (message.startsWith("Standard Tickets:")) {
                        int standardTickets = parseValue(message, "Standard Tickets:");
                        Document doc = new Document("type", "Standard Tickets")
                                .append("value", standardTickets)
                                .append("timestamp", System.currentTimeMillis());
                        collection.insertOne(doc);
                        System.out.println("Вставлено Standard Tickets: " + standardTickets);
                    } else if (message.startsWith("Business Tickets:")) {
                        int businessTickets = parseValue(message, "Business Tickets:");
                        Document doc = new Document("type", "Business Tickets")
                                .append("value", businessTickets)
                                .append("timestamp", System.currentTimeMillis());
                        collection.insertOne(doc);
                        System.out.println("Вставлено Business Tickets: " + businessTickets);
                    } else if (message.startsWith("Business Tickets Percent:")) {
                        double businessTicketsPercent = parsePercentValue(message, "Business Tickets Percent:");
                        Document doc = new Document("type", "Business Tickets Percent")
                                .append("value", businessTicketsPercent)
                                .append("timestamp", System.currentTimeMillis());
                        collection.insertOne(doc);
                        System.out.println("Вставлено Business Tickets Percent: " + businessTicketsPercent + "%");
                    } else {
                        System.out.println("Неизвестный тип сообщения: " + message);
                    }
                }
            }
        } finally {
            consumer.close();
            mongoClient.close();
        }
    }

    private static int parseValue(String message, String prefix) {
        try {
            String valueStr = message.substring(prefix.length()).trim();
            return Integer.parseInt(valueStr);
        } catch (NumberFormatException e) {
            System.err.println(e);
            return 0;
        }
    }

    private static double parsePercentValue(String message, String prefix) {
        try {
            String valueStr = message.substring(prefix.length()).trim();
            if (valueStr.endsWith("%")) {
                valueStr = valueStr.substring(0, valueStr.length() - 1).trim();
            }
            return Double.parseDouble(valueStr);
        } catch (NumberFormatException e) {
            System.out.println(e);
            return 0.0;
        }
    }
}
