package com.surirobot.communication;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
/**
 * 
 * @author jussieu
 *
 * Cette class permet de tester les methodes définies dans la classe {@link Communication}
 */
public class CommunicationTest {

	@Before
	public void before() {

	}

	@Test(expected = NullPointerException.class)
	public void doGetFail() throws Exception{
		Communication.doGet(null, null);
	}
	
	@Test(expected = IllegalStateException.class)
	public void doGetFail2() throws Exception{
		Communication.doGet("not-url", null);
	}
	
	@Test(expected = IOException.class)
	public void doGetFail3() throws Exception{
		Communication.doGet("http://not-a-good-url.com", null);
	}

	@Test(expected = NullPointerException.class)
	public void doPostFail() throws Exception{
		Communication.doPost(null, null, null);
	}
	
	@Test(expected = IllegalStateException.class)
	public void doPostFail2() throws Exception{
		Communication.doPost("not-url", null, null);
	}
	
	@Test(expected = IOException.class)
	public void doPostFail3() throws Exception{
		Communication.doPost("http://not-a-good-url.com", null, null);
	}
	

}
