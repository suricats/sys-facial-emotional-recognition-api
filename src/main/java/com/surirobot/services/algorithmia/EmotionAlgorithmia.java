package com.surirobot.services.algorithmia;

import java.io.IOException;
import java.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.surirobot.services.interfaces.IService;
import com.surirobot.utils.EnvVar;

/*
 * Class à la quelle on donne une image et qui s'occupe d'appeler l'API
 * Algorithmia pour la reconnaissance des émotions 
 */
public class EmotionAlgorithmia implements IService<String, byte[]>{

private static final Logger logger = LogManager.getLogger();
	
	/*
	 * (non-Javadoc)
	 * @see com.surirobot.services.interfaces.IService#getEmotions(java.lang.Object)
	 * Recuperer la réponse de Algorithmia
	 */
	@Override
	public JSONObject getEmotions(String image) {
		logger.info("EmotionAlgorithmia : start getEmotions");
		JSONObject json = new Parser().parse(send(decoder(image)));
		return json;
	}
	
	/*
	 * Méthode qui nous convertit une image en base64 vers du binaire
	 */
	public static byte[] decoder(String base64Image) {
		return Base64.getDecoder().decode(base64Image);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.surirobot.services.interfaces.IService#send(java.lang.Object)
	 * Méthode qui s'occupe d'appeler Algorithmia et récuperer le résultat
	 */
	@Override
	public HttpResponse send(byte[] image) {
		logger.info("EmotionAlgorithmia : start send");
		
		HttpClient client = new DefaultHttpClient();  
		HttpPost request = new HttpPost("https://api.algorithmia.com/v1/algo/deeplearning/EmotionRecognitionCNNMBP/1.0.1");
		request.setHeader("Content-Type", "application/octet-stream");
		request.setHeader("Authorization", "Simple "+System.getenv(EnvVar.API_ALGORITHMIA.toString()));
		request.setEntity(new ByteArrayEntity(image, ContentType.APPLICATION_OCTET_STREAM));
		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			logger.error("Problème de protocole...\n"+e.getStackTrace());
		} catch (IOException e) {
			logger.error("Problème d'entrée sortie...\n"+e.getStackTrace());
		}
		return response;
	}

}
