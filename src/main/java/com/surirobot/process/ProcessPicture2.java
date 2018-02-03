package com.surirobot.process;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.surirobot.interfaces.IProcessPicture2;
import com.surirobot.services.microsoftazure.EmotionAzure;
import com.surirobot.utils.Emotion;

/**
 * 
 * @author jussieu
 *
 * Class permettant de traiter la liste des images reçues en Base64. 
 */

public class ProcessPicture2 implements IProcessPicture2{

	private static final Logger logger = LogManager.getLogger();

	
	
	/**
	 * 
	 * Méthode qui crée des threads et récupère le résultat de chaque thread
	 * et fait la moyenne des scores.
	 */
	
	@Override
	public synchronized String process(String data) {
		logger.info("ProcessPicture : start process : ");
		if(!(data != null && data.length()>0)) return "[]";

		try {
			JSONArray json = new EmotionAzure().getEmotions(data);
			JSONArray result = new JSONArray();
			json.forEach(item ->{
				JSONObject obj = (JSONObject) item;
				result.put(getImportantEmotion(obj));
			});
			return result.toString();
		}catch(Exception e) {
			return new JSONArray().toString();
		}
	}

	/**
	 * Méthode qui nous retourne l'émotion dominante.
	 * @param json contien toutes les émotions obtenues.
	 * @return l'émotion la plus dominanate (contenantle plus grand score).
	 */
	public static JSONObject getImportantEmotion(JSONObject json) {
		logger.info("ProcessPicture : start getImportantEmotion");
		Double max = 0.0;
		String s = Emotion.NEUTRAL.toString().toLowerCase();
		Map<String, Object> map = json.getJSONObject("scores").toMap();
		for(Entry<String, Object> e: map.entrySet()) {
			if(e.getValue() instanceof Integer) {
				e.setValue((Double)((Integer) e.getValue()).doubleValue());
			}
			if((max) < (Double) e.getValue()) {
				max = ((Double) e.getValue());
				s = e.getKey();
			}
		}
		return new JSONObject().put("emotion", s);
	}


}
