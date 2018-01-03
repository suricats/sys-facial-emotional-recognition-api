package com.surirobot.services.microsoftazure;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.surirobot.services.interfaces.IParser;
import com.surirobot.utils.Emotion;

/*
 * Cette class permet de parser le résutat de la réponse de l'API Azure. 
 */
public class Parser implements IParser{
	private static final Logger logger = LogManager.getLogger();

	/*
	 * Cette méthode parse le résultat de l'API Azure
	 */
	@Override
	public JSONObject parse(String content) {
		logger.info("EmotionAzure Parser : start parse");
		JSONArray json = new JSONArray(content);
		if(json.length()<1) return new JSONObject();
		JSONObject tmp = json.getJSONObject(0).getJSONObject("scores");
		JSONObject score = new JSONObject();
		for(Emotion e : Emotion.values()) {
			score.put(e.toString().toLowerCase(), tmp.optDouble(e.toString().toLowerCase(), 0.0));
		}
		return new JSONObject().put("scores", score);
	}

}
