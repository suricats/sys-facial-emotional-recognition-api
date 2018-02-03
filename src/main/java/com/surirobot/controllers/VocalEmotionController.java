package com.surirobot.controllers;

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
import com.surirobot.process.ProcessVocal;

/**
 * Ce controller reçoit une requete du client qui contient un flux vocal 
 * ensuite ,il delegue le travail à <code>{@link ProcessVocal }</code> , après 
 * le traitemnt de celle ci ,le controller retourne l'emotion corréspondante .
 * 
 * 
 */
@Controller
public class VocalEmotionController {
	private static final Logger logger = LogManager.getFormatterLogger();

	@PostMapping(value = "/emotions/actions/retrieve-vocal-emotion")
	@ResponseBody
	public String vocal(@RequestBody Map<String, String> request, HttpServletResponse response) {
    	logger.info("VocalEmotionController : start vocal");
		if(request.get("record") == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			JSONObject error = new JSONObject();
			error.put("message", "invalid request");
			return error.toString();
		}
		return new ProcessVocal().process(request.get("record"));
	}
}
