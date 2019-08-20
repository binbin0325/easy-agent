package com.easy.lsy.agent.core.system;

import java.lang.management.BufferPoolMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.util.List;

public class DashboardCommand {

    public static void main(String args[]) {
        addBufferPoolMemoryInfo();
        addRuntimeInfo();
        addMemoryInfo();
        addGcInfo();
    }

    /**
     * 添加缓冲池内存信息
     */
    private static void addBufferPoolMemoryInfo() {
        try {
            @SuppressWarnings("rawtypes")
            Class bufferPoolMXBeanClass = Class.forName("java.lang.management.BufferPoolMXBean");
            @SuppressWarnings("unchecked")
            List<BufferPoolMXBean> bufferPoolMXBeans = ManagementFactory.getPlatformMXBeans(bufferPoolMXBeanClass);
            for (BufferPoolMXBean mbean : bufferPoolMXBeans) {
                long used = mbean.getMemoryUsed();
                long total = mbean.getTotalCapacity();
                System.out.println(mbean.getName());
                System.out.println(used);
                System.out.println(total);
                System.out.println(Long.MIN_VALUE);
            }
        } catch (ClassNotFoundException e) {
            // ignore
        }
    }

    /**
     * Runtime Info
     */
    private static void addRuntimeInfo() {
        System.out.println("os.name:" + System.getProperty("os.name"));
        System.out.println("os.version:" + System.getProperty("os.version"));
        System.out.println("java.version:" + System.getProperty("java.version"));
        System.out.println("java.home:" + System.getProperty("java.home"));
        System.out.println("systemload.average:" +
            String.format("%.2f", ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage()));
        System.out.println("processors:" + "" + Runtime.getRuntime().availableProcessors());
        System.out.println("uptime:" + "" + ManagementFactory.getRuntimeMXBean().getUptime() / 1000 + "s");
    }

    /**
     * Memory Info
     */
    private static void addMemoryInfo() {
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();

        System.out.println(heapMemoryUsage);
        System.out.println(nonHeapMemoryUsage);
        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();

        System.out.println("*****************heap******************");
        for (MemoryPoolMXBean poolMXBean : memoryPoolMXBeans) {
            if (MemoryType.HEAP.equals(poolMXBean.getType())) {
                MemoryUsage usage = poolMXBean.getUsage();
                String poolName = beautifyName(poolMXBean.getName());
                System.out.println("poolName:" + poolName);
                System.out.println("usage:" + usage);
            }
        }
        System.out.println("*****************nonheap******************");
        for (MemoryPoolMXBean poolMXBean : memoryPoolMXBeans) {
            if (MemoryType.NON_HEAP.equals(poolMXBean.getType())) {
                MemoryUsage usage = poolMXBean.getUsage();
                String poolName = beautifyName(poolMXBean.getName());
                System.out.println("poolName:" + poolName);
                System.out.println("usage:" + usage);
            }
        }
    }

    private static String beautifyName(String name) {
        return name.replace(' ', '_').toLowerCase();
    }

    /**
     * GcInfo
     */
    private static void addGcInfo() {
        List<GarbageCollectorMXBean> garbageCollectorMxBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMxBeans) {
            String name = garbageCollectorMXBean.getName();
            System.out.println("gc." + beautifyName(name) + ".count:" +
                "" + garbageCollectorMXBean.getCollectionCount());
            System.out.println("gc." + beautifyName(name) + ".time(ms):" + "" + garbageCollectorMXBean.getCollectionTime());
        }
    }

}
