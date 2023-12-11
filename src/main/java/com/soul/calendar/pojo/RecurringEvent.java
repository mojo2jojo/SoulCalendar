package com.soul.calendar.pojo;

import java.sql.Timestamp;

public class RecurringEvent {
    private Integer eventId;
    private Integer offsetInDays;
    private Integer frequency;

    public RecurringEvent(Integer eventId, Timestamp startDate, Integer offsetInDays, Integer frequency) {
        this.eventId = eventId;
        this.offsetInDays = offsetInDays;
        this.frequency = frequency;
    }

    public RecurringEvent() {
    }

    public RecurringEvent(Integer eventId, Integer offsetInDays, Integer frequency) {
        this.eventId = eventId;
        this.offsetInDays = offsetInDays;
        this.frequency = frequency;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getoffsetInDays() {
        return offsetInDays;
    }

    public void setoffsetInDays(Integer offsetInDays) {
        this.offsetInDays = offsetInDays;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

}
