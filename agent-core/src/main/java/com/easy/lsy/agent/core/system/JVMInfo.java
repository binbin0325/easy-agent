package com.easy.lsy.agent.core.system;

import com.easy.lsy.agent.core.logging.api.ILog;
import com.easy.lsy.agent.core.logging.api.LogManager;
import com.easy.lsy.agent.core.logging.core.LogType;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.util.List;

public class JVMInfo {
    private static final ILog logger = LogManager.getLogger(JVMInfo.class);

    /**
     * Memory Info
     */
    public void getMemoryInfo() {
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        long heapUsed = heapMemoryUsage.getUsed();
        long heapMax = heapMemoryUsage.getMax();
        long heapTotal = heapMemoryUsage.getCommitted();
        double heapUsage = heapUsed / (double)(heapMax == -1 || heapMax == Long.MIN_VALUE ? heapTotal : heapMax) * 100;
        logger.info("Heap[used:" + heapUsed + "(" + (heapUsed >> 10) + "K) "
            + "/total:" + heapTotal + "(" + (heapTotal >> 10) + "K) "
            + "/max:" + heapMax + "(" + (heapMax >> 10) + "K) "
            + "/usage:" + heapUsage + "]", LogType.JVM);
        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean poolMXBean : memoryPoolMXBeans) {
            if (MemoryType.HEAP.equals(poolMXBean.getType())) {
                MemoryUsage usage = poolMXBean.getUsage();
                String poolName = beautifyName(poolMXBean.getName());
                logger.info("poolName:" + poolName + "usage:" + usage, LogType.JVM);
            }
        }
        long nonHeapUsed = nonHeapMemoryUsage.getUsed();
        long nonHeapMax = nonHeapMemoryUsage.getMax();
        long nonHeapTotal = nonHeapMemoryUsage.getCommitted();
        double nonHeapUsage = nonHeapUsed / (double)(nonHeapMax == -1 || nonHeapMax == Long.MIN_VALUE ? nonHeapTotal : nonHeapMax) * 100;
        logger.info("NonHeap[used:" + nonHeapUsed + "(" + (nonHeapUsed >> 10) + "K) "
            + "/total:" + nonHeapTotal + "(" + (nonHeapTotal >> 10) + "K) "
            + "/max:" + nonHeapMax + "(" + (nonHeapMax >> 10) + "K) "
            + "/usage:" + nonHeapUsage + "]", LogType.JVM);
        for (MemoryPoolMXBean poolMXBean : memoryPoolMXBeans) {
            if (MemoryType.NON_HEAP.equals(poolMXBean.getType())) {
                MemoryUsage usage = poolMXBean.getUsage();
                String poolName = beautifyName(poolMXBean.getName());
                logger.info("poolName:" + poolName + "usage:" + usage, LogType.JVM);
            }
        }
    }

    private static String beautifyName(String name) {
        return name.replace(' ', '_').toLowerCase();
    }

    /**
     * GcInfo
     */
    public void getGcInfo() {
        List<GarbageCollectorMXBean> garbageCollectorMxBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMxBeans) {
            String name = garbageCollectorMXBean.getName();
            logger.info("gc." + beautifyName(name) + ".count:" +
                "" + garbageCollectorMXBean.getCollectionCount() + "/time(ms):"
                + "" + garbageCollectorMXBean.getCollectionTime(), LogType.JVM);
        }
    }
}
