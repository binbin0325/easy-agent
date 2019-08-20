package com.easy.lsy.agent.core.plugin;

import com.easy.lsy.agent.core.logging.api.ILog;
import com.easy.lsy.agent.core.logging.api.LogManager;
import com.easy.lsy.agent.core.plugin.loader.AgentClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class EnhanceClassBootstrap {
    private static final ILog logger = LogManager.getLogger(EnhanceClassNameResolver.class);

    /**
     * load all enhance class.
     *
     * @return plugin definition list.
     */
    public List<AbstractEnhanceClassDefine> loadClass() {
        AgentClassLoader.initDefaultLoader();
        EnhanceClassNameResolver resolver = new EnhanceClassNameResolver();
        List<String> enhanceClassNames = resolver.getEnhanceClassNames();

        if (enhanceClassNames == null || enhanceClassNames.size() == 0) {
            logger.info("No classes that need to be enhanced were found.");
            return new ArrayList<AbstractEnhanceClassDefine>();
        }

        List<AbstractEnhanceClassDefine> plugins = new ArrayList<AbstractEnhanceClassDefine>();
        for (String className : enhanceClassNames) {
            try {
                logger.info("loading class:" + className);
                AbstractEnhanceClassDefine plugin =
                    (AbstractEnhanceClassDefine)Class.forName(className,
                        true,
                        AgentClassLoader.getDefault())
                        .newInstance();
                plugins.add(plugin);
            } catch (Throwable t) {
                logger.error("load class failure:" + className, t);
            }
        }

        return plugins;

    }

}
