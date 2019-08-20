package com.easy.lsy.agent.connection;

import com.easy.lsy.agent.core.logging.api.ILog;
import com.easy.lsy.agent.core.logging.api.LogManager;
import com.easy.lsy.agent.core.plugin.intercept.enhance.InstanceMethodsAroundInterceptor;
import com.easy.lsy.agent.core.plugin.intercept.enhance.MethodInterceptResult;
import java.lang.reflect.Method;

public class ConnectionInterceptor implements InstanceMethodsAroundInterceptor {
    private static final ILog logger = LogManager.getLogger(ConnectionInterceptor.class);
    @Override
    public void beforeMethod(Object objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
        MethodInterceptResult result) throws Throwable {

    }

    @Override
    public Object afterMethod(Object objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
        Object ret) throws Throwable {
        return ret;
    }

    @Override
    public void handleMethodException(Object objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
        Throwable t) {

    }
}
