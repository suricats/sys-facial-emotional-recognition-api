package com.surirobot.task;

import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.surirobot.services.algorithmia.EmotionAlgorithmia;
import com.surirobot.services.microsoftazure.EmotionAzure;

/**
 * @author jussieu
 *
 * Class Thread qui s'occupe de récuperer les émotions d'une image.
 *
 */
public class Task implements Callable<JSONObject>{
	private static final Logger logger = LogManager.getLogger();

	private final String picture;

	public Task(String picture) {
		this.picture = picture;
	}
	
	@Override
	public JSONObject call() throws Exception {
		logger.info("Task : start call");

		return new EmotionAzure().getEmotions(picture);
	}

}
