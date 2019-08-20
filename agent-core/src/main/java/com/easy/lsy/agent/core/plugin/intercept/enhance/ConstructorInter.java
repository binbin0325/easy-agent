

package com.easy.lsy.agent.core.plugin.intercept.enhance;

import com.easy.lsy.agent.core.logging.api.ILog;
import com.easy.lsy.agent.core.logging.api.LogManager;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

public class ConstructorInter {
    private static final ILog logger = LogManager.getLogger(ConstructorInter.class);

    private InstanceConstructorInterceptor interceptor;


    public ConstructorInter(String constructorInterceptorClassName, ClassLoader classLoader) {
        try {
            interceptor = (InstanceConstructorInterceptor)Class.forName(constructorInterceptorClassName, true, classLoader).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Intercept the target constructor.
     *
     * @param targetObject target class instance.
     * @param allArguments all constructor arguments
     */
    @RuntimeType
    public void intercept(@This Object targetObject,
        @AllArguments Object[] allArguments) {
        try {

            interceptor.onConstruct(targetObject, allArguments);
        } catch (Throwable t) {
            logger.error("ConstructorInter failure.", t);
        }

    }
}
