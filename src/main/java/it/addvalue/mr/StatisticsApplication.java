package it.addvalue.mr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;

@EnableMetrics
@EnableScheduling
@SpringBootApplication
public class StatisticsApplication
{
    public static void main(String args[])
    {
        SpringApplication.run(StatisticsApplication.class, args);
    }
}
