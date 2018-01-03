package com.surirobot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.surirobot.utils.EnvVar;

@SpringBootApplication
public class Application {
	private static final Logger logger = LogManager.getFormatterLogger();
	
	public static void main(String[] args) {
		checkEnvVar();
		SpringApplication.run(Application.class, args);
	}
	
    private static void checkEnvVar(){
    	for(EnvVar var : EnvVar.values()){
    		logger.info(var + " : "+ var.getValue());
    		if(var.getValue()==null){
    			logger.error("Check your environment var "+var.toString()+"... ");
    			System.exit(0);
    		}
    	}
    }

}
