package com.surirobot.process;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.surirobot.interfaces.IProcessVocal;
import com.surirobot.services.vokaturi.EmotionVokaturi;
/**
 * 
 * @author jussieu
 * 
 * Permet de traiter le flux audio reçu.
 *
 */
public class ProcessVocal implements IProcessVocal {

	private static final Logger logger = LogManager.getLogger();

	/**
	 * Méthode qui traite le fichier vocal reçu.
	 * si le flux == {@link <code>null</code>} ou vide: la méthode retourne un json vide.
	 * sinon : elle passe les donnée à l'API.
	 */
	
	@Override
	public String process(String data) {
		logger.info("ProcessVocal : start process");
		
		if(data == null) return "{}";
		if(data.equals("")) return "{}";
		
		return new EmotionVokaturi().getEmotions(data).toString();
		
	}

}
