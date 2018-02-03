package com.surirobot.utils;

/**
 * 
 * @author jussieu
 *
 * Enumération pour les variables d'environnement.
 */
public enum EnvVar {

	APIKEY,
	API_ALGORITHMIA,
	API_BEYOND;

	private String value;

	EnvVar(){
		try{
			value = System.getenv(toString());
		} catch (Exception e){
			value = null;
		}
	}

	public String getValue(){ return value; }
}
