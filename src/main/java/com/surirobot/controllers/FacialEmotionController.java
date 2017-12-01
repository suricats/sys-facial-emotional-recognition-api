package com.surirobot.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surirobot.process.ProcessPicture;

@Controller
public class FacialEmotionController {

	@PostMapping(value = "/emotions/actions/retrieve-facial-emotion")
	@ResponseBody
	public String detect(@RequestBody Map<String, List<String>> request, HttpServletResponse response) {
		if(request.get("pictures") == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			JSONObject error = new JSONObject();
			error.put("message", "invalid request");
			return error.toString();
		}
		return new ProcessPicture().process(request.get("pictures"));
	}

}
