package com.easy.lsy.agent.core.plugin;

import com.easy.lsy.agent.core.logging.api.ILog;
import com.easy.lsy.agent.core.logging.api.LogManager;
import com.easy.lsy.agent.core.plugin.enums.EnumAgentConfig;
import com.easy.lsy.agent.core.utils.FileUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Find the enhanceClassName in the configuration file and get the className to be enhanced.
 *
 * @author binbin.zhang
 */
public class EnhanceClassNameResolver {
    private static final ILog logger = LogManager.getLogger(EnhanceClassNameResolver.class);

    public List<String> getEnhanceClassNames() {
        List<String> list = new ArrayList<>();
        try {
            list = Arrays.asList(FileUtils.readConfig(EnumAgentConfig.ENHANCECLASSNAME.getValue()).split(","));
        } catch (Exception e) {
            logger.error("Failed to parse the list of classes to be enhanced.", e);
        }
        return list;
    }

}
