package com.surirobot.services.algorithmia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.surirobot.communication.RequestEmotion;
import com.surirobot.communication.ResponseHolder;
import com.surirobot.services.interfaces.IService;
import com.surirobot.utils.EnvVar;

/*
 * Class à la quelle on donne une image et qui s'occupe d'appeler l'API
 * Algorithmia pour la reconnaissance des émotions 
 */
public class EmotionAlgorithmia implements IService<String, byte[]>{

	private static final Logger logger = LogManager.getLogger();
	
	private static final String URL = "https://api.algorithmia.com/v1/algo/deeplearning/EmotionRecognitionCNNMBP/1.0.1";

	/*
	 * (non-Javadoc)
	 * @see com.surirobot.services.interfaces.IService#getEmotions(java.lang.Object)
	 * Recuperer la réponse de Algorithmia
	 */
	@Override
	public JSONObject getEmotions(String image) {
		logger.info("EmotionAlgorithmia : start getEmotions");
		String result = send(decoder(image));
		logger.info(result);
		JSONObject json = new Parser().parse(result);
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
	public String send(byte[] image) {
		logger.info("EmotionAlgorithmia : start send");

		List<Header> headers = new ArrayList<>();
		headers.add(new BasicHeader("Content-Type","application/octet-stream"));
		logger.info("KEY : "+System.getenv(EnvVar.API_ALGORITHMIA.toString()));
		headers.add(new BasicHeader("Authorization","Simple "+System.getenv(EnvVar.API_ALGORITHMIA.toString())));
		try {
			ResponseHolder responseHolder = RequestEmotion.doPost(URL, headers
					,new ByteArrayEntity(image, ContentType.APPLICATION_OCTET_STREAM));
			return responseHolder.content;
		}catch(IOException e) {
			return "{}";
		}
	}

}
