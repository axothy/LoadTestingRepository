package ru.axothy;

/**
 * Параметры платформы приложений для авиакомпании
 */
public class PlatformParameters {

    private static final String KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String KAFKA_TOPIC = "airline";

    private static final String MONGO_HOST = "localhost";
    private static final String MONGO_PORT = "27017";
    private static final String MONGO_DB_NAME = "airline";
    private static final String MONGO_COLLECTION_NAME = "ticket_statistics";

    private static final String POSTGRES_DB_NAME = "airline";

    private static final String FLUSH_PATH = "/Users/axothy/Desktop/flush";

}