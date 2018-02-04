package com.surirobot.process;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.surirobot.interfaces.IProcessPicture;
import com.surirobot.interfaces.IProcessVideo;
import com.surirobot.interfaces.IProcessVocal;

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
			IProcessPicture processP = new ProcessPicture();
			JSONArray facial = new JSONArray(processP.process(data.get("pictures")));
			json.put("facial",facial);
		}
		if(data.containsKey("record")) {
			IProcessVocal processV = new ProcessVocal();
			JSONObject vocal = new JSONObject(processV.process((String) data.get("record")));
			json.put("vocal",vocal);
		}

		return json.toString();
	}

}
