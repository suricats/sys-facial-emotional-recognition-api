package com.surirobot.services.algorithmia;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.surirobot.services.interfaces.IParser;
import com.surirobot.utils.Emotion;

/*
 * Cette class permet de parser le résutat de la réponse de l'API Algorithmia. 
 */
public class Parser implements IParser{
	private static final Logger logger = LogManager.getLogger();
	private static Map<String, String> emotions = null;

	public static void initializeMap() {
		if(emotions == null) {
			emotions = new HashMap<>();
			emotions.put("Sad", Emotion.SADNESS.toString());
			emotions.put("Happy", Emotion.HAPPINESS.toString());
			emotions.put("Angry", Emotion.ANGER.toString());
			emotions.put("Neutral", Emotion.NEUTRAL.toString());
			emotions.put("Disgust", Emotion.DISGUST.toString());
			emotions.put("Fear", Emotion.FEAR.toString());
			emotions.put("Surprise", Emotion.SURPRISE.toString());
		}
	}

	/*
	 * Cette méthode parse le résultat de l'API Algorithmia
	 */
	@Override
	public JSONObject parse(String response) {
		logger.info("EmotionAlgorithmia Parser : start parse");
		initializeMap();
		JSONObject json = new JSONObject(response);
		JSONArray results = json.getJSONObject("result").optJSONArray("results");
		if(results.length()<1) return new JSONObject();
		JSONArray tmp = results.getJSONObject(0).optJSONArray("emotions");
		JSONObject score = new JSONObject();
		JSONObject js;
		for(int i =0;i<tmp.length();++i) {
			js = tmp.getJSONObject(i);
			String emotion = emotions.get(js.optString("label"));
			score.put(emotion.toLowerCase(), js.optDouble("confidence"));
		}
		return new JSONObject().put("scores", score);
	}

}

