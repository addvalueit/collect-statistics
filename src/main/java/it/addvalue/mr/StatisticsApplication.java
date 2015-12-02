package it.addvalue.mr;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
        try
        {
            System.setProperty("local.hostAddress", InetAddress.getLocalHost().getHostAddress());
        }
        catch ( UnknownHostException u )
        {
            // don't worry
        }

        SpringApplication.run(StatisticsApplication.class, args);
    }
}
