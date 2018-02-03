package com.surirobot.services.vokaturi;

import java.io.IOException;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.surirobot.communication.Communication;
import com.surirobot.communication.ResponseHolder;
import com.surirobot.interfaces.services.IService;

/**
 * 
 * @author jussieu
 *
 * Class à la quelle on donne un son et qui s'occupe d'appeler Vokaturi pour
 * la reconnaissance des émotions.
 */
public class EmotionVokaturi implements IService<JSONObject, String, String>{

	private static final Logger logger = LogManager.getLogger();

	private static final String URL = "http://localhost:5000/";

	/**
	 * Appelle vokaturi, transmet le résultat au parser
	 * et retourne le résultat parsé.
	 */
	@Override
	public JSONObject getEmotions(String data) {
		logger.info("EmotionVokaturi : start getEmotions");
		try {
			return new Parser().parse(send(data));
		}catch(JSONException e) {
			return new JSONObject();
		}
	}

	/**
	 * Méthode pour appeler Vokaturi.
	 */
	@Override
	public String send(String data) {
		logger.info("EmotionVokaturi : start send");

		String body = new JSONObject().put("record", data).toString();
		StringEntity requestEntity = new StringEntity(
				body,
				ContentType.APPLICATION_JSON);
		try {
			ResponseHolder responseHolder = Communication.doPost(URL, null, requestEntity);
			return responseHolder.content;
		}catch(IOException e) {
			return "{}";
		}
	}

}
