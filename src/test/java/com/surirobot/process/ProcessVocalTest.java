package com.surirobot.process;

import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProcessVocalTest {
	
	@Test
	public void processTest() {
		String s = new ProcessVocal().process(null);
		assertEquals(new JSONObject(s).length(), 0);
		
	}
	
	@Test
	public void processTest2() {
		String s = new ProcessVocal().process("");
		assertEquals(new JSONObject(s).length(), 0);
	}

}
