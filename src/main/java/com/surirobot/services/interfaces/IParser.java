package com.surirobot.services.interfaces;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

public interface IParser {
	public JSONObject parse(HttpResponse response);
}
