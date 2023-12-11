package com.soul.calendar.dao;

import org.springframework.stereotype.Repository;

import com.soul.calendar.pojo.RecurringEvent;

@Repository
public class RecurringEventRepositoryImpl implements RecurringEventRepository {

    @Override
    public void save(RecurringEvent recurringEvent) {
        System.out.println("Saved the data" + recurringEvent);
    }
}
