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


	public List<JSONObject> createListScores(List<Double> scores) {
		List listScores = new ArrayList<>();
		for(Double score : scores) {
			JSONObject tmp = new JSONObject();
			for(Emotion e : Emotion.values()) {
				tmp.put(e.toString().toLowerCase(), score);
			}
			listScores.add(new JSONObject().put("scores", tmp));
		}
		return listScores;
	}

	@Before
	public void before() {
		listImages = new ArrayList<>();
	}

	@Test
	public void processWithoutDataTest() {
		json = new JSONObject(new ProcessPicture().process(listImages));
		assertTrue(json.length() == 0);
	}

	@Test
	public void averageTest() {
		List<Double> scores = new ArrayList<Double>();
		scores.add(1.0);
		scores.add(2.0);
		List<JSONObject> listScores = createListScores(scores);
		JSONObject result = ProcessPicture.average(listScores);
		for(Emotion e : Emotion.values()) {
			assertTrue(result.getJSONObject("scores").getDouble(e.toString().toLowerCase()) == 1.5);
		}
	}

	@Test
	public void averageTest2() {
		List<Double> scores = new ArrayList<Double>();
		scores.add(2.0);
		scores.add(3.0);
		scores.add(4.0);
		List<JSONObject> listScores = createListScores(scores);
		JSONObject result = ProcessPicture.average(listScores);
		for(Emotion e : Emotion.values()) {
			assertTrue(result.getJSONObject("scores").getDouble(e.toString().toLowerCase()) == 3.0);
		}
	}

	@Test
	public void averageTest3() {
		List<Double> scores = new ArrayList<Double>();
		scores.add(2.0);
		List<JSONObject> listScores = createListScores(scores);
		JSONObject result = ProcessPicture.average(listScores);
		for(Emotion e : Emotion.values()) {
			assertTrue(result.getJSONObject("scores").getDouble(e.toString().toLowerCase()) == 2.0);
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

	@Test
	public void getImportantEmotionTest2() {
		List<Double> scores = new ArrayList<Double>();
		scores.add(1.0);
		scores.add(1.0);
		scores.add(1.0);
		List<JSONObject> listScores = createListScores(scores);	
		JSONObject tmp = new JSONObject();
		for(Emotion e : Emotion.values()) {
			if(e == Emotion.SADNESS) {
				tmp.put(e.toString().toLowerCase(), 1);
			}else {
				tmp.put(e.toString().toLowerCase(), 0);
			}
		}
		JSONObject emotion = ProcessPicture.getImportantEmotion(new JSONObject().put("scores", tmp));
		assertTrue(emotion.getString("emotion").equals(Emotion.SADNESS.toString().toLowerCase()));
	}

	@Test
	public void getImportantEmotionTest3() {
		List<Double> scores = new ArrayList<Double>();
		scores.add(1.0);
		scores.add(2.0);
		scores.add(5.0);
		List<JSONObject> listScores = createListScores(scores);	
		JSONObject tmp = new JSONObject();
		for(Emotion e : Emotion.values()) {
			if(e == Emotion.HAPPINESS) {
				tmp.put(e.toString().toLowerCase(), 1);
			}else {
				tmp.put(e.toString().toLowerCase(), 0);
			}
		}
		JSONObject emotion = ProcessPicture.getImportantEmotion(new JSONObject().put("scores", tmp));
		assertTrue(emotion.getString("emotion").equals(Emotion.HAPPINESS.toString().toLowerCase()));
	}
}
