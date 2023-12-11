package com.soul.calendar.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="events")
public class Events {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Integer eventId;

    @Column(name = "participants")
    private Integer[] participants;

    @NotNull
    @Column(name = "host", nullable = false)
    private Integer host;

    @NotNull
    @Column(name = "start_at", nullable = false)
    private Timestamp startAt;

    @NotNull
    @Column(name = "end_at", nullable = false)
    private Timestamp endAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, updatable = false)
    private Timestamp updatedAt;

    public Events(Integer[] participants, @NotNull Integer host, @NotNull Timestamp startAt,
            @NotNull Timestamp endAt) {
        this.participants = participants;
        this.host = host;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public Events() {}

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getHost() {
        return host;
    }

    public void setHost(Integer host) {
        this.host = host;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
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

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer[] getParticipants() {
        return participants;
    }

    public void setParticipants(Integer[] participants) {
        this.participants = participants;
    }

}
