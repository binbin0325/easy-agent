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

        System.out.println("监控 - Begin By Byte-buddy beforeMethod");

        System.out.println("对象类型名称： " + objInst.getClass().getName());
        System.out.println("方法名称： " + method.getName());
        System.out.println("入参个数： " + method.getParameterCount());
        for (int i = 0; i < method.getParameterCount(); i++) {
            System.out.println("入参 Idx： " + (i + 1) + " 类型： " + method.getParameterTypes()[i].getTypeName() + " 内容： " + allArguments[i]);
        }
        System.out.println("出参类型： " + method.getReturnType().getName());
        System.out.println("监控 - End beforeMethod\r\n");

    }

    @Override
    public Object afterMethod(Object objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
        Object ret) throws Throwable {

        System.out.println("监控 - Begin By Byte-buddy afterMethod");
        System.out.println("对象类型名称： " + objInst.getClass().getName());
        System.out.println("方法名称： " + method.getName());
        System.out.println("入参个数： " + method.getParameterCount());
        for (int i = 0; i < method.getParameterCount(); i++) {
            System.out.println("入参 Idx： " + (i + 1) + " 类型： " + method.getParameterTypes()[i].getTypeName() + " 内容： " + allArguments[i]);
        }
        System.out.println("出参类型： " + method.getReturnType().getName());
        System.out.println("出参结果： " + ret);
        //System.out.println("方法耗时：" + (System.currentTimeMillis() - start) + "ms");
        System.out.println("监控 - End afterMethod \r\n");
        return ret;
    }

    @Override
    public void handleMethodException(Object objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
        Throwable t) {
        System.out.println("监控 - Begin By Byte-buddy handleMethodException");
        System.out.println("对象类型名称： " + objInst.getClass().getName());
        System.out.println("Throwable类型： " + t);
        System.out.println("监控 - End handleMethodException\r\n");
    }
}
