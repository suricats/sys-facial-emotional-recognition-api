package com.surirobot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.surirobot.utils.EnvVar;
/**
 * 
 * @author jussieu
 * Class permetant de lancer le server.
 *
 */
@SpringBootApplication
public class Application {
	private static final Logger logger = LogManager.getFormatterLogger();
	
	/**
	 * Lancement du server.
	 * @param args les arguments necessaires pour le lancement du server,
	 * 
	 */
	public static void main(String[] args) {
		logger.info("Application : start main");
		checkEnvVar();
		SpringApplication.run(Application.class, args);
	}
	
	/**
	 * Verficication de l'environement.
	 */
    private static void checkEnvVar(){
    	logger.info("Application : start checkEnvVar");
    	for(EnvVar var : EnvVar.values()){
    		logger.info(var + " : "+ var.getValue());
    		if(var.getValue()==null){
    			logger.error("Check your environment var "+var.toString()+"... ");
    			System.exit(0);
    		}
    	}
    }

}
