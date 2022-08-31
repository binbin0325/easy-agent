package com.easy.lsy.agent.core.plugin;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Author tlibn
 * @Date 2022/8/29 12:21
 **/
public class StaticMethodsInter {

    @Advice.OnMethodEnter
    public static void onMethodEnter(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments) {

        System.out.println("监控 - Begin Static beforeMethod");

        System.out.println("对象类型名称： " + method.getDeclaringClass().getName());
        System.out.println("方法名称： " + method.getName());
        System.out.println("入参个数： " + method.getParameterCount());
        for (int i = 0; i < method.getParameterCount(); i++) {
            System.out.println("入参 Idx： " + (i + 1) + " 类型： " + method.getParameterTypes()[i].getTypeName() + " 内容： " + arguments[i]);
        }
        System.out.println("出参类型： " + method.getReturnType().getName());
        System.out.println("监控 - End Static beforeMethod\r\n");
    }

    @Advice.OnMethodExit
    public static void onMethodExit(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments, @Advice.Return Object ret) {

        System.out.println("监控 - Begin Static afterMethod");
        System.out.println("对象类型名称： " + method.getDeclaringClass().getName());
        System.out.println("方法名称： " + method.getName());
        System.out.println("入参个数： " + method.getParameterCount());
        for (int i = 0; i < method.getParameterCount(); i++) {
            System.out.println("入参 Idx： " + (i + 1) + " 类型： " + method.getParameterTypes()[i].getTypeName() + " 内容： " + arguments[i]);
        }
        System.out.println("出参类型： " + method.getReturnType().getName());
        System.out.println("出参结果： " + ret);
        System.out.println("监控 - End Static afterMethod \r\n");
    }

}