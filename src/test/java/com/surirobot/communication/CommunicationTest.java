package com.surirobot.communication;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class RequestEmotionTest {

	@Before
	public void before() {

	}

	@Test(expected = NullPointerException.class)
	public void doGetFail() throws Exception{
		RequestEmotion.doGet(null, null);
	}
	
	@Test(expected = IllegalStateException.class)
	public void doGetFail2() throws Exception{
		RequestEmotion.doGet("not-url", null);
	}
	
	@Test(expected = IOException.class)
	public void doGetFail3() throws Exception{
		RequestEmotion.doGet("http://not-a-good-url.com", null);
	}

	@Test(expected = NullPointerException.class)
	public void doPostFail() throws Exception{
		RequestEmotion.doPost(null, null, null);
	}
	
	@Test(expected = IllegalStateException.class)
	public void doPostFail2() throws Exception{
		RequestEmotion.doPost("not-url", null, null);
	}
	
	@Test(expected = IOException.class)
	public void doPostFail3() throws Exception{
		RequestEmotion.doPost("http://not-a-good-url.com", null, null);
	}

}
