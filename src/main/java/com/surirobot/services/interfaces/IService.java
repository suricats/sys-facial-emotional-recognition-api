package com.surirobot.services.interfaces;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

public interface IService<T,K> {
	public JSONObject getEmotions(T data);
	public HttpResponse send(K data);
}
