package com.surirobot.controllers;

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

import com.surirobot.process.ProcessPicture;

/**
 * Ce controller reçoit une requete du client qui contient un flux d'images 
 * pouvant avoir un ou plusieurs visages.
 * ensuite ,il delegue le travail à <code>{@link ProcessPicture }</code> , après 
 * le traitemnt de celle ci ,le controller retourne l'emotion corréspondante pour chaque visage.
 * 
 * 
 */
@Controller
public class FacialEmotionController {
	private static final Logger logger = LogManager.getFormatterLogger();
	@PostMapping(value = "/emotions/actions/retrieve-facial-emotion")
	@ResponseBody
	public String facial(@RequestBody Map<String, List<String>> request, HttpServletResponse response) {
    	logger.info("FacialEmotionController : start facial");
		if(request.get("pictures") == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			JSONObject error = new JSONObject();
			error.put("message", "invalid request");
			return error.toString();
		}
		return new ProcessPicture().process(request.get("pictures"));
	}

}
