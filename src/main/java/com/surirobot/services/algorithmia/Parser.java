package com.surirobot.services.algorithmia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
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
	 * Cette méthode permet de parser le résultat <pre>response</pre> retourné 
	 * par l'API ,sous form d'un <pre>JSONOBject</pre> contenant les émotions 
	 *  basiques.
	 */
	@Override
	public JSONObject parse(HttpResponse response) {
		logger.info("EmotionAlgorithmia : start parse");
		initializeMap();
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
		JSONObject json = new JSONObject(s);
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
		System.out.println("TEST"+score.toString());
		return new JSONObject().put("scores", score);
	}

}

