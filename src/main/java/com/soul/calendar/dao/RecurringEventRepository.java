package com.soul.calendar.dao;

import com.soul.calendar.pojo.RecurringEvent;

public interface RecurringEventRepository {

    void save(RecurringEvent recurringEvent);
}