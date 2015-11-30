package it.addvalue.mr.metrics;

import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;
import com.sun.management.OperatingSystemMXBean;

@SuppressWarnings("restriction")
public class OperatingSystemMXBeanMetricSet implements MetricSet
{
    private final OperatingSystemMXBean operatingSystemMXBean;

    public OperatingSystemMXBeanMetricSet()
    {
        operatingSystemMXBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    @Override
    public Map<String, Metric> getMetrics()
    {

        final Map<String, Metric> gauges = new HashMap<String, Metric>();

        memory(gauges);
        cpu(gauges);

        return Collections.unmodifiableMap(gauges);
    }

    private void cpu(final Map<String, Metric> gauges)
    {
        gauges.put("systats.cpu.ProcessCpuTime", new Gauge<Long>()
        {
            public Long getValue()
            {
                return operatingSystemMXBean.getProcessCpuTime();
            }
        });

        gauges.put("systats.cpu.AvailableProcessors", new Gauge<Integer>()
        {
            public Integer getValue()
            {
                return operatingSystemMXBean.getAvailableProcessors();
            }
        });

        gauges.put("systats.cpu.ProcessCpuLoad", new Gauge<Double>()
        {
            public Double getValue()
            {
                return operatingSystemMXBean.getProcessCpuLoad() * 100;
            }
        });

        gauges.put("systats.cpu.SystemCpuLoad", new Gauge<Double>()
        {
            public Double getValue()
            {
                return operatingSystemMXBean.getSystemCpuLoad() * 100;
            }
        });

        gauges.put("systats.cpu.SystemLoadAverage", new Gauge<Double>()
        {
            public Double getValue()
            {
                return operatingSystemMXBean.getSystemLoadAverage() * 100;
            }
        });
    }

    private void memory(final Map<String, Metric> gauges)
    {
        gauges.put("systats.memory.CommittedVirtualMemory", new Gauge<Long>()
        {
            public Long getValue()
            {
                return operatingSystemMXBean.getCommittedVirtualMemorySize();
            }
        });

        gauges.put("systats.memory.FreePhysicalMemorySize", new Gauge<Long>()
        {
            public Long getValue()
            {
                return operatingSystemMXBean.getFreePhysicalMemorySize();
            }
        });

        gauges.put("systats.memory.TotalPhysicalMemorySize", new Gauge<Long>()
        {
            public Long getValue()
            {
                return operatingSystemMXBean.getTotalPhysicalMemorySize();
            }
        });
    }
}
