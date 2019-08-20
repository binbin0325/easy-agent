package com.easy.lsy.agent.core.plugin.intercept.enhance;

import com.easy.lsy.agent.core.logging.api.ILog;
import com.easy.lsy.agent.core.logging.api.LogManager;
import com.easy.lsy.agent.core.plugin.AbstractEnhanceClassDefine;
import com.easy.lsy.agent.core.plugin.intercept.ConstructorInterceptPoint;
import com.easy.lsy.agent.core.plugin.intercept.EnhanceException;
import com.easy.lsy.agent.core.plugin.intercept.InstanceMethodsInterceptPoint;
import com.easy.lsy.agent.core.utils.StringUtils;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import static net.bytebuddy.matcher.ElementMatchers.isStatic;
import static net.bytebuddy.matcher.ElementMatchers.not;

public abstract class ClassEnhancePluginDefine extends AbstractEnhanceClassDefine {
    private static final ILog logger = LogManager.getLogger(ClassEnhancePluginDefine.class);

    @Override
    protected DynamicType.Builder<?> enhance(TypeDescription typeDescription, DynamicType.Builder<?> newClassBuilder,
        ClassLoader classLoader) {
        return enhanceInstance(typeDescription, newClassBuilder, classLoader);
    }

    private DynamicType.Builder<?> enhanceInstance(TypeDescription typeDescription,
        DynamicType.Builder<?> newClassBuilder, ClassLoader classLoader) {
        ConstructorInterceptPoint[] constructorInterceptPoints = getConstructorsInterceptPoints();
        InstanceMethodsInterceptPoint[] instanceMethodsInterceptPoints = getInstanceMethodsInterceptPoints();
        String enhanceOriginClassName = typeDescription.getTypeName();
        boolean existedConstructorInterceptPoint = false;
        if (constructorInterceptPoints != null && constructorInterceptPoints.length > 0) {
            existedConstructorInterceptPoint = true;
        }
        boolean existedMethodsInterceptPoints = false;
        if (instanceMethodsInterceptPoints != null && instanceMethodsInterceptPoints.length > 0) {
            existedMethodsInterceptPoints = true;
        }
        /**
         * nothing need to be enhanced in class instance, maybe need enhance static methods.
         */
        if (!existedConstructorInterceptPoint && !existedMethodsInterceptPoints) {
            return newClassBuilder;
        }

        /**
         * 1. enhance constructors
         */
        if (existedConstructorInterceptPoint) {
            for (ConstructorInterceptPoint constructorInterceptPoint : constructorInterceptPoints) {
                newClassBuilder = newClassBuilder.constructor(constructorInterceptPoint.getConstructorMatcher()).intercept(SuperMethodCall.INSTANCE
                    .andThen(MethodDelegation.withDefaultConfiguration()
                        .to(new ConstructorInter(constructorInterceptPoint.getConstructorInterceptor(), classLoader))
                    )
                );
            }
        }
        /**
         * 3. enhance instance methods
         */
        if (existedMethodsInterceptPoints) {
            for (InstanceMethodsInterceptPoint instanceMethodsInterceptPoint : instanceMethodsInterceptPoints) {
                String interceptor = instanceMethodsInterceptPoint.getMethodsInterceptor();
                if (StringUtils.isEmpty(interceptor)) {
                    logger.error("no InstanceMethodsAroundInterceptor define to enhance class " + enhanceOriginClassName);
                    throw new EnhanceException("no InstanceMethodsAroundInterceptor define to enhance class " + enhanceOriginClassName);
                }
                ElementMatcher.Junction<MethodDescription> junction = not(isStatic()).and(instanceMethodsInterceptPoint.getMethodsMatcher());
                junction = junction.and(ElementMatchers.<MethodDescription>isDeclaredBy(typeDescription));

                newClassBuilder =
                    newClassBuilder.method(junction)
                        .intercept(
                            MethodDelegation.withDefaultConfiguration()
                                .to(new InstMethodsInter(interceptor, classLoader))
                        );

            }
        }

        return newClassBuilder;
    }

    /**
     * Constructor methods intercept point. See {@link ConstructorInterceptPoint}
     *
     * @return collections of {@link ConstructorInterceptPoint}
     */
    protected abstract ConstructorInterceptPoint[] getConstructorsInterceptPoints();

    /**
     * Instance methods intercept point. See {@link InstanceMethodsInterceptPoint}
     *
     * @return collections of {@link InstanceMethodsInterceptPoint}
     */
    protected abstract InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints();

}
