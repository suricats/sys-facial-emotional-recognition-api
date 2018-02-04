package com.surirobot.services.microsoftazure;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.surirobot.interfaces.services.IParser;
import com.surirobot.utils.Emotion;

/**
 * 
 * @author jussieu
 *
 * Cette class permet de parser le résutat de la réponse de l'API Azure. 
 */
public class Parser implements IParser<JSONArray>{
	private static final Logger logger = LogManager.getLogger();
	
	/**
	 * Cette méthode parse le résultat de l'API Azure.
	 * @param content le contenu à parser sous format {@link String}
	 * @return le résultat du parser sous format {@link JSONArray}
	 * @throws JSONException l'exception lancée en cas d'erreur.
	 */
	public JSONArray parse(String content) throws JSONException {
		logger.info("EmotionAzure Parser : start parse");
		JSONArray json = null;
		json = new JSONArray(content);
		if(json.length()<1) return new JSONArray();
		JSONArray result = new JSONArray();
		json.forEach(item -> {
		    JSONObject tmp = ((JSONObject) item).getJSONObject("scores");
		    JSONObject score = new JSONObject();
			for(Emotion e : Emotion.values()) {
				score.put(e.toString().toLowerCase(), tmp.optDouble(e.toString().toLowerCase(), 0.0));
			}
			result.put(new JSONObject().put("scores", score));
		});
		
		return result;
	}

}
