

package com.easy.lsy.agent.core.logging.api;

import com.easy.lsy.agent.core.config.Config;
import com.easy.lsy.agent.core.logging.core.EasyLogResolver;
import com.easy.lsy.agent.core.logging.core.WriterFactory;
import com.easy.lsy.agent.core.utils.StringUtils;
import java.io.File;
import java.io.UnsupportedEncodingException;

public class LogManager {
    private static LogResolver RESOLVER = new EasyLogResolver();

    public static void setLogResolver(LogResolver resolver) {
        LogManager.RESOLVER = resolver;
    }

    public static ILog getLogger(Class<?> clazz) {
        if (RESOLVER == null) {
            return NoopLogger.INSTANCE;
        }
        if (StringUtils.isEmpty(Config.Agent.JAR_PATH)) {
            String jarWholePath = WriterFactory.class.getProtectionDomain().getCodeSource().getLocation().getFile();
            try {
                jarWholePath = java.net.URLDecoder.decode(jarWholePath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                System.out.println(e.toString());
            }
            Config.Agent.JAR_PATH = new File(jarWholePath).getParentFile().getAbsolutePath();
        }
        return LogManager.RESOLVER.getLogger(clazz);
    }
}
