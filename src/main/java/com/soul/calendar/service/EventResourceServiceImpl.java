package com.soul.calendar.service;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soul.calendar.dao.EventsDAO;
import com.soul.calendar.dao.UserEventMappingDAO;
import com.soul.calendar.entity.Events;
import com.soul.calendar.entity.UserEventMapping;
import com.soul.calendar.pojo.CreateEventBody;
import com.soul.calendar.pojo.EventInterval;
import com.soul.calendar.pojo.FindFavourableSlotBody;
import com.soul.calendar.pojo.RecurringEvent;

@Service
public class EventResourceServiceImpl implements EventResourceService{

    private EventsDAO eventsDAO;
    private UserEventMappingDAO userEventMappingDAO;

    @Autowired
    public EventResourceServiceImpl(EventsDAO eventsDAO, UserEventMappingDAO userEventMappingDAO) {
        this.eventsDAO=eventsDAO;
        this.userEventMappingDAO=userEventMappingDAO;
    }

    @Override
    public List<Integer> findByUserId(Integer userId) {
        List<UserEventMapping> userEventMappings = userEventMappingDAO.findAllByUserId(userId);
        List<Integer> eventIdList=new ArrayList<Integer>();

        for( UserEventMapping mapping: userEventMappings ) {
            eventIdList.add(mapping.getEventId());
        }

        return eventIdList;
    }

    @Override
    public CreateEventBody save(CreateEventBody createEventBody) {

        //save event to event table

        Events newEvent = new Events(createEventBody.getParticipants(), createEventBody.getHost(), createEventBody.getStartAt(), createEventBody.getEndAt());
        newEvent = eventsDAO.save(newEvent);
        // System.out.println(newEvent.getCreatedAt().toString());

        // save event and participant in mapping table

        UserEventMapping userEventMapping = new UserEventMapping(createEventBody.getHost(), newEvent.getEventId());
        userEventMappingDAO.save(userEventMapping);

        for(Integer participant: createEventBody.getParticipants()) {
            userEventMapping = new UserEventMapping(participant, newEvent.getEventId());
            userEventMappingDAO.save(userEventMapping);
        }

        createEventBody.setEventId(newEvent.getEventId());
        return createEventBody;
    }

    @Override
    public Events getEventById(Integer eventId) {
        Optional<Events> result = eventsDAO.findById(eventId);

        Events theEvent = null;

        if (result.isPresent()) {
            theEvent = result.get();
        }
        else {
            // we didn't find the employee
            throw new RuntimeException("Did not find evnt id - " + eventId);
        }

        return theEvent;
    }

    @Override
    public Map<Integer, List<Events>> findConflictsByUserId(Integer userId) {
        
        List<Integer> eventIdList=this.findByUserId(userId);
        List<Events> eventList = new ArrayList<>();

        for(Integer id: eventIdList) {
            eventList.add(this.getEventById(id));
        }

        // Sort the events By StartTimeAndEndTime
        Collections.sort(eventList , new Comparator<Events>() {
            @Override
            public int compare(Events event1, Events event2) {
                int startTimeComparison = event1.getStartAt().compareTo(event2.getStartAt());
                if (startTimeComparison == 0) {
                    return event1.getEndAt().compareTo(event2.getEndAt());
                }
                return startTimeComparison;
            }
        } );

        // Finding overlapping intervals

        Events currEvent = eventList.get(0);
        Map<Integer, List<Events>> conflictingEventsMap = new HashMap<>();        
        Integer found = 0;
        Integer index = -1;

        for (int i = 1; i < eventList.size(); i++) {
            Events nextEvent = eventList.get(i);
            if (currEvent.getEndAt().before(nextEvent.getStartAt()) ) {
                found = 0;
                currEvent = nextEvent;
            } else {
                if (found == 0) {
                    found = 1;
                    index++;
                    conflictingEventsMap.put(index, new ArrayList<>());
                    conflictingEventsMap.get(index).add(currEvent);
                }
                conflictingEventsMap.get(index).add(nextEvent);
                Timestamp maxEndTime = currEvent.getEndAt().after(nextEvent.getEndAt()) ? currEvent.getEndAt() : nextEvent.getEndAt();
                currEvent.setEndAt(maxEndTime);
            }
        }
        
        return conflictingEventsMap;

    }

