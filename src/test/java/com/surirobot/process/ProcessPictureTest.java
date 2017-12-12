package com.surirobot.process;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.surirobot.utils.Emotion;

public class ProcessPictureTest {
	List<String> listImages;
	List<JSONObject> listScores;
	JSONObject json = null;
	
	@Before
	public void before() {
		listImages = new ArrayList<>();
		listScores = new ArrayList<>();
		JSONObject tmp = new JSONObject();
		for(Emotion e : Emotion.values()) {
			tmp.put(e.toString().toLowerCase(), 1.0);
		}
		listScores.add(new JSONObject().put("scores", tmp));
		tmp = new JSONObject();
		for(Emotion e : Emotion.values()) {
			tmp.put(e.toString().toLowerCase(), 2.0);
		}
		listScores.add(new JSONObject().put("scores", tmp));
	}
	
	@Test
	public void processWithoutDataTest() {
		json = new JSONObject(new ProcessPicture().process(listImages));
		assertTrue(json.length() == 0);
	}
	
	@Test
	public void averageTest() {
		JSONObject result = ProcessPicture.average(listScores);
		for(Emotion e : Emotion.values()) {
			assertTrue(result.getJSONObject("scores").getDouble(e.toString().toLowerCase()) == 1.5);
		}
	}
	
	@Test
	public void getImportantEmotionTest() {
		JSONObject tmp = new JSONObject();
		for(Emotion e : Emotion.values()) {
			tmp.put(e.toString().toLowerCase(), 0);
		}
		JSONObject emotion = ProcessPicture.getImportantEmotion(new JSONObject().put("scores", tmp));
		assertTrue(emotion.getString("emotion").equals(Emotion.NEUTRAL.toString().toLowerCase()));
	}
}
