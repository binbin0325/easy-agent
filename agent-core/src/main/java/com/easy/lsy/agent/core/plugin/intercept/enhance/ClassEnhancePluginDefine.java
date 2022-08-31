package com.easy.lsy.agent.core.plugin.intercept.enhance;

import com.easy.lsy.agent.core.logging.api.ILog;
import com.easy.lsy.agent.core.logging.api.LogManager;
import com.easy.lsy.agent.core.plugin.AbstractEnhanceClassDefine;
import com.easy.lsy.agent.core.plugin.StaticMethodsInter;
import com.easy.lsy.agent.core.plugin.intercept.ConstructorInterceptPoint;
import com.easy.lsy.agent.core.plugin.intercept.EnhanceException;
import com.easy.lsy.agent.core.plugin.intercept.InstanceMethodsInterceptPoint;
import com.easy.lsy.agent.core.utils.StringUtils;
import net.bytebuddy.asm.Advice;
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

        // 静态方法插桩
        newClassBuilder = this.enhanceJavaClass(typeDescription, newClassBuilder, classLoader);

        // 构造器和实例方法插桩
        newClassBuilder = this.enhanceInstance(typeDescription, newClassBuilder, classLoader);

        return newClassBuilder;
    }

    /**
     * 调用enhanceClass()方法做静态方法插桩
     * ClassEnhancePluginDefine 中实现了AbstractClassEnhancePluginDefine的抽象方法enhanceClass()：
     *
     * Enhance a class to intercept class static methods.
     *
     * @param typeDescription target class description
     * @param newClassBuilder byte-buddy's builder to manipulate class bytecode.
     * @return new byte-buddy's builder for further manipulation.
     */
    private DynamicType.Builder<?> enhanceJavaClass(TypeDescription typeDescription, DynamicType.Builder<?> newClassBuilder,
                                                           ClassLoader classLoader) {
            newClassBuilder = newClassBuilder.method(isStatic().and(ElementMatchers.any()))
                    .intercept(Advice.to(StaticMethodsInter.class));
        return newClassBuilder;
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
         * 在类实例中不需要任何增强，可能需要增强静态方法。
         */
        if (!existedConstructorInterceptPoint && !existedMethodsInterceptPoints) {
            return newClassBuilder;
        }

        /**
         * 1. enhance constructors
         * 增强构造器
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
         * 增强实例方法
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
