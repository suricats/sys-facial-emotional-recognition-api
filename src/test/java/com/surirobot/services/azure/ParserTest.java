package com.surirobot.services.azure;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.surirobot.process.ProcessPicture;
import com.surirobot.services.microsoftazure.Parser;
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
		s ="[" + 
				"  {" + 
				"    \"faceRectangle\": {" + 
				"      \"top\": 114," + 
				"      \"left\": 212," + 
				"      \"width\": 65," + 
				"      \"height\": 65" + 
				"    }," + 
				"    \"scores\": {" + 
				"      \"anger\": 0.1," + 
				"      \"contempt\": 0.1," + 
				"      \"disgust\": 0.1," + 
				"      \"fear\": 0.1," + 
				"      \"happiness\": 0.1," + 
				"      \"neutral\": 0.1," + 
				"      \"sadness\": 0.1," + 
				"      \"surprise\": 0.1" + 
				"    }" + 
				"  }]";
	}

	@Test
	public void parseTest() {
		JSONObject json = new Parser().parse(s);
		assertTrue(json.optJSONObject("faceRectangle")==null);
		assertTrue(json.optJSONObject("scores")!=null);
		assertTrue(json.optJSONObject("scores").optDouble("anger") == 0.1);
	}
	
	@Test
	public void parseTest1() {
		JSONObject json = new Parser().parse(s);
		assertFalse(json.has("faceRectangle"));
		assertTrue(json.has("scores"));
		assertTrue(json.optJSONObject("scores").has("anger"));
	}

	@Test
	public void parseEmptyArrayResponseTest() {
		JSONObject json = new Parser().parse("[]");
		assertTrue(json.similar(new JSONObject("{}")));
	}

}
