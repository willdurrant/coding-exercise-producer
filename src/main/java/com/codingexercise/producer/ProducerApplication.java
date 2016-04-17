package com.codingexercise.producer;

import java.io.File;
import java.util.List;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.util.Assert;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.client.RestTemplate;

import com.codingexercise.producer.domain.Event;
import com.sun.javafx.binding.StringFormatter;

@SpringBootApplication
@EnableJms
public class ProducerApplication implements CommandLineRunner {

	private static final String MAILBOX_EVENTS_DESTINATION = "mailbox-events-destination";

	private static final String EVENTS_REST_URL = "http://localhost:9080/events";

	private static final Logger log = LoggerFactory.getLogger(ProducerApplication.class);

	private static final String API_ARG_SWITCH = "-api";

	/**
	 * Inner Enum to represent the selected lightweight API mechanism for
	 * calling the consumer.
	 * 
	 * @author wdurrant
	 */
	private enum API_TYPE {
		HTTP, JMS
	}

	@Autowired
	private FileReader fileReader;

	@Autowired
	private JmsTemplate jmsTemplate;

	/**
	 * Spring Java Config instantiation of the JMS container factory.
	 * 
	 * @param connectionFactory
	 * @return JmsListenerContainerFactory<?>
	 */
	@Bean
	JmsListenerContainerFactory<?> myJmsContainerFactory(ConnectionFactory connectionFactory) {
		SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		return factory;
	}

	/**
	 * Main method invoked upon running.
	 * 
	 * @param args
	 *            as String[]. Optionally can include the switch for specifying
	 *            API type in the format of -api <option>
	 */
	public static void main(String[] args) {
		// Clean out any ActiveMQ data from a previous run
		FileSystemUtils.deleteRecursively(new File("activemq-data"));

		SpringApplication.run(ProducerApplication.class, args);
	}

	/**
	 * Delegated to by the main() method.
	 */
	@Override
	public void run(String... args) throws Exception {

		API_TYPE apiType = API_TYPE.HTTP;

		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				int nextIndex = i + 1;
				if (nextIndex < args.length && args[i].equalsIgnoreCase(API_ARG_SWITCH)) {
					if (args[nextIndex].equalsIgnoreCase(API_TYPE.JMS.toString())) {
						apiType = API_TYPE.JMS;
					}
				}
			}
		}

		log.info("The Producer will send Event data to the Consumer using API type of : {}", apiType);

		switch (apiType) {
		case HTTP:
			List<Event> jsonEvents = null;
			try {
				jsonEvents = fileReader.getSourceDataAsEvents();
			} catch (RuntimeException e) {
				log.error(
						"Fatal error caught trying to parse source file into Event objects. Terminating the application!");
				System.exit(0);
			}
			for (Event event : jsonEvents) {
				postEvent(event);
			}
			break;
		case JMS:
			List<String> linesAsRawText = null;
			try {
				linesAsRawText = fileReader.getSourceDataAsText();
			} catch (RuntimeException e) {
				log.error("Fatal error caught trying to parse source file. Terminating the application!");
				System.exit(0);
			}
			for (String lineAsText : linesAsRawText) {
				sendEvent(lineAsText);
			}
			break;
		}

	}

	/**
	 * 
	 * @param event as Event
	 */
	private void postEvent(Event event) {

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

		ResponseEntity<Event> persistedEvent = restTemplate.postForEntity(EVENTS_REST_URL, event,
				Event.class);

		HttpStatus status = persistedEvent.getStatusCode();

		Assert.isTrue(status.equals(HttpStatus.CREATED),
				String.format(
						"Problem persisting event via HTTP Rest API. Expected HTTP Status code or 201 (Created) but got %s",
						status));
	}

	private void sendEvent(String lineAsText) {
		// Send a message
		MessageCreator messageCreator = new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(lineAsText);
			}
		};
		jmsTemplate.send(MAILBOX_EVENTS_DESTINATION, messageCreator);
	}

}
