// package com.soul.calendar.service;

// import java.sql.Timestamp;

// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.Comparator;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.soul.calendar.dao.EventsDAO;
// import com.soul.calendar.dao.UserEventMappingDAO;
// import com.soul.calendar.entity.Events;
// import com.soul.calendar.entity.UserEventMapping;
// import com.soul.calendar.pojo.CreateEventBody;
// import com.soul.calendar.pojo.EventInterval;
// import com.soul.calendar.pojo.FindFavourableSlotBody;

// @Service
// public class EventResourceServiceImpl implements EventResourceService{

//     private EventsDAO eventsDAO;
//     private UserEventMappingDAO userEventMappingDAO;

//     @Autowired
//     public EventResourceServiceImpl(EventsDAO eventsDAO, UserEventMappingDAO userEventMappingDAO) {
//         this.eventsDAO=eventsDAO;
//         this.userEventMappingDAO=userEventMappingDAO;
//     }

//     @Override
//     public List<Integer> findByUserId(Integer userId) {
//         List<UserEventMapping> userEventMappings = userEventMappingDAO.findAllByUserId(userId);
//         List<Integer> eventIdList=new ArrayList<Integer>();

//         for( UserEventMapping mapping: userEventMappings ) {
//             eventIdList.add(mapping.getEventId());
//         }

//         return eventIdList;
//     }

//     @Override
//     public CreateEventBody save(CreateEventBody createEventBody) {

//         //save event to event table

//         Events newEvent = new Events(createEventBody.getHost(), createEventBody.getStartAt(), createEventBody.getEndAt());
//         newEvent = eventsDAO.save(newEvent);
//         // System.out.println(newEvent.getCreatedAt().toString());

//         // save event and participant in mapping table

//         UserEventMapping userEventMapping = new UserEventMapping(createEventBody.getHost(), newEvent.getEventId());
//         userEventMappingDAO.save(userEventMapping);

//         for(Integer participant: createEventBody.getParticipants()) {
//             userEventMapping = new UserEventMapping(participant, newEvent.getEventId());
//             userEventMappingDAO.save(userEventMapping);
//         }

//         createEventBody.setEventId(newEvent.getEventId());
//         return createEventBody;
//     }

//     @Override
//     public Events getEventById(Integer eventId) {
//         Optional<Events> result = eventsDAO.findById(eventId);

//         Events theEvent = null;

//         if (result.isPresent()) {
//             theEvent = result.get();
//         }
//         else {
//             // we didn't find the employee
//             throw new RuntimeException("Did not find evnt id - " + eventId);
//         }

//         return theEvent;
//     }

//     @Override
//     public Map<Integer, List<Events>> findConflictsByUserId(Integer userId) {
        
//         List<Integer> eventIdList=this.findByUserId(userId);
//         List<Events> eventList = new ArrayList<>();

//         for(Integer id: eventIdList) {
//             eventList.add(this.getEventById(id));
//         }

//         // Sort the events By StartTimeAndEndTime
//         Collections.sort(eventList , new Comparator<Events>() {
//             @Override
//             public int compare(Events event1, Events event2) {
//                 int startTimeComparison = event1.getStartAt().compareTo(event2.getStartAt());
//                 if (startTimeComparison == 0) {
//                     return event1.getEndAt().compareTo(event2.getEndAt());
//                 }
//                 return startTimeComparison;
//             }
//         } );

//         // Finding overlapping intervals

//         Events currEvent = eventList.get(0);
//         Map<Integer, List<Events>> conflictingEventsMap = new HashMap<>();        
//         Integer found = 0;
//         Integer index = -1;

//         for (int i = 1; i < eventList.size(); i++) {
//             Events nextEvent = eventList.get(i);
//             if (currEvent.getEndAt().before(nextEvent.getStartAt()) ) {
//                 found = 0;
//                 currEvent = nextEvent;
//             } else {
//                 if (found == 0) {
//                     found = 1;
//                     index++;
//                     conflictingEventsMap.put(index, new ArrayList<>());
//                     conflictingEventsMap.get(index).add(currEvent);
//                 }
//                 conflictingEventsMap.get(index).add(nextEvent);
//                 Timestamp maxEndTime = currEvent.getEndAt().after(nextEvent.getEndAt()) ? currEvent.getEndAt() : nextEvent.getEndAt();
//                 currEvent.setEndAt(maxEndTime);
//             }
//         }
        
//         return conflictingEventsMap;

//     }

//     @Override
//     public Map<Integer, List<EventInterval>> findFavourableSlot(FindFavourableSlotBody findFavourableSlotBody) {

//         // Get free slots for user start-> currentTime to end-> oneYearLaterTime

//         Timestamp currentTime = new Timestamp(System.currentTimeMillis()); ;
//         Timestamp oneYearLaterTime = addDurationToTimestamp(currentTime, 525600);

//         //map of participants and all events
//         Map<Integer, List< EventInterval >> freeSlots = new HashMap<>();    

//         for(Integer participant: findFavourableSlotBody.getParticipants()) {
//             // get eventsId of each participants
//             List<Integer> eventIdList=this.findByUserId(participant);

