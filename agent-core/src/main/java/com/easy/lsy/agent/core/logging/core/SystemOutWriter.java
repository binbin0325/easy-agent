

package com.easy.lsy.agent.core.logging.core;

import java.io.PrintStream;

public enum SystemOutWriter implements IWriter {
    INSTANCE;

    @Override
    public void write(String message) {
        PrintStream out = System.out;
        out.println(message);
    }
}
