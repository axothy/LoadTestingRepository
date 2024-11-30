package ru.axothy.airline.appender;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledLocalFileSystemAppender {
    private static final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);
    private static final long APPEND_DELAY_SECONDS = 60;
    private static final Runnable flush = () -> {
        System.out.println("Flushing...");
        //TODO impl flush to local file system
    };

    public static void scheduleFlush() {
        ScheduledFuture<?> scheduledFuture =
                scheduledExecutor.scheduleAtFixedRate(flush, 5, APPEND_DELAY_SECONDS, TimeUnit.SECONDS);
    }
}
