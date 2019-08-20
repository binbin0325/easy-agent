/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.easy.lsy.agent.core.plugin.intercept.enhance;

import com.easy.lsy.agent.core.logging.api.ILog;
import com.easy.lsy.agent.core.logging.api.LogManager;
import com.easy.lsy.agent.core.plugin.loader.InterceptorInstanceLoader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;


public class InstMethodsInter {
    private static final ILog logger = LogManager.getLogger(InstMethodsInter.class);

    /**
     * An {@link InstanceMethodsAroundInterceptor} This name should only stay in {@link String}, the real {@link Class}
     * type will trigger classloader failure. If you want to know more, please check on books about Classloader or
     * Classloader appointment mechanism.
     */
    private InstanceMethodsAroundInterceptor interceptor;

    /**
     * @param instanceMethodsAroundInterceptorClassName class full name.
     */
    public InstMethodsInter(String instanceMethodsAroundInterceptorClassName, ClassLoader classLoader) {

        try {
            interceptor = InterceptorInstanceLoader.load(instanceMethodsAroundInterceptorClassName, classLoader);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Intercept the target instance method.
     *
     * @param targetObject target class instance.
     * @param allArguments all method arguments
     * @param method method description.
     * @param zuper the origin call ref.
     * @return the return value of target instance method.
     * @throws Exception only throw exception because of zuper.call() or unexpected exception in sky-walking ( This is a
     * bug, if anything triggers this condition ).
     */
    @RuntimeType
    public Object intercept(@This Object targetObject,
        @AllArguments Object[] allArguments,
        @SuperCall Callable<?> zuper,
        @Origin Method method
    ) throws Throwable {

        MethodInterceptResult result = new MethodInterceptResult();
        try {
            interceptor.beforeMethod(targetObject, method, allArguments, method.getParameterTypes(),
                result);
        } catch (Throwable t) {
            logger.error("class" + targetObject.getClass() + "before method intercept failure" + method.getName(), t);
        }

        Object ret = null;
        try {
            if (!result.isContinue()) {
                ret = result._ret();
            } else {
                ret = zuper.call();
            }
        } catch (Throwable t) {
            try {
                interceptor.handleMethodException(targetObject, method, allArguments, method.getParameterTypes(),
                    t);
            } catch (Throwable t2) {
                logger.error("class" + targetObject.getClass() + "handle method " + method.getName() + "exception failure", t2);
            }
            throw t;
        } finally {
            try {
                ret = interceptor.afterMethod(targetObject, method, allArguments, method.getParameterTypes(),
                    ret);
            } catch (Throwable t) {
                logger.error("class" + targetObject.getClass() + "after method " + method.getName() + "intercept failure", t);
            }
        }
        return ret;
    }
}
