package ru.axothy;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;

import java.util.Arrays;
import java.util.concurrent.TimeoutException;

/**
 * Потоковое приложение для вывода данных в Kafka
 * Перед запуском вне контейнера обязательно добавить VM Options
 * --add-exports=java.base/sun.nio.ch=ALL-UNNAMED, --add-opens=java.base/java.lang=ALL-UNNAMED, --add-opens=java.base/java.lang.reflect=ALL-UNNAMED, --add-opens=java.base/java.io=ALL-UNNAMED, --add-opens=java.base/sun.security.action=ALL-UNNAMED, --add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED
 */
public class FileToKafkaApp {
    public static void main(String[] args) throws StreamingQueryException, TimeoutException {
        SparkSession spark = SparkSession.builder()
                .appName("FileToKafkaApp")
                .master("local[*]")
                .getOrCreate();

        spark.sparkContext().setLogLevel("WARN");

        String inputPath = "/app/data"; //fixme change

        // Читаем новые файлы из директории в потоковом режиме
        Dataset<Row> df = spark.readStream()
                .format("text")
                .option("path", inputPath)
                .option("wholetext", true)
                .load();

        // Разбиваем содержимое файла на отдельные строки и создаем сообщения для Kafka
        Dataset<String> messages = df.select("value").as(Encoders.STRING())
                .flatMap((FlatMapFunction<String, String>) content -> {
                    String[] lines = content.split("\\R");
                    return Arrays.asList(lines).iterator();
                }, Encoders.STRING());

        // Преобразуем сообщения в формат, подходящий для Kafka
        Dataset<Row> kafkaMessages = messages.selectExpr("CAST(value AS STRING) as value");

        String kafkaBootstrapServers = "localhost:9092";
        String kafkaTopic = "airline";

        StreamingQuery query = kafkaMessages.writeStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", kafkaBootstrapServers)
                .option("topic", kafkaTopic)
                .option("checkpointLocation", "checkpoint")
                .start();

        query.awaitTermination();
    }

}
