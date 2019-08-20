package com.easy.lsy.agent.core.boot;

import com.easy.lsy.agent.core.config.Config;
import com.easy.lsy.agent.core.logging.api.ILog;
import com.easy.lsy.agent.core.logging.api.LogManager;
import com.easy.lsy.agent.core.logging.core.LogType;
import com.easy.lsy.agent.core.system.ThreadsInfo;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadService implements IService, Runnable {

    private static final ILog logger = LogManager.getLogger(ThreadService.class);
    private static final ThreadsInfo threadsInfo = new ThreadsInfo();

    @Override public void boot() throws Throwable {
        logger.info("Start monitoring thread information.");
        Executors.newSingleThreadScheduledExecutor(new DefaultNamedThreadFactory("ThreadService"))
            .scheduleAtFixedRate(this, 0, Config.Thread.Monitoring_interval, TimeUnit.SECONDS);
    }

    @Override public void run() {
        logger.info("**********Thread information**********.", LogType.Thread);
        try {
            threadsInfo.getThreadsInfo();
        } catch (Exception e) {
            logger.error("An error occurred while obtaining monitoring thread information.", e);
        }

    }
}
