package com.soul.calendar.pojo;

import java.sql.Timestamp;
import java.util.Arrays;

public class CreateEventBody {

    private Integer eventId;
    private Integer host;
    private Integer[] participants;
    private Timestamp startAt;
    private Timestamp endAt;
    
    public Integer getHost() {
        return host;
    }
    public void setHost(Integer host) {
        this.host = host;
    }
    public Integer[] getParticipants() {
        return participants;
    }
    public void setParticipants(Integer[] participants) {
        this.participants = participants;
    }
    public Timestamp getStartAt() {
        return startAt;
    }
    public void setStartAt(Timestamp startAt) {
        this.startAt = startAt;
    }
    public Timestamp getEndAt() {
        return endAt;
    }
    public void setEndAt(Timestamp endAt) {
        this.endAt = endAt;
    }

    public Integer getEventId() {
        return eventId;
    }
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
    // public CreateEventBody(Integer eventId, Integer host, Integer[] participants, Timestamp startAt, Timestamp endAt) {
    //     this.eventId = eventId;
    //     this.host = host;
    //     this.participants = participants;
    //     this.startAt = startAt;
    //     this.endAt = endAt;
    // }

    public CreateEventBody(Integer host, Integer[] participants, Timestamp startAt, Timestamp endAt) {
        this.host = host;
        this.participants = participants;
        this.startAt = startAt;
        this.endAt = endAt;
    }
    @Override
    public String toString() {
        return "CreateEventBody [eventId=" + eventId + ", host=" + host + ", participants="
                + Arrays.toString(participants) + ", startAt=" + startAt + ", endAt=" + endAt + "]";
    }

}
