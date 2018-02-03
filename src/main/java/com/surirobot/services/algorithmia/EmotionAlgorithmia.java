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

import com.surirobot.communication.Communication;
import com.surirobot.communication.ResponseHolder;
import com.surirobot.interfaces.services.IService;
import com.surirobot.utils.EnvVar;

/**
 * 
 * @author jussieu
 * 
 * Class à la quelle on donne une image et qui s'occupe d'appeler l'API
 * Algorithmia pour la reconnaissance des émotions. 
 *
 */
public class EmotionAlgorithmia implements IService<JSONObject, String, byte[]>{

	private static final Logger logger = LogManager.getLogger();
	
	private static final String URL = "https://api.algorithmia.com/v1/algo/deeplearning/EmotionRecognitionCNNMBP/1.0.1";

	/**
	 * Recuperer la réponse de l'API Algorithmia.
	 */
	@Override
	public JSONObject getEmotions(String image) {
		logger.info("EmotionAlgorithmia : start getEmotions");
		String result = send(decoder(image));
		logger.info(result);
		JSONObject json = new Parser().parse(result);
		return json;
	}

	/**
	 * Méthode qui nous convertit une image en {@link Base64} vers {@link Byte}.
	 * 
	 * @param base64Image l'image sous format {@link Base64}
	 * @return le résulatat de conversion.
	 */
	public static byte[] decoder(String base64Image) {
		return Base64.getDecoder().decode(base64Image);
	}

	/**
	 * Méthode qui s'occupe d'appeler Algorithmia et récuperer le résultat.
	 */
	@Override
	public String send(byte[] image) {
		logger.info("EmotionAlgorithmia : start send");

		List<Header> headers = new ArrayList<>();
		headers.add(new BasicHeader("Content-Type","application/octet-stream"));
		logger.info("KEY : "+System.getenv(EnvVar.API_ALGORITHMIA.toString()));
		headers.add(new BasicHeader("Authorization","Simple "+System.getenv(EnvVar.API_ALGORITHMIA.toString())));
		try {
			ResponseHolder responseHolder = Communication.doPost(URL, headers
					,new ByteArrayEntity(image, ContentType.APPLICATION_OCTET_STREAM));
			return responseHolder.content;
		}catch(IOException e) {
			return "{}";
		}
	}

}
