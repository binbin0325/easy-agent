
package com.easy.lsy.agent.core.logging.api;

public interface LogResolver {
    /**
     * @param clazz, the class is showed in log message.
     * @return {@link ILog} implementation.
     */
    ILog getLogger(Class<?> clazz);
}
