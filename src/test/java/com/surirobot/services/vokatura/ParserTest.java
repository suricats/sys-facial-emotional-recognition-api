package com.surirobot.services.vokatura;

import static org.junit.Assert.assertTrue;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.surirobot.process.ProcessPicture;
import com.surirobot.services.vokaturi.Parser;
import com.surirobot.utils.Emotion;
/**
 * 
 * @author jussieu
 * 
 * Cette class permet de tester les methodes définies dans la classe {@link Parser}.
 *
 */
public class ParserTest {

	@Test
	public void parseTest() {
		String s = "{"+
				"      \"anger\": 0.7," +  
				"      \"fear\": 0.22," + 
				"      \"happiness\": 0.08," + 
				"      \"neutral\": 0.0," + 
				"      \"sadness\": 0.0," +
			"}";
		JSONObject json = new Parser().parse(s);
		assertTrue(json.getString("emotion").equals(Emotion.ANGER.toString().toLowerCase()));
	}
	
	@Test
	public void parseTest2() {
		String s = "{"+
				"      \"anger\": 0.2," +  
				"      \"fear\": 0.2," + 
				"      \"happiness\": 0.4," + 
				"      \"neutral\": 0.2," + 
				"      \"sadness\": 0.2," +
			"}";
		JSONObject json = new Parser().parse(s);
		assertTrue(json.getString("emotion").equals(Emotion.HAPPINESS.toString().toLowerCase()));
	}
	
	@Test
	public void parseTest3() {
		String s = 
				"{"+
				"      \"anger\": 0.0," +  
				"      \"fear\": 0.0," + 
				"      \"happiness\": 0.0," + 
				"      \"neutral\": 0.0," + 
				"      \"sadness\": 0.0," +
				"}";
		JSONObject json = new Parser().parse(s);
		assertTrue(json.getString("emotion").equals(Emotion.NEUTRAL.toString().toLowerCase()));
	}
	
	@Test
	public void parseTest4() {
		String s = "{"+
				"      \"anger\": 0.2," +  
				"      \"fear\": 0.22," + 
				"      \"happiness\": 0.08," + 
				"      \"neutral\": 0.0," + 
				"      \"sadness\": 0.5," +
			"}";
		JSONObject json = new Parser().parse(s);
		assertTrue(json.getString("emotion").equals(Emotion.SADNESS.toString().toLowerCase()));
	}
	
	@Test
	public void parseTest5() {
		String s = "{"+
				"      \"anger\": 0.7," +  
				"      \"fear\": 0.22," + 
				"      \"happiness\": 0.08," + 
				"      \"neutral\": 0.0," + 
				"      \"sadness\": 0.0," +
			"}";
		JSONObject json = new Parser().parse(s);
		assertTrue(json.has("emotion"));
	}
	
	public void parseTest6() {
		assertTrue(new Parser().parse("{}").equals(new JSONObject()));
	}
	
	@Test (expected = JSONException.class)
	public void parseTestFail() {
		new Parser().parse("[]");
	}
	
	@Test (expected = JSONException.class)
	public void parseTestFail3() {
		new Parser().parse("[a:{},b:{}]");
	}
}
