package com.surirobot.services.vokaturi;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.surirobot.interfaces.services.IParser;
import com.surirobot.utils.Emotion;

/**
 * 
 * @author rjussieu
 *
 * Cette class permet de parser le résutat de la réponse du serveur Vokaturi.
 */
public class Parser implements IParser{
	
	private static final Logger logger = LogManager.getLogger();

	/**
	 * Cette méthode parse le résultat de l'API Vokaturi.
	 */
	@Override
	public JSONObject parse(String content)throws JSONException {
		logger.info("EmotionVokaturi Parser : start parse");
		
		JSONObject json = new JSONObject(content);
		if(json.length() == 0)
			return new JSONObject();
		return new JSONObject().put("emotion", getImportantEmotion(json));
	}
	
	/**
	 * Méthode qui nous retourne l'émotion dominante.
	 * @param json contient toutes les émotions obtenues par l'API.
	 * @return l'motion contenante le plus grand score.
	 * @throws JSONException lancée en cas d'erreur.
	 */
	public static String getImportantEmotion(JSONObject json) throws JSONException {
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