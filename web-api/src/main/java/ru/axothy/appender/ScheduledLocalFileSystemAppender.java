package ru.axothy.appender;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledLocalFileSystemAppender {
    private static final String FLUSH_PATH = "/app/flush";
    private static final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);
    private static final long APPEND_DELAY_SECONDS = 60;
    private static final Runnable flush = () -> {
        System.out.println("Flushing...");
        long standard = TicketsAggregator.getNumberOfStandardTickets();
        long business = TicketsAggregator.getNumberOfBusinessTickets();
        int businessPercent = TicketsAggregator.getBusinessTicketsPercent();

        Path directoryPath = Paths.get(FLUSH_PATH);

        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                System.err.println("Failed to create directory: " + directoryPath);
                e.printStackTrace();
                return;
            }
        }

        String timestamp = LocalDateTime.now().toString().replace(':', '-');
        String filePath = directoryPath + "/" + "data-" + timestamp + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Standard Tickets: " + standard);
            writer.newLine();
            writer.write("Business Tickets: " + business);
            writer.newLine();
            writer.write("Business Tickets Percent: " + businessPercent + "%");
            System.out.println("Data flushed to file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath);
            e.printStackTrace();
        }
    };

    public static void scheduleFlush() {
        ScheduledFuture<?> scheduledFuture =
                scheduledExecutor.scheduleAtFixedRate(flush, 5, APPEND_DELAY_SECONDS, TimeUnit.SECONDS);
    }
}
