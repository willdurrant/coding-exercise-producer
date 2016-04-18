package com.codingexercise.producer;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.codingexercise.producer.domain.Event;
import com.codingexercise.producer.domain.EventAttribute;

/**
 * JUnit test for the ProducerApplication.
 * 
 * @author wdurrant
 */
public class ProducerApplicationTest {

	private ProducerApplication classUnderTest = new ProducerApplication();

	@Mock
	private FileReader mockFileReader;

	@Mock
	private JmsTemplate mockJmsTemplate;
	
	@Mock
	private RestTemplate mockRestTemplate;
	
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(classUnderTest, "fileReader", mockFileReader);
		ReflectionTestUtils.setField(classUnderTest, "jmsTemplate", mockJmsTemplate);
		ReflectionTestUtils.setField(classUnderTest, "restTemplate", mockRestTemplate);
	}

	@Test
	public void test_Jms_Complete_HappyDays_Flow() throws Exception {
		List<String> mockLineData = new ArrayList<>();
		mockLineData.add(
				"{\"events\":[{\"attributes\":{\"Account Number\":\"370000000000000\",\"Transaction Amount\":\"USD 25000.00\",\"Name\":\"Lewis Hamilton\",\"Product\":\"Centurion\"}}]}");
		mockLineData.add(
				"{\"events\":[{\"attributes\":{\"Account Number\":\"370000000000001\",\"Transaction Amount\":\"USD 1000.00\",\"Name\":\"Nico Rosberg\",\"Product\":\"Platinum\"}}]}");
		Mockito.when(mockFileReader.getSourceDataAsText()).thenReturn(mockLineData);

		String[] args = { "-api", "jms" };
		//invoke classUnderTest
		classUnderTest.run(args);
		
		//verify outcome
		Mockito.verify(mockFileReader).getSourceDataAsText();
		Mockito.verify(mockFileReader, Mockito.never()).getSourceDataAsEvents();
		//verify JMS Template is called only 2 times
		Mockito.verify(mockJmsTemplate, Mockito.times(2)).send(Mockito.anyString(), Mockito.any(MessageCreator.class));
	}
	

	@Test
	public void test_Http_Complete_HappyDays_Flow() throws Exception {
		
		List<Event> mockEventData = new ArrayList<>();
		List<EventAttribute> attributes = new ArrayList<>();
		EventAttribute attribute1 = new EventAttribute("000123", "$1000", "John Smith", "Platinum");
		attributes.add(attribute1);
		Event event1 = new Event(attributes);
		mockEventData.add(event1);
		Mockito.when(mockFileReader.getSourceDataAsEvents()).thenReturn(mockEventData);
		ResponseEntity<Event> responseEntity = new ResponseEntity<Event>(HttpStatus.CREATED);
		Mockito.when(mockRestTemplate.postForEntity(Mockito.anyString(), Mockito.any(Event.class), (Class<Event>) Mockito.any(Class.class))).thenReturn(responseEntity);
		
		
		String[] args = { "-api", "http" };
		//invoke classUnderTest
		classUnderTest.run(args);
		Mockito.verify(mockFileReader, Mockito.never()).getSourceDataAsText();
		Mockito.verify(mockFileReader).getSourceDataAsEvents();
		//verify RestTemplate is called only once
		Mockito.verify(mockRestTemplate).postForEntity(Mockito.anyString(), Mockito.any(Event.class), (Class<Event>) Mockito.any(Class.class));
	}

	@Test
	public void testRun_DefaultToHttp() throws Exception {
		String[] args = null;
		classUnderTest.run(args);
		Mockito.verify(mockFileReader, Mockito.never()).getSourceDataAsText();
		Mockito.verify(mockFileReader).getSourceDataAsEvents();
	}

	@Test
	public void testRun_DefaultToHttpIfIncorrect() throws Exception {
		String[] args = { "-api", "some_incorrect_value" };
		classUnderTest.run(args);
		Mockito.verify(mockFileReader, Mockito.never()).getSourceDataAsText();
		Mockito.verify(mockFileReader).getSourceDataAsEvents();
	}



}
