package com.surirobot.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.surirobot.interfaces.IProcessPicture;
import com.surirobot.task.Task;
import com.surirobot.utils.Emotion;
import com.surirobot.utils.ExecutorSingleton;

/**
 * 
 * @author jussieu
 * 
 * Class permettant de traiter la liste des images reçues en Base64.
 *
 */

public class ProcessPicture implements IProcessPicture{

	private static final Logger logger = LogManager.getLogger();

	/**
	 * Méthode qui crée des threads et récupère le résultat de chaque thread
	 * et fait la moyenne des scores.
	 */
	@Override
	public synchronized String process(List<String> data) {
		logger.info("ProcessPicture : start process : ");
		if(data.size()<1) return "{}";

		List<Callable<JSONObject>> tasks = new ArrayList<>();
		data.forEach(e ->{
			tasks.add(new Task(e));
		});

		CompletionService completionService = ExecutorSingleton.getInstance();
		

		List<JSONObject> scores = new ArrayList<>();
		List<Future<JSONObject>> futures = new ArrayList<Future<JSONObject>>();
		try {
			for(Callable<JSONObject> task : tasks)
				futures.add(completionService.submit(task));

			for(int i = 0; i<tasks.size();i++)
				scores.add((JSONObject) completionService.take().get());

		} catch (InterruptedException | ExecutionException e) {
			logger.error("Interruption Execution Thread...\n"+e.getStackTrace());
		}finally{
			//executor.shutdown();
		};
		return getImportantEmotion(average(scores)).toString();
	}
	
	/**
	 * 
	 * Méthode qui nous retourne l'émotion dominante .
	 * 
	 * @param json content la réponse obtenue de L'API.
	 * @return l'emotion contenant le plus grand score.
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

	/**
	 * Méthode qui fait la moyenne des scores.
	 * @param scores laliste des emotions pour chaque visage.
	 * @return la moyenne pour chaque émotion obtenue.
	 */
	public static JSONObject average(List<JSONObject> scores) {
		logger.info("ProcessPicture : start average");

		JSONObject result = new JSONObject();
		for(Emotion e : Emotion.values()) 
			result.put(e.toString().toLowerCase(),0.0);

		scores.forEach(score -> {
			if(score != null && score.length() != 0)
				for(Emotion e : Emotion.values()) {
					result.put(e.toString().toLowerCase(),
							result.getDouble(e.toString().toLowerCase())+score.getJSONObject("scores").optDouble(e.toString().toLowerCase(),0.0));
				}
		});
		
		if(scores.size()>0)
		for(Emotion e : Emotion.values())
			result.put(e.toString().toLowerCase(),result.getDouble(e.toString().toLowerCase())/scores.size());

		return new JSONObject().put("scores", result);
	}

}
