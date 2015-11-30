package it.addvalue.mr.metrics;

import static com.codahale.metrics.MetricRegistry.name;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.RatioGauge;

/**
 * Classe copia-incollata ed adattata da com.codahale.metrics.jvm.MemoryUsageGaugeSet
 */
public class MemoryMXBeanMetricsSet implements MetricSet
{
    private static final Pattern         WHITESPACE = Pattern.compile("[\\s]+");

    private final MemoryMXBean           mxBean;

    private final List<MemoryPoolMXBean> memoryPools;

    public MemoryMXBeanMetricsSet()
    {
        this.mxBean = ManagementFactory.getMemoryMXBean();
        this.memoryPools = new ArrayList<MemoryPoolMXBean>(ManagementFactory.getMemoryPoolMXBeans());
    }

    @Override
    public Map<String, Metric> getMetrics()
    {
        final Map<String, Metric> gauges = new HashMap<String, Metric>();

        principalMemoryIndicators(gauges);
        // secondaryMemoryIndicators(gauges);
        memoryPools(gauges);

        return Collections.unmodifiableMap(gauges);
    }

    private void principalMemoryIndicators(final Map<String, Metric> gauges)
    {
        gauges.put("systats.memory.heap.used", new Gauge<Long>()
        {
            public Long getValue()
            {
                return mxBean.getHeapMemoryUsage()
                             .getUsed();
            }
        });

        gauges.put("systats.memory.heap.max", new Gauge<Long>()
        {
            public Long getValue()
            {
                return mxBean.getHeapMemoryUsage()
                             .getMax();
            }
        });

        gauges.put("systats.memory.heap.committed", new Gauge<Long>()
        {
            public Long getValue()
            {
                return mxBean.getHeapMemoryUsage()
                             .getCommitted();
            }
        });

        gauges.put("systats.memory.heap.usage", new RatioGauge()
        {
            protected Ratio getRatio()
            {
                final MemoryUsage usage = mxBean.getHeapMemoryUsage();
                return Ratio.of(100 * usage.getUsed(), usage.getMax());
            }
        });

        gauges.put("systats.memory.non-heap.used", new Gauge<Long>()
        {
            public Long getValue()
            {
                return mxBean.getNonHeapMemoryUsage()
                             .getUsed();
            }
        });

        gauges.put("systats.memory.non-heap.max", new Gauge<Long>()
        {
            public Long getValue()
            {
                return mxBean.getNonHeapMemoryUsage()
                             .getMax();
            }
        });

        gauges.put("systats.memory.non-heap.committed", new Gauge<Long>()
        {
            public Long getValue()
            {
                return mxBean.getNonHeapMemoryUsage()
                             .getCommitted();
            }
        });

        gauges.put("systats.memory.non-heap.usage", new RatioGauge()
        {
            protected Ratio getRatio()
            {
                final MemoryUsage usage = mxBean.getNonHeapMemoryUsage();
                return Ratio.of(100 * usage.getUsed(), usage.getMax());
            }
        });
    }

    private void memoryPools(final Map<String, Metric> gauges)
    {
        for ( final MemoryPoolMXBean pool : memoryPools )
        {
            final String poolName = name("systats.memory.pools", WHITESPACE.matcher(pool.getName())
                                                                           .replaceAll("-"));

            gauges.put(name(poolName, "usage"), new RatioGauge()
            {
                protected Ratio getRatio()
                {
                    MemoryUsage usage = pool.getUsage();
                    return Ratio.of(100 * usage.getUsed(),
                                    usage.getMax() == -1 ? usage.getCommitted() : usage.getMax());
                }
            });

            gauges.put(name(poolName, "max"), new Gauge<Long>()
            {
                public Long getValue()
                {
                    return pool.getUsage()
                               .getMax();
                }
            });

            gauges.put(name(poolName, "used"), new Gauge<Long>()
            {
                public Long getValue()
                {
                    return pool.getUsage()
                               .getUsed();
                }
            });

            gauges.put(name(poolName, "committed"), new Gauge<Long>()
            {
                public Long getValue()
                {
                    return pool.getUsage()
                               .getCommitted();
                }
            });

            // gauges.put(name(poolName, "init"), new Gauge<Long>()
            // {
            // public Long getValue()
            // {
            // return pool.getUsage()
            // .getInit();
            // }
            // });
        }
    }

    private void secondaryMemoryIndicators(final Map<String, Metric> gauges)
    {
        gauges.put("systats.memory.total.init", new Gauge<Long>()
        {
            public Long getValue()
            {
                return mxBean.getHeapMemoryUsage()
                             .getInit() +
                       mxBean.getNonHeapMemoryUsage()
                             .getInit();
            }
        });

        gauges.put("systats.memory.total.used", new Gauge<Long>()
        {
            public Long getValue()
            {
                return mxBean.getHeapMemoryUsage()
                             .getUsed() +
                       mxBean.getNonHeapMemoryUsage()
                             .getUsed();
            }
        });

        gauges.put("systats.memory.total.committed", new Gauge<Long>()
        {
            public Long getValue()
            {
                return mxBean.getHeapMemoryUsage()
                             .getCommitted() +
                       mxBean.getNonHeapMemoryUsage()
                             .getCommitted();
            }
        });

        gauges.put("systats.memory.total.max", new Gauge<Long>()
        {
            public Long getValue()
            {
                return mxBean.getHeapMemoryUsage()
                             .getMax() +
                       mxBean.getNonHeapMemoryUsage()
                             .getMax();
            }
        });

        gauges.put("systats.memory.heap.init", new Gauge<Long>()
        {
            public Long getValue()
            {
                return mxBean.getHeapMemoryUsage()
                             .getInit();
            }
        });

        gauges.put("systats.memory.non-heap.init", new Gauge<Long>()
        {
            public Long getValue()
            {
                return mxBean.getNonHeapMemoryUsage()
                             .getInit();
            }
        });
    }

}
