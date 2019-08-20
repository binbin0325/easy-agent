package com.easy.lsy.agent.core.plugin;

import com.easy.lsy.agent.core.plugin.bytebuddy.AbstractJunction;
import com.easy.lsy.agent.core.plugin.match.ProtectiveShieldMatcher;
import com.easy.lsy.agent.core.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.isInterface;
import static net.bytebuddy.matcher.ElementMatchers.not;

public class EnhanceClassFinder {
    private final Map<String, AbstractEnhanceClassDefine> nameMatchDefine = new HashMap<String, AbstractEnhanceClassDefine>();

    public EnhanceClassFinder(List<AbstractEnhanceClassDefine> plugins) {
        for (AbstractEnhanceClassDefine plugin : plugins) {
            String matchName = plugin.enhanceClass();

            if (StringUtils.isEmpty(matchName)) {
                continue;
            }

            AbstractEnhanceClassDefine pluginDefines = nameMatchDefine.get(matchName);
            if (pluginDefines == null) {
                nameMatchDefine.put(matchName, plugin);
            }
        }
    }

    /**
     * Find abstractEnhanceClassDefine
     *
     * @param typeDescription
     * @param classLoader
     * @return
     */
    public AbstractEnhanceClassDefine find(TypeDescription typeDescription,
        ClassLoader classLoader) {
        AbstractEnhanceClassDefine matchedPlugins = null;
        String typeName = typeDescription.getTypeName();
        if (nameMatchDefine.containsKey(typeName)) {
            matchedPlugins = nameMatchDefine.get(typeName);
        }
        return matchedPlugins;
    }

    public ElementMatcher<? super TypeDescription> buildMatch() {
        ElementMatcher.Junction judge = new AbstractJunction<NamedElement>() {
            @Override
            public boolean matches(NamedElement target) {
                return nameMatchDefine.containsKey(target.getActualName());
            }
        };
        judge = judge.and(not(isInterface()));

        return new ProtectiveShieldMatcher(judge);
    }
}
