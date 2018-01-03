package com.surirobot.process;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.surirobot.interfaces.IProcessVocal;
import com.surirobot.services.vokaturi.EmotionVokaturi;

public class ProcessVocal implements IProcessVocal {

	private static final Logger logger = LogManager.getLogger();

	/*
	 * (non-Javadoc)
	 * @see com.surirobot.interfaces.IProcess#process(java.lang.Object)
	 * 
	 * Méthode qui traite le fichier vocal reçu.
	 * 
	 */
	
	@Override
	public String process(String data) {
		logger.info("ProcessVocal : start process");
		
		if(data == null) return "{}";
		if(data.equals("")) return "{}";
		
		return new EmotionVokaturi().getEmotions(data).toString();
		
	}

}
