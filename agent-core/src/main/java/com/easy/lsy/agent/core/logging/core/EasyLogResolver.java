

package com.easy.lsy.agent.core.logging.core;

import com.easy.lsy.agent.core.logging.api.ILog;
import com.easy.lsy.agent.core.logging.api.LogResolver;

public class EasyLogResolver implements LogResolver {
    @Override
    public ILog getLogger(Class<?> clazz) {
        return new EasyLogger(clazz);
    }
}
