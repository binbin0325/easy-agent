package com.easy.lsy.agent.core;

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

        final EnhanceClassFinder enhanceClassFinder = new EnhanceClassFinder(new EnhanceClassBootstrap().loadClass());
        final ByteBuddy byteBuddy = new ByteBuddy()
            .with(TypeValidation.of(false));

        new AgentBuilder.Default(byteBuddy)
            .ignore(
                nameStartsWith("net.bytebuddy.")
                    .or(nameStartsWith("org.slf4j."))
                    .or(nameStartsWith("org.apache.logging."))
                    .or(nameStartsWith("org.groovy."))
                    .or(nameContains("javassist"))
                    .or(nameContains(".asm."))
                    .or(nameStartsWith("sun.reflect"))
                    .or(ElementMatchers.<TypeDescription>isSynthetic()))
            .type(enhanceClassFinder.buildMatch())
            .transform(new AgentTransformer(enhanceClassFinder))
            .installOn(instrumentation);

        ServiceManager.INSTANCE.boot();
    }
}

