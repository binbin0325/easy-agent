package com.easy.lsy.agent.core.plugin.match;

import com.easy.lsy.agent.core.logging.api.ILog;
import com.easy.lsy.agent.core.logging.api.LogManager;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * In some cases, some frameworks and libraries use some binary codes tech too. From the community feedback, some of
 * them have compatible issues with byte-buddy core, which trigger "Can't resolve type description" exception.
 *
 * So I build this protective shield by a nested matcher. When the origin matcher(s) can't resolve the type, the
 * SkyWalking agent ignores this types.
 *
 * Notice: this ignore mechanism may miss some instrumentations, but at most cases, it's same. If missing happens,
 * please pay attention to the WARNING logs.

 */
public class ProtectiveShieldMatcher<T> extends ElementMatcher.Junction.AbstractBase<T> {
    private static final ILog logger = LogManager.getLogger(ProtectiveShieldMatcher.class);

    private final ElementMatcher<? super T> matcher;

    public ProtectiveShieldMatcher(ElementMatcher<? super T> matcher) {
        this.matcher = matcher;
    }

    public boolean matches(T target) {
        try {
            return this.matcher.matches(target);
        } catch (Throwable t) {
            logger.error("Byte-buddy occurs exception when match type.", t);
            return false;
        }
    }
}
