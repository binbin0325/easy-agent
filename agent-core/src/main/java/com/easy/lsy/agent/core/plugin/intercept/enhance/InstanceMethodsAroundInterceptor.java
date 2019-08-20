

package com.easy.lsy.agent.core.plugin.intercept.enhance;

import java.lang.reflect.Method;

/**
 * A interceptor, which intercept method's invocation.
 * @author binbin.zhang
 */
public interface InstanceMethodsAroundInterceptor {
    /**
     * called before target method invocation.
     *
     * @param result change this result, if you want to truncate the method.
     * @throws Throwable
     */
    void beforeMethod(Object objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
        MethodInterceptResult result) throws Throwable;

    /**
     * called after target method invocation. Even method's invocation triggers an exception.
     *
     * @param method
     * @param ret the method's original return value.
     * @return the method's actual return value.
     * @throws Throwable
     */
    Object afterMethod(Object objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
        Object ret) throws Throwable;

    /**
     * called when occur exception.
     *
     * @param method
     * @param t the exception occur.
     */
    void handleMethodException(Object objInst, Method method, Object[] allArguments,
        Class<?>[] argumentsTypes,
        Throwable t);
}
