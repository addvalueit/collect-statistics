package it.addvalue.mr.metrics;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;

@Configuration
public class CodehaleGraphiteMetricsConfiguration extends MetricsConfigurerAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CodehaleGraphiteMetricsConfiguration.class);

    @Value("${statistics.address}")
    private String              statisticAddress;

    @Value("${statistics.port}")
    private int                 statisticPort;

    @Value("${statistics.reportingInterval}")
    private int                 reportingInterval;

    @Bean
    public HttpSessionListener httpSessionListener()
    {
        return new MetricsSessionListener();
    }

    @Override
    public void configureReporters(MetricRegistry metricRegistry)
    {
        // registerReporter allows the MetricsConfigurerAdapter to
        // shut down the reporter when the Spring context is closed

        // metriche di base offerte da Codehale Metrics [che pero' hanno nomi brutti]
        metricRegistry.registerAll(new GarbageCollectorMetricSet());
        // metricRegistry.registerAll(new MemoryUsageGaugeSet());
        // metricRegistry.registerAll(new ThreadStatesGaugeSet());
        // metricRegistry.register("jvm.fd.usage", new FileDescriptorRatioGauge());

        // metriche specifiche da me create
        metricRegistry.registerAll(new OperatingSystemMXBeanMetricSet());
        metricRegistry.registerAll(new MemoryMXBeanMetricsSet());
        metricRegistry.registerAll(new ThreadStatesMetricsSet());
        metricRegistry.registerAll(new FileSystemMetricsSet());

        LOGGER.info("Connecting with graphite on host '" + statisticAddress + "' and port: " + statisticPort);
        
        Graphite graphite = new Graphite(new InetSocketAddress(statisticAddress, statisticPort));
        ViolentedGraphiteReporter reporter = ViolentedGraphiteReporter.forRegistry(metricRegistry)
                                                                      .prefixedWith(getHostName())
                                                                      .convertRatesTo(TimeUnit.SECONDS)
                                                                      .convertDurationsTo(TimeUnit.MILLISECONDS)
                                                                      .filter(MetricFilter.ALL)
                                                                      .build(graphite);

        registerReporter(reporter).start(reportingInterval, TimeUnit.SECONDS);
    }

    private String getHostName()
    {
        StringBuffer output = new StringBuffer();
        try
        {
            Process p = Runtime.getRuntime()
                               .exec("hostname");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null)
            {
                output.append(line + "\n");
            }
        }
        catch ( Exception e )
        {
            throw new RuntimeException("Non sono in grado di recuperare l'host-name", e);
        }
        return output.toString()
                     .replace("\n", "");
    }
}
