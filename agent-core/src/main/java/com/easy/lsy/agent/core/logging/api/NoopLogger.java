

package com.easy.lsy.agent.core.logging.api;

import com.easy.lsy.agent.core.logging.core.LogType;

public enum NoopLogger implements ILog {
    INSTANCE;

    @Override
    public void info(String message) {

    }

    @Override public void info(String format, LogType logType) {

    }

    @Override
    public void info(String format, Object... arguments) {

    }

    @Override
    public void warn(String format, Object... arguments) {

    }

    @Override
    public void error(String format, Throwable e) {

    }

    @Override
    public boolean isDebugEnable() {
        return false;
    }

    @Override
    public boolean isInfoEnable() {
        return false;
    }

    @Override
    public boolean isWarnEnable() {
        return false;
    }

    @Override
    public boolean isErrorEnable() {
        return false;
    }

    @Override
    public void debug(String format) {

    }

    @Override
    public void debug(String format, Object... arguments) {

    }

    @Override
    public void error(String format) {

    }

    @Override
    public void error(Throwable e, String format, Object... arguments) {

    }


    @Override
    public void warn(Throwable e, String format, Object... arguments) {

    }
}
