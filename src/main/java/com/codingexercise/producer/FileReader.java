package com.codingexercise.producer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import com.codingexercise.producer.domain.Event;
import com.codingexercise.producer.domain.EventAttribute;

/**
 * Component to load in source file and parse the data into a collection of JSON objects.
 * 
 * @author wdurrant
 */
@Component
public class FileReader {

	private String fileName = "source.txt";

	public List<Event> loadEventsFromFile(){
	
		List<Event> loadedEvents = new ArrayList<>();
		
		JSONParser parser = new JSONParser();
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
//		File file = new File("C:\\Dev\\workspaces\\sts373\\Producer\\src\\main\\resources\\source.txt");
		 try {
			List<String> lines = FileUtils.readLines(file, "UTF-8");
			for (String line : lines) {
				JSONObject jsonObject = (JSONObject) parser.parse(line);
				JSONArray events = (JSONArray) jsonObject.get("events");
				List<EventAttribute> eventAttributes = new ArrayList<>();
	            @SuppressWarnings("unchecked")
				Iterator<JSONObject> iterator = events.iterator();	            
	            while (iterator.hasNext()) {
	            	JSONObject jsonT = (JSONObject)iterator.next();
	            	JSONObject jsonEventAttribute = (JSONObject) jsonT.get("attributes");
	            	String accountNum = (String) jsonEventAttribute.get("Account Number");
	                String txAmount = (String) jsonEventAttribute.get("Transaction Amount");
	                String cardMemberName = (String) jsonEventAttribute.get("Name");
	                String product = (String) jsonEventAttribute.get("Product");
	                EventAttribute eventAttribute = new EventAttribute(accountNum, txAmount, cardMemberName, product);
	                eventAttributes.add(eventAttribute);
	            }
				
                loadedEvents.add(new Event(eventAttributes));
                
			}
		} catch (IOException e) {
			new RuntimeException("IOException caught trying to read source file", e);
		} catch (ParseException e) {
			new RuntimeException("ParseException caught parsing line into JSON", e);
		}
		 return loadedEvents;
	}
	
	
}
