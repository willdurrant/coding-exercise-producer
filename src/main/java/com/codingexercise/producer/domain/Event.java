package com.codingexercise.producer.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Domain object to represent Customer Event.
 * 
 * @author wdurrant
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event implements Serializable{

	/**
	 * Generated serial id.
	 */
	private static final long serialVersionUID = 4158024838489970681L;
	
	/**
	 * Id only populated if persisted via HTTP rest.
	 */
	private String id;
	
	/**
	 * Collection of event attributes.
	 */
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventAttributes == null) ? 0 : eventAttributes.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (eventAttributes == null) {
			if (other.eventAttributes != null)
				return false;
		} else if (!eventAttributes.equals(other.eventAttributes))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

