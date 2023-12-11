package com.soul.calendar.pojo;

public class EventInterval {
    
    private Long startAt;

    private Long endAt;

    public EventInterval(Long startAt, Long endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public EventInterval() {
    }

    public Long getStartAt() {
        return startAt;
    }

    public void setStartAt(Long startAt) {
        this.startAt = startAt;
    }

    public Long getEndAt() {
        return endAt;
    }

    public void setEndAt(Long endAt) {
        this.endAt = endAt;
    }

    @Override
    public String toString() {
        return "EventInterval [startAt=" + startAt + ", endAt=" + endAt + "]";
    }

}
