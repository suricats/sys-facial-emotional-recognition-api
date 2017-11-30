package com.surirobot.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.surirobot.utils.Emotion;

public class EmotionAzureTest {

	List<JSONObject> list;
	
	
	@Before
	public void beforeTest() {
		list = new ArrayList<>();
		JSONObject tmp = new JSONObject();
		for(Emotion e : Emotion.values()) {
			tmp.put(e.toString().toLowerCase(), 1.0);
		}
		list.add(new JSONObject().put("scores", tmp));
		tmp = new JSONObject();
		for(Emotion e : Emotion.values()) {
			tmp.put(e.toString().toLowerCase(), 2.0);
		}
		list.add(new JSONObject().put("scores", tmp));
	}
	


}
