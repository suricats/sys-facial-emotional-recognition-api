package com.surirobot.utils;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

public class ExecutorSingleton {
	
	private static CompletionService<JSONObject> completionService;
	private ExecutorSingleton() {}
	
	public static CompletionService<JSONObject> getInstance() {
		if(completionService == null) completionService =
				new ExecutorCompletionService<JSONObject>(Executors.newFixedThreadPool(10));
		return completionService;
	}

}
