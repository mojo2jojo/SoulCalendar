package com.soul.calendar.pojo;

import java.util.Arrays;

public class FindFavourableSlotBody {
    
    private Integer[] participants;

    private Integer durationInMinutes;

    public FindFavourableSlotBody(Integer[] participants, Integer durationInMinutes) {
        this.participants = participants;
        this.durationInMinutes = durationInMinutes;
    }

    public Integer[] getParticipants() {
        return participants;
    }

    public void setParticipants(Integer[] participants) {
        this.participants = participants;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    @Override
    public String toString() {
        return "FindFavourableSlotBody [participants=" + Arrays.toString(participants) + ", durationInMinutes="
                + durationInMinutes + "]";
    }

}
