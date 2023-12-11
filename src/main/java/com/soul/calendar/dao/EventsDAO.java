package com.soul.calendar.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soul.calendar.entity.Events;

public interface EventsDAO extends JpaRepository<Events, Integer> {
    // no code here
}