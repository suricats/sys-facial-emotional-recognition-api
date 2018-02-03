package com.surirobot.services.beyondverbal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.surirobot.interfaces.services.IParser;


/**
 * @author jussieu.
 * Cette class permet de parser le résutat de la réponse de l'API Beyond. 
 * 
 */
public class Parser implements IParser{
	private static final Logger logger = LogManager.getLogger();

	/**
	 * Cette méthode parse le résultat de l'API Beyond.
	 */
	@Override
	public JSONObject parse(String content) {
		logger.info("EmotionBeyond Parser : start parse");
		logger.info(content);
		JSONObject result = new JSONObject(content)
				.getJSONObject("result");
		if(result != null && result.has("analysisSegments")) {
			JSONObject emotion = result.getJSONArray("analysisSegments")
					.getJSONObject(0).getJSONObject("analysis")
					.getJSONObject("Mood")
					.getJSONObject("Group11")
					.getJSONObject("Primary");
			logger.info(result.toString());
			String[] emotions = emotion.getString("Phrase").split(", ");
			return new JSONObject().put("emotion", new JSONArray(emotions));
		}

		return new JSONObject().put("emotion",new JSONArray());
	}

}
