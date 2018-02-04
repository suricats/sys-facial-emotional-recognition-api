package com.surirobot.controllers;

import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surirobot.process.ProcessVideo;

/**
 * Ce controller reçoit une requete du client qui contient un flux d'images
 *  accompagné éventuellement d'un flux vocal  por chaque personne.
 * ensuite ,il delegue le travail à <code>{@link ProcessVideo }</code> , après 
 * le traitemnt de celle ci ,le controller retourne l'emotion corréspondante pour chaque visage.
 * 
 * 
 */
@Controller
public class VideosEmotionController {
	private static final Logger logger = LogManager.getFormatterLogger();

	@PostMapping(value = "/emotions/actions/retrieve-videos-emotion")
	@ResponseBody
	public String video(@RequestBody HashMap<String, HashMap<String,String>> request, HttpServletResponse response) {
    	logger.info("VideosEmotionController : start video");
    	JSONObject result = new JSONObject();
    	if(request.size()>0) {
    		for(Entry<String, HashMap<String, String>> entry : request.entrySet()) {
    		    String id  = entry.getKey();
    		     result.put(id, new JSONObject(new ProcessVideo().process(entry.getValue())));
    		}
    		return result.toString();
    	}
    	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		JSONObject error = new JSONObject();
		error.put("message", "invalid request");
		return error.toString();
	
	}
}
