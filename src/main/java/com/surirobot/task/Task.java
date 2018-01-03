package com.surirobot.task;

import java.util.concurrent.Callable;

import org.json.JSONObject;

import com.surirobot.services.algorithmia.EmotionAlgorithmia;
import com.surirobot.services.microsoftazure.EmotionAzure;

/*
 * Class Thread qui s'occupe de récuperer les émotions d'une image
 */
public class Task implements Callable<JSONObject>{
	private final String picture;

	public Task(String picture) {
		this.picture = picture;
	}
	
	@Override
	public JSONObject call() throws Exception {
		return new EmotionAzure().getEmotions(picture);
	}

}
