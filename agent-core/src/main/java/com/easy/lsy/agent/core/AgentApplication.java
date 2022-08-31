package com.easy.lsy.agent.core;

import com.easy.lsy.agent.core.config.TypeMatcher;
import com.easy.lsy.agent.core.plugin.AgentTransformer;
import com.easy.lsy.agent.core.plugin.EnhanceClassBootstrap;
import com.easy.lsy.agent.core.plugin.EnhanceClassFinder;
import com.easy.lsy.agent.core.boot.ServiceManager;
import java.lang.instrument.Instrumentation;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.matcher.ElementMatchers;

import static net.bytebuddy.matcher.ElementMatchers.nameContains;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

public class AgentApplication {
    public static void premain(String agentArgs, Instrumentation instrumentation) {

        System.out.println("agent premain start");
        final EnhanceClassFinder enhanceClassFinder = new EnhanceClassFinder(new EnhanceClassBootstrap().loadClass());
        final ByteBuddy byteBuddy = new ByteBuddy()
            .with(TypeValidation.of(false));

        System.out.println("agent premain 2");
        new AgentBuilder.Default(byteBuddy)
                //需要忽略的资源
                .ignore(TypeMatcher.INSTANCE.ignoreRule())

            .type(enhanceClassFinder.buildMatch())
            //.type((ElementMatchers.any()))
            .transform(new AgentTransformer(enhanceClassFinder))
            .installOn(instrumentation);

        ServiceManager.INSTANCE.boot();
    }
}

