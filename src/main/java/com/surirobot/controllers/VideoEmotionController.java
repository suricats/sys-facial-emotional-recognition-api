package com.surirobot.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surirobot.interfaces.IProcessVideo;
import com.surirobot.process.ProcessPicture;
import com.surirobot.process.ProcessVideo;

/**
 * Ce controller reçoit une requete du client qui contient un flux d'images
 * pouvant avoir un ou plusieurs visages accompagné éventuellement
 * d'un flux vocal .
 * ensuite ,il delegue le travail à <code>{@link ProcessVideo }</code> , après 
 * le traitemnt de celle ci ,le controller retourne l'emotion corréspondante pour chaque visage.
 * 
 * 
 */

@Controller
public class VideoEmotionController {
	private static final Logger logger = LogManager.getFormatterLogger();
	@PostMapping(value = "/emotions/actions/retrieve-video-emotion")
	@ResponseBody
	public String video(@RequestBody HashMap<String, String> request, HttpServletResponse response) {
    	logger.info("VideoEmotionController : start video");
		if(!request.containsKey("pictures") && !request.containsKey("record")) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			JSONObject error = new JSONObject();
			error.put("message", "invalid request");
			return error.toString();
		}

		if(request.get("pictures") instanceof String) {
			request.put("pictures", request.get("pictures"));
			if (request.get("record") instanceof String)
				request.put("record", (String) request.get("record"));
			IProcessVideo process = new ProcessVideo();
			return process.process(request);
		}
		else {
			if (request.get("record") instanceof String) {
				request.put("record", (String) request.get("record"));
				IProcessVideo process = new ProcessVideo();
				return process.process(request);
			}
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			JSONObject error = new JSONObject();
			error.put("message", "invalid request");
			return error.toString();
		}
	}
}
