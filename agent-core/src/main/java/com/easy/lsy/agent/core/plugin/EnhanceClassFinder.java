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

    AbstractEnhanceClassDefine defaultPluginDefines = null;

    public EnhanceClassFinder(List<AbstractEnhanceClassDefine> plugins) {
        for (AbstractEnhanceClassDefine plugin : plugins) {
            //默认取最后一个
            defaultPluginDefines = plugin;
            String matchName = plugin.enhanceClass();

            System.out.println("matchName== "+matchName);
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
        boolean containsKeyFlag = nameMatchDefine.containsKey(typeName);
        System.out.println("agent premain find typeName  " + typeName);
        System.out.println("agent premain find containsKeyFlag  " + containsKeyFlag);

        if (containsKeyFlag) {
            matchedPlugins = nameMatchDefine.get(typeName);
        }
        return matchedPlugins;
    }

    public ElementMatcher<? super TypeDescription> buildMatch() {
        System.out.println("agent premain buildMatch 3");
        ElementMatcher.Junction judge = new AbstractJunction<NamedElement>() {
            @Override
            public boolean matches(NamedElement target) {
                System.out.println("agent premain buildMatch target.getActualName()  " + target.getActualName());
                boolean containsKeyFlag = nameMatchDefine.containsKey(target.getActualName());
                int mapSize = nameMatchDefine.size();
                System.out.println("agent premain buildMatch containsKeyFlag  " +  containsKeyFlag);
                System.out.println("agent premain buildMatch mapSize  " +  mapSize);
                //如果不包含此类，则为此类默认添加ConnectionInstrumentation插件
                System.out.println("defaultPluginDefines==  " + defaultPluginDefines);
                boolean putFlag = mapSize<100&&!containsKeyFlag&&defaultPluginDefines!=null;
                System.out.println("putFlag  " + putFlag);
                if(putFlag){
                    nameMatchDefine.put(target.getActualName(),defaultPluginDefines);
                }

                return nameMatchDefine.containsKey(target.getActualName());
            }
        };
        judge = judge.and(not(isInterface()));

        return new ProtectiveShieldMatcher(judge);
    }
}