//             // get event from event id
//             List<Events> eventList = new ArrayList<>();
//             for(Integer id: eventIdList) {
//                 Events events = getEventById(id);
//                 if(events.getStartAt().after(currentTime))
//                     eventList.add(getEventById(id));
//             }
            
//             // Sort the events By StartTimeAndEndTime
//             Collections.sort(eventList , new Comparator<Events>() {
//                 @Override
//                 public int compare(Events event1, Events event2) {
//                     int startTimeComparison = event1.getStartAt().compareTo(event2.getStartAt());
//                     if (startTimeComparison == 0) {
//                         return event1.getEndAt().compareTo(event2.getEndAt());
//                     }
//                     return startTimeComparison;
//                 }
//             } );

//             // Merge all overlapping intervals
//             List<Events> mergedEventList = mergOverlappingEvents(eventList);

//             Timestamp low=currentTime;
            

//             freeSlots.put(participant, new ArrayList<>());

//             for(Events event: mergedEventList) {
//                 if(event.getEndAt().after(oneYearLaterTime)) {
//                     break;
//                 }
//                 freeSlots.get(participant).add( new EventInterval(low, event.getStartAt()) );
//                 low=event.getEndAt();
//             }
//             freeSlots.get(participant).add( new EventInterval(low, oneYearLaterTime) );
//         }

//         return freeSlots;

//         // getSlot(freeSlots, findFavourableSlotBody.getDurationInMinutes() );

//     }

//     public Timestamp addDurationToTimestamp(Timestamp key, int durationInMinute) {
//         long milliseconds = durationInMinute * 60 * 1000;
//         long updatedTimeInMillis = key.getTime() + milliseconds;
//         return new Timestamp(updatedTimeInMillis);
//     }

//     public boolean isDiffGreaterThanDur(Timestamp start, Timestamp end, int durationInMinute) {
//         long durationInMilliseconds = durationInMinute * 60 * 1000;
//         long diffInMilliseconds = end.getTime() - start.getTime();

//         if (diffInMilliseconds >= durationInMilliseconds) {
//             return true;
//         } 
//         return false;
//     }

//     public boolean findInterval(List<EventInterval> intervals, Timestamp key, int dur) {
//         for (EventInterval curInterval : intervals) {
//             Timestamp keyDur= addDurationToTimestamp(key,dur);

//             if (key.after(curInterval.getStartAt()) && keyDur.before(curInterval.getEndAt())) {
//                 return true;
//             }
//         }
//         return false;
//     }

//     public void getSlot(Map<Integer,List<EventInterval>> freeSlots, Integer duration) {

//         List<EventInterval> allEventIntervals = new ArrayList<>();
        
//         for (Map.Entry<Integer,List<EventInterval>> entry : freeSlots.entrySet()) {

//           for (EventInterval slot : entry.getValue()) {
//             Timestamp start = slot.getStartAt();
//             Timestamp end = slot.getEndAt();
//             if (isDiffGreaterThanDur(start,end,duration)) {
//               allEventIntervals.add(new EventInterval(start, end));
//             }
//           }

//         }

//         // Sort the events By StartTimeAndEndTime
//         Collections.sort(allEventIntervals , new Comparator<EventInterval>() {
//             @Override
//             public int compare(EventInterval event1, EventInterval event2) {
//                 int startTimeComparison = event1.getStartAt().compareTo(event2.getStartAt());
//                 if (startTimeComparison == 0) {
//                     return event1.getEndAt().compareTo(event2.getEndAt());
//                 }
//                 return startTimeComparison;
//             }
//         } );

      
//         for(int i=0;i<allEventIntervals.size();++i) {

//           Timestamp start = allEventIntervals.get(i).getStartAt();
      
//           boolean notFound = false;
      
//           for (int user = 0; user < freeSlots.size(); user++) {
//             if (!findInterval(freeSlots.get(user), start, duration)) {
//               notFound = true;
//               break;
//             }
//           }
      
//           if (notFound) {
//             continue;
//           }
      
//           System.out.println("res-> " + start + " , " + addDurationToTimestamp(start,duration));
//           return;
//         }
      
//         System.out.println("res not found");
//     }
      

//     public List<Events> mergOverlappingEvents(List<Events> eventList) {
        
//         int n=eventList.size();
//         if(n<=1) return eventList;

//         List<Events> res= new ArrayList<>(); ;
//         Events currEvent = eventList.get(0);

//         for (int i = 1; i < n; i++) {
//             Events nextEvent = eventList.get(i);
//             if (currEvent.getEndAt().before(nextEvent.getStartAt()) ) {
//                 res.add(currEvent);
//                 currEvent = nextEvent;
//             } else {
//                 Timestamp maxEndTime = currEvent.getEndAt().after(nextEvent.getEndAt()) ? currEvent.getEndAt() : nextEvent.getEndAt();
//                 currEvent.setEndAt(maxEndTime);
//             }
//         }
//         res.add(currEvent);
//         return res;
//     }
    
// }
