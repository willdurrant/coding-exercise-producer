package com.codingexercise.producer.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event implements Serializable{

	/**
	 * Generated serial id.
	 */
	private static final long serialVersionUID = 4158024838489970681L;
	
	private String id;
	
	private List<EventAttribute> eventAttributes;
    
    public Event() {}

    public Event(List<EventAttribute> eventAttributes){
    	this.eventAttributes = eventAttributes;
    }

	public List<EventAttribute> getEventAttributes() {
		return eventAttributes;
	}

	public String getId() {
		return id;
	}


}

