package com.surirobot.services.microsoftazure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import com.surirobot.services.interfaces.IParser;
import com.surirobot.utils.Emotion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Cette class permet de parser le résutat de la réponse de l'API intérogée. 
 */
public class Parser implements IParser{
	private static final Logger logger = LogManager.getLogger();

	/*
	 * Cette méthode permet de parser le résultat <pre>response</pre> retourné 
	 * par l'API ,sous form d'un <pre>JSONOBject</pre> contenant les émotions 
	 *  basiques.
	 */
	@Override
	public JSONObject parse(HttpResponse response) {
		logger.info("EmotionAzure : start parse");
		if(response.getStatusLine().getStatusCode() != 200) return new JSONObject();
		String s = "";
		try {
			BufferedReader bis = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String tmp = null;
			while((tmp = bis.readLine())!=null) {
				s += tmp;
			}
		} catch (UnsupportedOperationException | IOException e) {
			e.printStackTrace();
		}
		JSONArray json = new JSONArray(s);
		if(json.length()<1) return new JSONObject();
		JSONObject tmp = json.getJSONObject(0).getJSONObject("scores");
		JSONObject score = new JSONObject();
		for(Emotion e : Emotion.values()) {
			score.put(e.toString().toLowerCase(), tmp.optDouble(e.toString().toLowerCase(), 0.0));
		}
		return new JSONObject().put("scores", score);
	}

}
