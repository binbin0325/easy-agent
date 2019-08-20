

package com.easy.lsy.agent.core.config;

import com.easy.lsy.agent.core.logging.core.LogLevel;

public class Config {

    public static class Agent {
        /**
         * Namespace isolates headers in cross process propagation. The HEADER name will be `HeaderName:Namespace`.
         */
        public static String NAMESPACE = "";

        /**
         * Application code is showed in skywalking-ui. Suggestion: set a unique name for each service, service instance
         * nodes share the same code
         */
        public static String SERVICE_NAME = "";

        /**
         * Authentication active is based on backend setting, see application.yml for more details. For most scenarios,
         * this needs backend extensions, only basic match auth provided in default implementation.
         */
        public static String AUTHENTICATION = "";

        /**
         * Negative or zero means off, by default. {@link #SAMPLE_N_PER_3_SECS} means sampling N  in 3 seconds tops.
         */
        public static int SAMPLE_N_PER_3_SECS = -1;

        /**
         * If the operation name of the first span is included in this set, this segment should be ignored.
         */
        public static String IGNORE_SUFFIX = ".jpg,.jpeg,.js,.css,.png,.bmp,.gif,.ico,.mp3,.mp4,.html,.svg";

        /**
         * The max number of spans in a single segment. Through this config item, skywalking keep your application
         * memory cost estimated.
         */
        public static int SPAN_LIMIT_PER_SEGMENT = 300;

        /**
         * If true, skywalking agent will save all instrumented classes files in `/debugging` folder. Skywalking team
         * may ask for these files in order to resolve compatible problem.
         */
        public static boolean IS_OPEN_DEBUGGING_CLASS = false;

        /**
         * Active V2 header in default
         */
        public static boolean ACTIVE_V2_HEADER = true;

        /**
         * Deactive V1 header in default
         */
        public static boolean ACTIVE_V1_HEADER = false;
        /**
         * Agent jar path
         */
        public static String JAR_PATH = "";
    }

    public static class Logging {
        /**
         * Log file name.
         */
        public static String FILE_NAME = "agent.log";
        /**
         * Thread Log file name.
         */
        public static String THREAD_FILE_NAME = "threadInformation.log";
        /**
         * JVM Log file name.
         */
        public static String JVM_FILE_NAME = "jvmInformation.log";

        /**
         * Log files directory. Default is blank string, means, use "system.out" to output logs.
         */
        public static String DIR = "";

        /**
         * The max size of log file. If the size is bigger than this, archive the current file, and write into a new
         * file.
         */
        public static int MAX_FILE_SIZE = 300 * 1024 * 1024;

        /**
         * The log level. Default is debug.
         */
        public static LogLevel LEVEL = LogLevel.DEBUG;

        /**
         * Output to log file switch
         */
        public static boolean FILE_SWITCH = true;
    }

    public static class Thread {

        public static int Monitoring_interval = 10;

    }

    public static class JVM {

        public static int Monitoring_interval = 30;

    }
}
