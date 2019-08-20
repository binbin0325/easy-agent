package com.easy.lsy.agent.core.system;

import com.easy.lsy.agent.core.logging.api.ILog;
import com.easy.lsy.agent.core.logging.api.LogManager;
import com.easy.lsy.agent.core.logging.core.LogType;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Monitoring thread
 *
 * @author binbin.zhang 2019/1/21
 */
public class ThreadsInfo {

    private static ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private static final ILog logger = LogManager.getLogger(ThreadsInfo.class);

    public void getThreadsInfo() throws Exception {
        Map<Long, Long> shortThreadMap = getTopNThreads(100, 10);
        Map<String, Thread> threadMap = getThreads();
        for (Long id : shortThreadMap.keySet()) {
            for (String name : threadMap.keySet()) {
                Thread thread = threadMap.get(name);
                if (id == thread.getId()) {
                    logger.info("Thread Id:" + id + "\t Name:" + thread.getName()
                        + "\t Group:" + thread.getThreadGroup() + "\t Priority:" + thread.getPriority()
                        + "\t State:" + thread.getState() + "\t Cpu:" + shortThreadMap.get(id)
                        + "\t Interrupted:" + thread.isInterrupted() + "\t Daemon:" + thread.isDaemon(), LogType.Thread);
                }
            }
        }

    }

    private static Map<String, Thread> getThreads() {
        ThreadGroup root = getRoot();
        Thread[] threads = new Thread[root.activeCount()];
        while (root.enumerate(threads, true) == threads.length) {
            threads = new Thread[threads.length * 2];
        }
        SortedMap<String, Thread> map = new TreeMap<String, Thread>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        for (Thread thread : threads) {
            if (thread != null) {
                map.put(thread.getName() + "-" + thread.getId(), thread);
            }
        }
        return map;
    }

    private static ThreadGroup getRoot() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup parent;
        while ((parent = group.getParent()) != null) {
            group = parent;
        }
        return group;
    }

    /**
     * 获取所有线程List
     *
     * @return
     */
    private static List<Thread> getThreadList() {
        List<Thread> result = new ArrayList<Thread>();
        ThreadGroup root = getRoot();
        Thread[] threads = new Thread[root.activeCount()];
        while (root.enumerate(threads, true) == threads.length) {
            threads = new Thread[threads.length * 2];
        }
        for (Thread thread : threads) {
            if (thread != null) {
                result.add(thread);
            }
        }
        return result;
    }

    /**
     * get the top N busy thread
     *
     * @param sampleInterval the interval between two samples
     * @param topN the number of thread
     * @return a Map representing <ThreadID, cpuUsage>
     */
    private static Map<Long, Long> getTopNThreads(int sampleInterval, int topN) {
        List<Thread> threads = getThreadList();

        // Sample CPU
        Map<Long, Long> times1 = new HashMap<Long, Long>();
        for (Thread thread : threads) {
            long cpu = threadMXBean.getThreadCpuTime(thread.getId());
            times1.put(thread.getId(), cpu);
        }

        try {
            // Sleep for some time
            Thread.sleep(sampleInterval);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Resample
        Map<Long, Long> times2 = new HashMap<Long, Long>(threads.size());
        for (Thread thread : threads) {
            long cpu = threadMXBean.getThreadCpuTime(thread.getId());
            times2.put(thread.getId(), cpu);
        }

        // Compute delta map and total time
        long total = 0;
        Map<Long, Long> deltas = new HashMap<Long, Long>(threads.size());
        for (Long id : times2.keySet()) {
            long time1 = times2.get(id);
            long time2 = times1.get(id);
            if (time1 == -1) {
                time1 = time2;
            } else if (time2 == -1) {
                time2 = time1;
            }
            long delta = time2 - time1;
            deltas.put(id, delta);
            total += delta;
        }

        // Compute cpu
        final HashMap<Thread, Long> cpus = new HashMap<Thread, Long>(threads.size());
        for (Thread thread : threads) {
            long cpu = total == 0 ? 0 : Math.round((deltas.get(thread.getId()) * 100) / total);
            cpus.put(thread, cpu);
        }

        // Sort by CPU time : should be a rendering hint...
        Collections.sort(threads, new Comparator<Thread>() {
            public int compare(Thread o1, Thread o2) {
                long l1 = cpus.get(o1);
                long l2 = cpus.get(o2);
                if (l1 < l2) {
                    return 1;
                } else if (l1 > l2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        // use LinkedHashMap to preserve insert order
        Map<Long, Long> topNThreads = new LinkedHashMap<Long, Long>();

        List<Thread> topThreads = topN > 0 && topN <= threads.size()
            ? threads.subList(0, topN) : threads;

        for (Thread thread : topThreads) {
            // Compute cpu usage
            topNThreads.put(thread.getId(), cpus.get(thread));
        }

        return topNThreads;
    }
}
