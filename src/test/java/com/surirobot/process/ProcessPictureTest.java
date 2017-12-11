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
	
	/*
	 * Test de process pour une image incorecte
	 * Le système renvoi bien le score, mais les émotions
	 * toute à 0
	 */
	@Test
	public void processTest() {
		listImages.add("");
		json = new JSONObject(new ProcessPicture().process(listImages));
		assertTrue(json.length() != 0);
		assertTrue(json.getString("emotion").equals("neutral"));
			
	}
	
	@Test
	public void averageTest() {
		JSONObject result = ProcessPicture.average(listScores);
		for(Emotion e : Emotion.values()) {
			assertTrue(result.getJSONObject("scores").getDouble(e.toString().toLowerCase()) == 1.5);
		}
	}
}
