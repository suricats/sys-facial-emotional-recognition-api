package com.surirobot.interfaces.services;

import org.json.JSONObject;

import com.surirobot.interfaces.IProcess;
/**
 * 
 * @author jussieu
 *
 * Parser les donées passées en parametre.
 */
public interface IParser<T> {
	/**
	 * 
	 * @param content les donées reçu de l'API interrogée.
	 * @return {@link JSONObject} d'emotions.
	 */
	public T parse(String content);
}
