package com.surirobot.services.algorithmia;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jussieu
 * 
 * Cette class permet de tester les methodes définies dans la classe {@link Parser}.
 *
 */

public class ParserTest {
	String s;
	
	@Before
	public void beforeTest() {
		s = "{" + 
				"    \"result\": {" + 
				"        \"results\": [" + 
				"            {" + 
				"                \"bbox\": {" + 
				"                    \"bottom\": 498," + 
				"                    \"left\": 171," + 
				"                    \"right\": 438," + 
				"                    \"top\": 231" + 
				"                }," + 
				"                \"emotions\": [" + 
				"                    {" + 
				"                        \"confidence\": 0.967633," + 
				"                        \"label\": \"Disgust\"" + 
				"                    }," + 
				"                    {" + 
				"                        \"confidence\": 0.0323478," + 
				"                        \"label\": \"Neutral\"" + 
				"                    }," + 
				"                    {" + 
				"                        \"confidence\": 0.0000128," + 
				"                        \"label\": \"Fear\"" + 
				"                    }" + 
				"                ]," + 
				"                \"person\": 0" + 
				"            }" + 
				"        ]" + 
				"    }," + 
				"    \"metadata\": {" + 
				"        \"content_type\": \"json\"," + 
				"        \"duration\": 14.715919915" + 
				"    }" + 
				"}";
	}
	
	@Test
	public void parseTest() {
		JSONObject json = new Parser().parse(s);
		assertTrue(json.has("scores"));
		assertTrue(json.getJSONObject("scores").has("neutral"));
		assertTrue(json.getJSONObject("scores").has("disgust"));
		assertTrue(json.getJSONObject("scores").has("fear"));
	}
	
	
	@Test
	public void parseTest1() {
		JSONObject json = new Parser().parse(s);
		assertTrue(json.getJSONObject("scores").optDouble("neutral")==0.0323478);
		assertTrue(json.getJSONObject("scores").optDouble("disgust")==0.967633);
		assertTrue(json.getJSONObject("scores").optDouble("fear")==0.0000128);
	}
	
	@Test
	public void parseTest2() {
		JSONObject json = new Parser().parse(s);
		assertFalse(json.has("metadata"));
		assertFalse(json.has("result"));
		assertFalse(json.has("results"));
		assertFalse(json.has("metadata"));
		assertTrue(json.has("scores"));
	}
	
	@Test
	public void parseTest3() {
		JSONObject json = new Parser().parse(s);
		assertFalse(json.getJSONObject("scores").has("happiness"));
		assertFalse(json.getJSONObject("scores").has("anger"));
		assertFalse(json.getJSONObject("scores").has("surprise"));
	}
	
	@Test (expected = JSONException.class)
	public void parseTestFail() {
		new Parser().parse("{}");
	}
	
	@Test (expected = JSONException.class)
	public void parseTestFail1() {
		new Parser().parse("[]");
	}
	
	@Test (expected = JSONException.class)
	public void parseTestFail2() {
		new Parser().parse("{result:results{}}");
	}
	
}
