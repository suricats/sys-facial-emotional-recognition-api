package com.surirobot.utils;

/*
 * Enumération pour les variables d'environnement 
 */
public enum EnvVar {

	APIKEY;

	private String value;

	EnvVar(){
		try{
			System.out.println(System.getenv(toString()));
			value = System.getenv(toString());
		} catch (Exception e){
			value = null;
		}
	}

	public String getValue(){ return value; }
}
