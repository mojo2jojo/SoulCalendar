package com.soul.calendar.service;

import java.util.List;
import java.util.Map;

import com.soul.calendar.entity.Events;
import com.soul.calendar.pojo.CreateEventBody;
import com.soul.calendar.pojo.EventInterval;
import com.soul.calendar.pojo.FindFavourableSlotBody;
import com.soul.calendar.pojo.RecurringEvent;

public interface EventResourceService {

    List<Integer> findByUserId(Integer userId);

    CreateEventBody save(CreateEventBody events);

    Events getEventById(Integer eventId);

    Map<Integer, List<Events>> findConflictsByUserId(Integer userId);

    EventInterval findFavourableSlot(FindFavourableSlotBody findFavourableSlotBody);

    void saveRecur(RecurringEvent recurringEvent);


}
