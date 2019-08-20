package com.easy.lsy.agent.core.boot;

import com.easy.lsy.agent.core.config.Config;
import com.easy.lsy.agent.core.logging.api.ILog;
import com.easy.lsy.agent.core.logging.api.LogManager;
import com.easy.lsy.agent.core.logging.core.LogType;
import com.easy.lsy.agent.core.system.JVMInfo;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JVMService implements IService, Runnable {
    private static final ILog logger = LogManager.getLogger(JVMService.class);
    private static final JVMInfo jvmInfo = new JVMInfo();

    @Override public void boot() throws Throwable {
        logger.info("Start monitoring jvm information.");
        Executors.newSingleThreadScheduledExecutor(new DefaultNamedThreadFactory("JVMService"))
            .scheduleAtFixedRate(this, 0, Config.JVM.Monitoring_interval, TimeUnit.SECONDS);
    }

    @Override public void run() {
        logger.info("**********JVM information**********.", LogType.JVM);
        try {
            jvmInfo.getMemoryInfo();
            jvmInfo.getGcInfo();
        } catch (Exception e) {
            logger.error("An error occurred while obtaining monitoring jvm information.", e);
        }
    }
}
