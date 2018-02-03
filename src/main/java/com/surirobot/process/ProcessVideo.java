package com.surirobot.process;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.surirobot.interfaces.IProcessVideo;

/**
 * 
 * @author jussieu
 * 
 * Class permettant de traiter la liste des images reçues en Base64.
 */

public class ProcessVideo implements IProcessVideo{

	private static final Logger logger = LogManager.getLogger();
	
	/**
	 * elle permet de traiter le flux image/vidéo.
	 */
	public String process(HashMap<String, String> data) {
    	logger.info("ProcessVideo : start process");
		JSONObject json = new JSONObject();
		if(data.containsKey("pictures")) {
			JSONArray facial = new JSONArray(new ProcessPicture2().process(data.get("pictures")));
			json.put("facial",facial);
		}
		if(data.containsKey("record")) {
			JSONObject vocal = new JSONObject(new ProcessVocal().process((String) data.get("record")));
			json.put("vocal",vocal);
		}

		return json.toString();
	}

}
