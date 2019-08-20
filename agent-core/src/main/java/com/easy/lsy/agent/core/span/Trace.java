package com.easy.lsy.agent.core.span;

import java.util.List;

public class Trace {
    private long id;
    private List<Span> span;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Span> getSpan() {
        return span;
    }

    public void setSpan(List<Span> span) {
        this.span = span;
    }
}
