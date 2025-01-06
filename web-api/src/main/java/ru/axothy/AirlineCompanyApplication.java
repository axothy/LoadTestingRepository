package ru.axothy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.axothy.appender.ScheduledLocalFileSystemAppender;

@SpringBootApplication
public class AirlineCompanyApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirlineCompanyApplication.class, args);
        ScheduledLocalFileSystemAppender.scheduleFlush();
    }

}
