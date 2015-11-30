package it.addvalue.mr.metrics;

import static com.codahale.metrics.MetricRegistry.name;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;

/**
 * classe copia-incollata e adattata da com.codahale.metrics.jvm.ThreadStatesGaugeSet
 */
public class ThreadStatesMetricsSet implements MetricSet
{

    // do not compute stack traces.
    private final static int   STACK_TRACE_DEPTH = 0;

    private final ThreadMXBean threads;

    public ThreadStatesMetricsSet()
    {
        this.threads = ManagementFactory.getThreadMXBean();
    }

    @Override
    public Map<String, Metric> getMetrics()
    {
        final Map<String, Metric> gauges = new HashMap<String, Metric>();

        for ( final Thread.State state : Thread.State.values() )
        {
            gauges.put(name("systats.threads.bystate." + state.toString()
                                                              .toLowerCase(),
                            "count"),
                       new Gauge<Long>()
                       {
                           public Long getValue()
                           {
                               ThreadInfo[] currentThreads = threads.getThreadInfo(threads.getAllThreadIds(),
                                                                                   STACK_TRACE_DEPTH);

                               Long value = 0L;
                               for ( ThreadInfo info : currentThreads )
                               {
                                   if ( info != null && info.getThreadState() == state )
                                   {
                                       value++;
                                   }
                               }

                               return value;
                           }
                       });
        }

        gauges.put("systats.threads.standardthreads.count", new Gauge<Integer>()
        {
            public Integer getValue()
            {
                return threads.getThreadCount();
            }
        });

        gauges.put("systats.threads.demonthreads.count", new Gauge<Integer>()
        {
            public Integer getValue()
            {
                return threads.getDaemonThreadCount();
            }
        });

        return Collections.unmodifiableMap(gauges);
    }

}
