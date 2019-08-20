
package com.easy.lsy.agent.core.plugin.intercept;

public class EnhanceException extends RuntimeException {
    private static final long serialVersionUID = -2234782755784217255L;

    public EnhanceException(String message) {
        super(message);
    }

    public EnhanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
