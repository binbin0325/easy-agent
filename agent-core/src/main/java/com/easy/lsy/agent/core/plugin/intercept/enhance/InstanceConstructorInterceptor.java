

package com.easy.lsy.agent.core.plugin.intercept.enhance;


public interface InstanceConstructorInterceptor {
    /**
     * Called after the origin constructor invocation.
     */
    void onConstruct(Object objInst, Object[] allArguments);
}
