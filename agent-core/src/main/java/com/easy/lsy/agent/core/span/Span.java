package com.easy.lsy.agent.core.span;

public class Span {
    private long id;
    private long parentId;
    private long beginTime;
    private long endTime;
    private String spanName;
    private SpanEnum spanType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getSpanName() {
        return spanName;
    }

    public void setSpanName(String spanName) {
        this.spanName = spanName;
    }

    public SpanEnum getSpanType() {
        return spanType;
    }

    public void setSpanType(SpanEnum spanType) {
        this.spanType = spanType;
    }

}
