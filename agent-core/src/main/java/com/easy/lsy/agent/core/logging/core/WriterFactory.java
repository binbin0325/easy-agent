package com.easy.lsy.agent.core.logging.core;

import com.easy.lsy.agent.core.config.Config;

public class WriterFactory {
    public static IWriter getLogWriter(LogType logType) {
        if (Config.Logging.FILE_SWITCH) {
            Config.Logging.DIR = Config.Agent.JAR_PATH + "/logs";
            String logTypeStr = logType.toString();
            if (logTypeStr.equals("Thread")) {
                return ThreadFileWriter.get();
            } else if (logTypeStr.equals("JVM")) {
                return JVMFileWriter.get();
            }
            return FileWriter.get();
        } else {
            return SystemOutWriter.INSTANCE;
        }
    }
}
