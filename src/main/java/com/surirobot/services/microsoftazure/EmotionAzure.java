package com.surirobot.services.microsoftazure;

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
import org.json.JSONArray;
import org.json.JSONException;

import com.surirobot.communication.Communication;
import com.surirobot.communication.ResponseHolder;
import com.surirobot.interfaces.services.ICommunication;
import com.surirobot.interfaces.services.IParser;
import com.surirobot.interfaces.services.IService;
import com.surirobot.utils.EnvVar;

/**
 * 
 * @author jussieu
 * 
 * Class à la quelle on donne une image et qui s'occupe d'appeler Azure pour
 * la reconnaissance des émotions .
 *
 */
public class EmotionAzure implements IService<JSONArray, String, byte[]>{

	private static final Logger logger = LogManager.getLogger();

	private static final String URL = "https://westus.api.cognitive.microsoft.com/emotion/v1.0/recognize";
	
   /**
    * Recuperer la réponse de Azure.
    */
	@Override
	public JSONArray getEmotions(String image) throws JSONException{
		logger.info("EmotionAzure : start getEmotions");

		String result = send(decoder(image));
		logger.info(result);
		IParser parser = new Parser();
		JSONArray json = (JSONArray) parser.parse(result);
		return json;
	}

	/**
	 *  Méthode qui nous convertit une image en base64 vers du binaire.
	 * @param base64Image le type de l'image en format {@link Base64}.
	 * @return le résulatat de la conversion en format {@link Byte}
	 */
	public static byte[] decoder(String base64Image) {
		return Base64.getDecoder().decode(base64Image);
	}

	/**
	 * Méthode qui s'occupe d'appeler Azure et récuperer le résultat.
	 */
	@Override
	public String send(byte[] image) {
		logger.info("EmotionAzure : start send");

		List<Header> headers = new ArrayList<>();
		headers.add(new BasicHeader("Content-Type","application/octet-stream"));
		logger.info("KEY : "+System.getenv(EnvVar.APIKEY.toString()));
		headers.add(new BasicHeader("Ocp-Apim-Subscription-Key",System.getenv(EnvVar.APIKEY.toString())));
		try {
			ResponseHolder responseHolder = Communication.doPost(URL, headers
					,new ByteArrayEntity(image, ContentType.APPLICATION_OCTET_STREAM));
			return responseHolder.content;
		}catch(IOException e) {
			e.printStackTrace();
		}
		return "{}";
	}

}
