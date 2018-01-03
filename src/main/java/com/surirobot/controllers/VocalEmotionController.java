package com.surirobot.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surirobot.process.ProcessVocal;

@Controller
public class VocalEmotionController {

	@PostMapping(value = "/emotions/actions/retrieve-vocal-emotion")
	@ResponseBody
	public String vocal(@RequestBody Map<String, String> request, HttpServletResponse response) {
		if(request.get("record") == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			JSONObject error = new JSONObject();
			error.put("message", "invalid request");
			return error.toString();
		}
		return new ProcessVocal().process(request.get("record"));
	}
}
