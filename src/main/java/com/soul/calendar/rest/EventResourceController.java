package com.soul.calendar.rest;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soul.calendar.entity.Events;
import com.soul.calendar.pojo.CreateEventBody;
import com.soul.calendar.pojo.EventInterval;
import com.soul.calendar.pojo.FindFavourableSlotBody;
import com.soul.calendar.pojo.RecurringEvent;
import com.soul.calendar.service.EventResourceService;

@RestController
@RequestMapping("/api/events")
public class EventResourceController {
    private static final Logger logger= LoggerFactory.getLogger(EventResourceController.class);

    private EventResourceService eventResourceService;

    @Autowired
    public EventResourceController(EventResourceService eventResourceService) {
        this.eventResourceService = eventResourceService;
    }

    @PostMapping("")
    public  ResponseEntity<CreateEventBody> addEvent(@RequestBody CreateEventBody createEventBody)  {

        if(createEventBody.getEndAt().before(createEventBody.getStartAt())) {
            throw new RuntimeException("Invalid input: startAt > endAt");
        }

        eventResourceService.save(createEventBody);
        return new ResponseEntity<>(createEventBody,HttpStatus.CREATED);
    }

    // ?usedID={userId} mapping needed
    @GetMapping("")
    public ResponseEntity< Map<String,List<Integer> >> listEventsByUserId(@RequestParam Integer userId) {
        List<Integer> eventList=eventResourceService.findByUserId(userId);
        Map<String, List<Integer> > map = new HashMap<>();
        map.put("eventList", eventList);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Events> getEventDetailsById(@PathVariable Integer eventId) {
        Events event=eventResourceService.getEventById(eventId);
        return new ResponseEntity<>(event,HttpStatus.OK);
    }

    // ?usedID={userId} mapping needed
    // return conflict in a day
    @GetMapping("/conflicts")
    public ResponseEntity< Map<Integer, List<Events>> > listEventConflictsByUserId(@RequestParam Integer userId) {
        Map<Integer, List<Events>> conflictingEventsMap=eventResourceService.findConflictsByUserId(userId);
        return new ResponseEntity<>(conflictingEventsMap,HttpStatus.OK);
    }

    // return most favourable upcomingSlot

    @PostMapping("/findFavourableSlot")
    public  ResponseEntity<Map<String,Timestamp>> findFavourableSlot(@RequestBody FindFavourableSlotBody findFavourableSlotBody)  {
        EventInterval slot= eventResourceService.findFavourableSlot(findFavourableSlotBody);
        Map<String,Timestamp> res= new HashMap<>();
        res.put("StartAt", new Timestamp(slot.getStartAt()));
        res.put("EndAt", new Timestamp(slot.getEndAt()));
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @PostMapping("/recurringMeeting")
    public ResponseEntity<String> postMethodName(@RequestBody RecurringEvent recurringEvent) {
        eventResourceService.saveRecur(recurringEvent);
        return new ResponseEntity<>("Created",HttpStatus.CREATED);
    }


}
