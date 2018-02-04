package com.surirobot.interfaces;
/**
 * 
 * @author jussieu
 *
 * @param <T> le type des donnée reçues du client.
 */
public interface IProcess<T> {
	public String process(T data);
}
