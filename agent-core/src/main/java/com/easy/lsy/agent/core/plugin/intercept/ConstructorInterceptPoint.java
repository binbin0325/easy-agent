
package com.easy.lsy.agent.core.plugin.intercept;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * One of the three "Intercept Point".
 * "Intercept Point" is a definition about where and how intercept happens.
 * In this "Intercept Point", the definition targets class's constructors, and the interceptor.
 *

 */
public interface ConstructorInterceptPoint {
    /**
     * Constructor matcher
     *
     * @return matcher instance.
     */
    ElementMatcher<MethodDescription> getConstructorMatcher();

    /**
     * @return represents a class name, the class instance must be a instance
     */
    String getConstructorInterceptor();
}
