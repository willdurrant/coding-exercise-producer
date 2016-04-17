/**
 * 
 */
package com.codingexercise.producer;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import com.codingexercise.producer.domain.Event;
import com.codingexercise.producer.domain.EventAttribute;

/**
 * Unit test for FileReader
 * 
 * @author wdurrant
 *
 */
public class FileReaderTest {

	private FileReader classUnderTest = new FileReader();

	/**
	 * Test method for
	 * {@link com.codingexercise.producer.FileReader#getSourceDataAsText()}.
	 */
	@Test
	public void testGetSourceDataAsText() {
		List<String> lines = classUnderTest.getSourceDataAsText();
		Assert.assertNotNull(lines);
		Assert.assertTrue(lines.size() > 0);
		for (String line : lines) {
			Assert.assertTrue(line.contains("events"));
			Assert.assertTrue(line.contains("attributes"));
			Assert.assertTrue(line.contains("Account Number"));
			Assert.assertTrue(line.contains("Transaction Amount"));
			Assert.assertTrue(line.contains("Name"));
			Assert.assertTrue(line.contains("Product"));
		}
	}
	
	/**
	 * Test method for
	 * {@link com.codingexercise.producer.FileReader#getSourceDataAsEvents()}.
	 */
	@Test
	public void testGetSourceDataAsEvents() {
		List<Event> jsonEvents = classUnderTest.getSourceDataAsEvents();
		Assert.assertNotNull(jsonEvents);
		Assert.assertTrue(jsonEvents.size() > 0);
		for (Event event : jsonEvents) {
			List<EventAttribute> eventAttributes = event.getEventAttributes();
			Assert.assertTrue(eventAttributes.size() > 0);
			for (EventAttribute eventAttribute : eventAttributes) {
				Assert.assertTrue(!StringUtils.isEmpty(eventAttribute.getAccountNum()));
				Assert.assertTrue(!StringUtils.isEmpty(eventAttribute.getCardMemberName()));
				Assert.assertTrue(!StringUtils.isEmpty(eventAttribute.getTxAmount()));
				Assert.assertTrue(!StringUtils.isEmpty(eventAttribute.getProduct()));
			}
		}
	}

}
