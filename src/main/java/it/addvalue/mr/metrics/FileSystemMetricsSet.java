package it.addvalue.mr.metrics;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.IteratorUtils;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;

public class FileSystemMetricsSet implements MetricSet
{

    public Map<String, Metric> getMetrics()
    {
        final Map<String, Metric> gauges = new HashMap<String, Metric>();

        @SuppressWarnings("unchecked")
        final List<FileStore> fileStores = IteratorUtils.toList(FileSystems.getDefault()
                                                                           .getFileStores()
                                                                           .iterator());

        gauges.put("systats.filesystem.usableSpace.count", new Gauge<Long>()
        {
            public Long getValue()
            {
                long value = 0L;
                for ( FileStore fs : fileStores )
                {
                    try
                    {
                        value += fs.getUsableSpace();
                    }
                    catch ( IOException e )
                    {
                        e.printStackTrace();
                    }
                }
                return value;
            }
        });

        gauges.put("systats.filesystem.totalSpace.count", new Gauge<Long>()
        {
            public Long getValue()
            {
                long value = 0L;
                for ( FileStore fs : fileStores )
                {
                    try
                    {
                        value += fs.getTotalSpace();
                    }
                    catch ( IOException e )
                    {
                        e.printStackTrace();
                    }
                }
                return value;
            }
        });

        return gauges;
    }

}
