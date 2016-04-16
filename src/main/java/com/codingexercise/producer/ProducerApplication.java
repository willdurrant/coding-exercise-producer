package com.codingexercise.producer;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.codingexercise.producer.domain.Event;

@SpringBootApplication
public class ProducerApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(ProducerApplication.class);

	@Autowired
	private FileReader fileReader;
	
	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<Event> jsonEvents = fileReader.loadEventsFromFile();
		for (Event event : jsonEvents) {
			postEvent(event);
		}
	}

	private HttpStatus postEvent(Event event) throws Exception {

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		
		ResponseEntity<Event> entity = restTemplate.postForEntity("http://localhost:9080/events", event, Event.class);

		HttpStatus status = entity.getStatusCode();

		if (status.equals(HttpStatus.CREATED)) {
			
		}
		return status;
	}

}
