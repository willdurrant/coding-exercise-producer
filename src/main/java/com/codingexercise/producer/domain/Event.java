package com.codingexercise.producer.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

	private List<EventAttribute> eventAttributes;
    
    public Event() {}

    public Event(List<EventAttribute> eventAttributes){
    	this.eventAttributes = eventAttributes;
    }

	public List<EventAttribute> getEventAttributes() {
		return eventAttributes;
	}

    
//    @Override
//    public String toString() {
//        return String.format(
//                "Event[id=%s, firstName='%s', lastName='%s']",
//                id, firstName, lastName);
//    }

}

