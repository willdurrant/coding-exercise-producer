package com.codingexercise.producer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import com.codingexercise.producer.domain.Event;
import com.codingexercise.producer.domain.EventAttribute;

/**
 * Component to load in source file and parse the data into a collection of JSON
 * objects or just pass back data as collection of strings.
 * 
 * @author wdurrant
 */
@Component
public class FileReader {

	private String fileName = "source.txt";

	public List<String> getSourceDataAsText() {
		ClassLoader classLoader = getClass().getClassLoader();

		InputStream inputStream = classLoader.getResourceAsStream(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		List<String> linesAsRawText = new ArrayList<>();
		String lineAsRawText;

		try {
			while ((lineAsRawText = reader.readLine()) != null) {
				linesAsRawText.add(lineAsRawText);
			}
		} catch (IOException e) {
			new RuntimeException("IOException caught trying to read from BufferedReader", e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				new RuntimeException("IOException caught trying to close the reader", e);
			}
		}

		return linesAsRawText;

	}

	public List<Event> getSourceDataAsEvents() {

		List<String> lines = getSourceDataAsText();
		
		List<Event> loadedEvents = new ArrayList<>();

		JSONParser parser = new JSONParser();
		try {

			for (String line : lines) {
				JSONObject jsonObject = (JSONObject) parser.parse(line);
				JSONArray events = (JSONArray) jsonObject.get("events");
				List<EventAttribute> eventAttributes = new ArrayList<>();
				@SuppressWarnings("unchecked")
				Iterator<JSONObject> iterator = events.iterator();
				while (iterator.hasNext()) {
					JSONObject jsonT = (JSONObject) iterator.next();
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
		} catch (ParseException e) {
			new RuntimeException("ParseException caught parsing line into JSON", e);
		}
		return loadedEvents;
	}

}
