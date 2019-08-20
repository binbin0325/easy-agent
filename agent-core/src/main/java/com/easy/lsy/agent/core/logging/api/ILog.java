

package com.easy.lsy.agent.core.logging.api;

import com.easy.lsy.agent.core.logging.core.LogType;

public interface ILog {
    void info(String format);

    void info(String format, LogType logType);

    void info(String format, Object... arguments);

    void warn(String format, Object... arguments);

    void warn(Throwable e, String format, Object... arguments);

    void error(String format, Throwable e);

    void error(Throwable e, String format, Object... arguments);

    boolean isDebugEnable();

    boolean isInfoEnable();

    boolean isWarnEnable();

    boolean isErrorEnable();

    void debug(String format);

    void debug(String format, Object... arguments);

    void error(String format);
}
