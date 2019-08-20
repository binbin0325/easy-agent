package com.easy.lsy.agent.core.demo;

import java.lang.instrument.Instrumentation;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.returns;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

public class Test {
    /**
     *
     */
    public void demo() {
        Class<? extends java.util.function.Function> dynamicType = new ByteBuddy()
            .subclass(java.util.function.Function.class)
            .method(ElementMatchers.named("apply"))
            .intercept(MethodDelegation.to(new GreetingInterceptor()))
            .make()
            .load(getClass().getClassLoader())
            .getLoaded();
        try {
            System.out.println(dynamicType.getName());
            System.out.println(dynamicType.newInstance().apply("Byte Buddy"));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建类并加载Class,使用的是WRAPPER类加载策略
     */
    public void demo1() {
        Class<?> type = new ByteBuddy()
            .subclass(GreetingInterceptor.class)
            .make()
            .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
            .getLoaded();
        System.out.println(getClass().getClassLoader().toString());
        System.out.println(type.getClassLoader().toString());
        System.out.println(type.getName());
    }

    /**
     * 重新加载class 将Foo重新定义为bar。
     */
    public void demo2() {
        ByteBuddyAgent.install();
        Foo foo = new Foo();
        new ByteBuddy()
            .redefine(Bar.class)
            .name(Foo.class.getName())
            .make()
            .load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
        System.out.println(foo.m());
        System.out.println(foo.getClass().getName());
    }

    /**
     * 创建Java，为所有实现toString 注解的类都增加toString()方法,此方法需要在premain方法中调用
     */
    public void demo3(String arguments, Instrumentation instrumentation) {
        new AgentBuilder.Default()
            .type(isAnnotatedWith(ToString.class))
            .transform(new AgentBuilder.Transformer() {
                @Override
                public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription,
                    ClassLoader classLoader, JavaModule module) {
                    return builder.method(named("toString"))
                        .intercept(FixedValue.value("transformed"));
                }
            }).installOn(instrumentation);
    }

    /**
     * 创建类，并调用方法
     */
    public void demo4() {
        try {
            Bar bar = new ByteBuddy()
                .subclass(Bar.class)
                .name("example.Type")
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();
            System.out.println(bar.m());
            System.out.println(bar.getClass().getName());

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建新的bar，并替换原来Bar中m方法的返回值
     */
    public void demo5() {
        try {
            Bar bar = new ByteBuddy()
                .subclass(Bar.class)
                .name("example.Type1")
                .method(named("m").and(returns(String.class)).and(takesArguments(0)))
                .intercept(FixedValue.value("Hello byte buddy"))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();
            System.out.println(bar.getClass().getName());
            System.out.println(bar.m());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 匹配多个方法.匹配规则是由下向上。当满足时就先应用
     */
    public void demo6() {
        try {
            Foo foo = new ByteBuddy()
                .subclass(Foo.class)
                .method(named("foo")).intercept(FixedValue.value("one"))
                .method(named("foo").and(takesArguments(1))).intercept(FixedValue.value("two"))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();
            System.out.println(foo.m());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void demo7() {
        try {
            String helloWorld = new ByteBuddy()
                .subclass(Source.class)
                .method(named("hello")).intercept(MethodDelegation.to(Target.class))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance()
                .hello("World");
            System.out.println(helloWorld);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void demo8(){

    }

    public static void main(String[] args) {
        Test test = new Test();
        //test.demo();
        //test.demo1();
        //test.demo2();
        // test.demo4();
        //test.demo5();
        //test.demo6();
        //test.demo7();
    }
}
