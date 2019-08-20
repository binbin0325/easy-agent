package com.easy.lsy.agent.core.plugin;

import com.easy.lsy.agent.core.logging.api.ILog;
import com.easy.lsy.agent.core.logging.api.LogManager;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

public class AgentTransformer implements AgentBuilder.Transformer {
    private static final ILog logger = LogManager.getLogger(AgentTransformer.class);
    private EnhanceClassFinder enhanceClassFinder;

    public AgentTransformer(EnhanceClassFinder enhanceClassFinder) {
        this.enhanceClassFinder = enhanceClassFinder;
    }

    @Override public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription,
        ClassLoader classLoader, JavaModule module) {
        AbstractEnhanceClassDefine pluginDefines = enhanceClassFinder.find(typeDescription, classLoader);
        if (pluginDefines != null) {
            DynamicType.Builder<?> newBuilder = builder;
            DynamicType.Builder<?> possibleNewBuilder = pluginDefines.define(typeDescription, newBuilder, classLoader);
            if (possibleNewBuilder != null) {
                newBuilder = possibleNewBuilder;
            }
            return newBuilder;
        }
        logger.debug("Matched class {}, but ignore by finding mechanism.", typeDescription.getTypeName());
        return builder;
    }
}
