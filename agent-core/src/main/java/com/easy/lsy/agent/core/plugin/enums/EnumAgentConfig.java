package com.easy.lsy.agent.core.plugin.enums;

public enum EnumAgentConfig {
    ENHANCECLASSNAME("enhance_class_name");
    private String value;

    private EnumAgentConfig(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
