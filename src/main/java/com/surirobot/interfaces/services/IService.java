package com.surirobot.interfaces.services;

import org.json.JSONObject;
/**
 * 
 * @author jussieu
 *
 * @param <T> les émotions obtenues de L'API.
 * @param <K>  le type du flux reçu image/vocal
 * @param <V>  le format du flux envoyé à l'API corepondante.
 */
public interface IService<T,K,V> {
	public T getEmotions(K data);
	public String send(V data);
}
