package it.addvalue.mr.metrics;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.codahale.metrics.annotation.Gauge;

public class MetricsSessionListener implements HttpSessionListener
{
    private AtomicInteger sessions = new AtomicInteger();

    @Gauge
    public int sessioniAperte()
    {
        return sessions.intValue();
    }

    public void sessionCreated(HttpSessionEvent arg0)
    {
        sessions.incrementAndGet();
    }

    public void sessionDestroyed(HttpSessionEvent arg0)
    {
        sessions.decrementAndGet();
    }
}
