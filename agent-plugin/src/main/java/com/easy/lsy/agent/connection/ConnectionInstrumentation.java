package com.easy.lsy.agent.connection;

import com.easy.lsy.agent.core.plugin.intercept.ConstructorInterceptPoint;
import com.easy.lsy.agent.core.plugin.intercept.InstanceMethodsInterceptPoint;
import com.easy.lsy.agent.core.plugin.intercept.enhance.ClassEnhancePluginDefine;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class ConnectionInstrumentation extends ClassEnhancePluginDefine {

    @Override protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[] {
            new InstanceMethodsInterceptPoint() {
                @Override public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return named("processMessage"); //需要增强类的方法名
                }

                @Override public String getMethodsInterceptor() {
                    return "com.easy.lsy.agent.connection.ConnectionInterceptor";
                }
            }
        };
    }

    /**
     * 需要增强的类全路径
     * @return
     */
    @Override protected String enhanceClass() {
        return "xxx.xxxx.xxx.xxx";
    }
}
