package com.surirobot.services.beyondverbal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
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
 * beyondverbal pour la reconnaissance des émotions. 
 *
 */
public class EmotionBeyond implements IService<JSONObject, String, byte[]>{
	private static final String RECORDING_URL = "https://apiv3.beyondverbal.com/v3/recording/";
	private static final String Auth_URL = "https://token.beyondverbal.com/token";
	
	private String recordingid ;
	private static Header access_token;

	private static final Logger logger = LogManager.getLogger();

	@Override
	public JSONObject getEmotions(String data) {
		logger.info("EmotionBeyond : start getEmotions");

		JSONObject json = new Parser().parse(send(decoder(data)));
		return json;
	}

	/**
	 * Méthode qui nous convertit une image en {@link Base64} vers {@link Byte}.
	 * @param base64Audio le format de l'audio.
	 * @return {@link Byte} le resultat de la converion.
	 */
	public static byte[] decoder(String base64Audio) {
		logger.info("EmotionBeyond : start decoder");
		return Base64.getDecoder().decode(base64Audio);
	}

	/**
	 * envoyer les donées pré-traité à l'API.
	 */
	@Override
	public String send(byte[] data) {
		logger.info("EmotionBeyond : start send");
		
		getToken();
		HttpEntity entity = getEntityForUpstream();
		List<Header> headers = new ArrayList<>();
		headers.add(access_token);
		try {
			ResponseHolder responseHolder = Communication.doPost(RECORDING_URL + "start", headers, entity);
			recordingid = getRecordingid(responseHolder.content);
			responseHolder =Communication.doPost(RECORDING_URL + recordingid, headers, getEntityForSendFile(data));
			return responseHolder.content;
		}catch(IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * traite la réponse de l'API.
	 * @param response la réponse de l'API.
	 * @return le résulatat du traitement.
	 */
	private String getRecordingid(String response) {
		logger.info("EmotionBeyond : start getRecondingid");
		
		if (response == null)
			return null;
		try {
			JSONObject json = new JSONObject(response);
			String recordingid = json.getString("recordingId");

			return recordingid;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Retourne l'entité d'un fichier.
	 * @param data les données du flux sous form {@link Byte}
	 * @return {@link HttpEntity}
	 */
	private HttpEntity getEntityForSendFile(byte[] data) {
		logger.info("EmotionBeyond : start getEntityForSendFile");
		ByteArrayInputStream raw = new ByteArrayInputStream(data);
		InputStreamEntity reqEntity = new InputStreamEntity(raw, -1);
		return reqEntity;
	}

	/**
	 * Retourne l'entité pour stream.
	 * @return {@link HttpEntity}
	 */
	private HttpEntity getEntityForUpstream() {
		logger.info("EmotionBeyond : start getEntityForUpstream");
		
		StringEntity se = null;
		try
		{
			se = new StringEntity(getConfigData());
			se.setContentType("application/json; charset=UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return se;
	}


	/**
	 * Récuperer le token à partir de l'API.
	 */
	private void getToken() {
		logger.info("EmotionBeyond : start getToken");
		if(access_token!=null)
			return;
		String jsonToken = null;
		try {
			jsonToken = Communication.doPost(Auth_URL, null, getEntityForAccessToken()).content;
		}catch(IOException e) {
			e.printStackTrace();
		}
		if (jsonToken == null)
			return ;
		JSONObject jsonObject;
		Header header = null;
		try {
			jsonObject = new JSONObject(jsonToken);
			header = new BasicHeader("Authorization",
					jsonObject.getString("token_type")+" "+jsonObject.getString("access_token"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		access_token = header;
	}

	/**
	 * Retourne l'entité à envoyer pour récuperer le token.
	 * 
	 * @return le corp de la requete sous format {@link HttpEntity}
	 */
	private HttpEntity getEntityForAccessToken() {
		logger.info("EmotionBeyond : start getEntityForAccessToken");
		
		String body = String.format("apikey=%s&grant_type=%s", System.getenv(EnvVar.API_BEYOND.toString()), "client_credentials");

		StringEntity se = null;
		try {
			se = new StringEntity(body);
			se.setContentType("Content-Type:application/x-www-form-urlencoded");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return se;

	}

	/**
	 *  Configurer un json input.
	 *  
	 * @return {@link String} coversion du json .
	 */
	private String getConfigData() {
		logger.info("EmotionBeyond : start getConfigData");
		try
		{
			// Instantiate a JSON Object and fill with Configuration Data
			// (Currently set to Auto Config)
			JSONObject inner_json = new JSONObject();
			inner_json.put("type", "WAV");
			inner_json.put("channels", 1);
			inner_json.put("sample_rate", 0);
			inner_json.put("bits_per_sample", 0);
			inner_json.put("auto_detect", true);
			JSONObject json = new JSONObject();
			json.put("data_format", inner_json);

			return json.toString();
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		return null;
	}


}
