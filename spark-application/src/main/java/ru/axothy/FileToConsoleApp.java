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
 * Потоковое приложение для вывода данных на консоль (для отладки)
 * Здесь тоже нужны VM Options
 */
public class FileToConsoleApp {
    public static void main(String[] args) throws StreamingQueryException, TimeoutException {
        SparkSession spark = SparkSession.builder()
                .appName("FileToConsoleApp")
                .master("local[*]")
                .getOrCreate();

        spark.sparkContext().setLogLevel("WARN");

        String inputPath = "/app/flush";

        Dataset<Row> df = spark.readStream()
                .format("text")
                .option("path", inputPath)
                .option("wholetext", true)
                .load();

        // Разбиваем содержимое файла на отдельные строки и создаем сообщения для отправки в консоль
        Dataset<String> messages = df.select("value").as(Encoders.STRING())
                .flatMap((FlatMapFunction<String, String>) content -> {
                    String[] lines = content.split("\\R");
                    // для отладки
                    System.out.println("Содержимое файла:");
                    for (String line : lines) {
                        System.out.println(line);
                    }
                    return Arrays.asList(lines).iterator();
                }, Encoders.STRING());

        // Преобразуем сообщения в формат Row (в данном случае это необязательно)
        Dataset<Row> formattedMessages = messages.toDF();

        // Выводим сообщения на консоль
        StreamingQuery query = formattedMessages.writeStream()
                .format("console")
                .option("truncate", "false")
                .start();

        query.awaitTermination();
    }
}