    @Override
    public EventInterval findFavourableSlot(FindFavourableSlotBody findFavourableSlotBody) {

        // Get free slots for user start-> currentTime to end-> oneYearLaterTime

        Long currentTime = System.currentTimeMillis();
        Long millisInYear= Long.parseLong("31536000000");
        Long oneYearLaterTime = currentTime + millisInYear;

        //map of participants and all events
        Map<Integer, List< EventInterval >> freeSlots = new HashMap<>();    

        for(Integer participant: findFavourableSlotBody.getParticipants()) {
            // get eventsId of each participants
            List<Integer> eventIdList=this.findByUserId(participant);

            // get event from event id
            List<EventInterval> eventIntervalList = new ArrayList<>();

            for(Integer id: eventIdList) {
                Events events = getEventById(id);
                // if(events.getStartAt().getTime() >= currentTime)
                eventIntervalList.add(new EventInterval(events.getStartAt().getTime(), events.getEndAt().getTime()));
            }
            
            // Sort the events By StartTimeAndEndTime
            Collections.sort(eventIntervalList , new Comparator<EventInterval>() {
                @Override
                public int compare(EventInterval e1, EventInterval e2) {
                    int cmp = Long.compare(e1.getStartAt(), e2.getStartAt());
                    if (cmp != 0) {
                        return cmp;
                    }
                    return Long.compare(e1.getEndAt(), e2.getEndAt());
                }
            } );

            // Merge all overlapping intervals
            List<EventInterval> mergedEventIntervalList = mergOverlappingEventInterval(eventIntervalList);

            Long low=currentTime;

            freeSlots.put(participant, new ArrayList<>());

            for(EventInterval interval: mergedEventIntervalList) {

                if(interval.getEndAt() >= (oneYearLaterTime)) {
                    break;
                }
                else if( interval.getEndAt() <= currentTime ) {
                    continue;
                }

                freeSlots.get(participant).add( new EventInterval(low, interval.getStartAt()) );
                low=interval.getEndAt();
            }

            if(low <  (oneYearLaterTime)) {
                freeSlots.get(participant).add( new EventInterval(low, oneYearLaterTime) );
            }
        }

        return getSlot(freeSlots,TimeUnit.MINUTES.toMillis(findFavourableSlotBody.getDurationInMinutes()));

    }

    // to create a recurring meeting
    public void saveRecur(RecurringEvent recurringEvent) {
        Events event = this.getEventById(recurringEvent.getEventId());
        Timestamp startDate = event.getStartAt();
        Timestamp endDate = event.getEndAt();
        Integer[] participants = event.getParticipants();

        for (int i = 0; i < recurringEvent.getFrequency(); i++) {
            Timestamp startDateN = addDays(startDate, recurringEvent.getoffsetInDays());
            Timestamp endDateN = addDays(endDate, recurringEvent.getoffsetInDays());
            CreateEventBody newEv = new CreateEventBody(event.getHost(), participants, startDateN, endDateN);
            newEv = this.save(newEv);
            startDate = newEv.getStartAt();
            endDate = newEv.getEndAt();
        }
    }

    Timestamp addDays(Timestamp timestamp, Integer offset) {
        long oldTime = timestamp.getTime();
        long offseInmillis = TimeUnit.DAYS.toMillis(offset);
        long newTime = oldTime + offseInmillis;
        return new Timestamp(newTime);
    }    

    // bool findIntervalFast(const vector<pair<int,int> >& intervals, int key ,int dur) {
    //     int low = 0;
    //     int high = intervals.size() - 1;
      
    //     while (low <= high) {
    //       int mid = (low + high) / 2;
    //       pair<int,int> midInterval = intervals[mid];
      
    //       if (key >= midInterval.first && key+dur <= midInterval.second) {
    //         return true;
    //       } else if (key < midInterval.first) {
    //         high = mid - 1;
    //       } else {
    //         low = mid + 1;
    //       }
    //     }
      
    //     return false;
    //   }

    public boolean findInterval(List<EventInterval> intervals, Long key, Long duration) {
        for (EventInterval interval : intervals) {
            if (key >= interval.getStartAt() && key + duration <= interval.getEndAt()) {
                return true;
            }
        }

        return false;
    }

    public EventInterval getSlot(Map<Integer, List<EventInterval>> freeSlots, Long duration) {
        PriorityQueue<Long> pq = new PriorityQueue<>();

        for (Map.Entry<Integer, List<EventInterval>> entry : freeSlots.entrySet()) {
            int userId = entry.getKey();
            List<EventInterval> userSlots = entry.getValue();
            for (EventInterval interval : userSlots) {
                if (interval.getEndAt() - interval.getStartAt() >= duration) {
                    pq.add(interval.getStartAt());
                }
            }
        }

        while (!pq.isEmpty()) {
            Long start = pq.poll();
            System.out.println(start);
            boolean notFound = false;
            for (Map.Entry<Integer, List<EventInterval>> entry : freeSlots.entrySet()) {
                int userId = entry.getKey();
                List<EventInterval> userSlots = entry.getValue();
                if (!findInterval(userSlots, start, duration)) {
                    notFound = true;
                    break;
                }
            }

            if (notFound) {
                continue;
            }
            return new EventInterval(start, start+duration);
        }
        System.out.println("found nothing");
        return new EventInterval(0L, 0L);
    }
      

    public List<EventInterval> mergOverlappingEventInterval(List<EventInterval> intervals) {
 
        List<EventInterval> mergedIntervals = new ArrayList<>();
        EventInterval currentInterval = null;

        for (EventInterval interval : intervals) {
            // If there is no current interval | current interval does not overlap with the new inteval
            if (currentInterval == null || currentInterval.getEndAt() < interval.getStartAt()) {
                currentInterval = new EventInterval();
                currentInterval.setStartAt(interval.getStartAt());
                currentInterval.setEndAt(interval.getEndAt());
                mergedIntervals.add(currentInterval);
            } else {
                // If current interval overlsps with new interval update end time of current interval
                currentInterval.setEndAt(Math.max(currentInterval.getEndAt(), interval.getEndAt()) );
            }
        }

        return mergedIntervals;

    }
    
}
