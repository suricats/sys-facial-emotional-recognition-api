package com.surirobot.services.vokaturi;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.surirobot.services.interfaces.IParser;
import com.surirobot.utils.Emotion;

/*
 * Cette class permet de parser le résutat de la réponse du serveur Vokaturi. 
 */
public class Parser implements IParser{
	
	private static final Logger logger = LogManager.getLogger();

	/*
	 * Cette méthode parse le résultat de l'API Vokaturi
	 */
	@Override
	public JSONObject parse(String content) {
		logger.info("EmotionVokaturi Parser : start parse");
		
		JSONObject json = new JSONObject(content);
		if(json.length() == 0)
			return new JSONObject();
		return new JSONObject().put("emotion", getImportantEmotion(json));
	}
	
	/*
	 * Méthode qui nous retourne l'émotion dominante
	 */
	public static String getImportantEmotion(JSONObject json) {
		logger.info("EmotionVokaturi ProcessPicture : start getImportantEmotion");
		
		Double max = 0.0;
		String emotion = Emotion.NEUTRAL.toString().toLowerCase();
		Set<String> keys = json.keySet();
		for(String key: keys) {
			if(max<json.getDouble(key)) {
				max = json.getDouble(key);
				emotion = key;
			}
		}
		return emotion;
	}

}