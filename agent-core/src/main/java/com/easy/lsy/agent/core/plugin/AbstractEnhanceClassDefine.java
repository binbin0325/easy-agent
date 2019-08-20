package com.easy.lsy.agent.core.plugin;

import com.easy.lsy.agent.core.logging.api.ILog;
import com.easy.lsy.agent.core.logging.api.LogManager;
import com.easy.lsy.agent.core.utils.StringUtils;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;

/**
 * Abstract Enhance Class Define.
 * <p>
 * It provides the outline of enhancing the target class. If you want to know more about enhancing, you should go to
 * see
 */
public abstract class AbstractEnhanceClassDefine {
    private static final ILog logger = LogManager.getLogger(AbstractEnhanceClassDefine.class);

    /**
     * Main entrance of enhancing the class.
     *
     * @param typeDescription target class description.
     * @param builder byte-buddy's builder to manipulate target class's bytecode.
     * @param classLoader load the given transformClass
     * @return the new builder, or <code>null</code> if not be enhanced.
     */
    public DynamicType.Builder<?> define(TypeDescription typeDescription,
        DynamicType.Builder<?> builder, ClassLoader classLoader) {
        String interceptorDefineClassName = this.getClass().getName();
        String transformClassName = typeDescription.getTypeName();
        if (StringUtils.isEmpty(transformClassName)) {
            logger.warn("classname of being intercepted is not defined by {}.", interceptorDefineClassName);
            return null;
        }

        logger.info("prepare to enhance class {} by {}.", transformClassName, interceptorDefineClassName);

        /**
         * find origin class source code for interceptor
         */
        DynamicType.Builder<?> newClassBuilder = this.enhance(typeDescription, builder, classLoader);

        logger.info("enhance class {} by {} completely.", transformClassName, interceptorDefineClassName);
        return newClassBuilder;
    }

    protected abstract DynamicType.Builder<?> enhance(TypeDescription typeDescription,
        DynamicType.Builder<?> newClassBuilder, ClassLoader classLoader);

    /**
     * Define the  for filtering class.
     *
     * @return Class name to be enhanced.
     */
    protected abstract String enhanceClass();

}
