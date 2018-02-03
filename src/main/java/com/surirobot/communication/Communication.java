package com.surirobot.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.surirobot.interfaces.services.ICommunication;
/**
 * 
 * @author jussieu
 * 
 * Cette classe permet de communiquer notre module avec les API's externes
 * à fin de pouvoir récupéré le résultat souhaité.
 *
 */
public final class Communication implements ICommunication {
	private static final Logger logger = LogManager.getFormatterLogger();
	
	/**
	 * 
	 * @param url l'url de L'API  intérrogée.
	 * @param header le header de la requete
	 * @return la réponse obtenue de l'API.
	 * @throws IOException l'exception lancée en cas d'erreur.
	 */

	public static ResponseHolder doGet(String url, Header header) throws IOException {
		logger.info("Communication : start doGet");
		HttpClient client = createHttpClient();
		HttpGet get = new HttpGet(url);// POST to API
		ResponseHolder responseHolder = new ResponseHolder();

		if (header != null) {
			get.addHeader(header);
		}

		HttpResponse response = client.execute(get);

		responseHolder.responseCode = response.getStatusLine().getStatusCode();
		responseHolder.responseString = getStatusString(responseHolder.responseCode);
		InputStream content = response.getEntity().getContent();// Response
		responseHolder.content = readStream(content);
		return responseHolder;
	}
	
   /** 
    * 
    * @param url l'url de L'API  intérrogée.
	* @param headers la liste des headers de la requete .
    * @param entity le corp de la requete.
    * @return la réponse obtenue de L'API.
    * @throws IOException l'exception lancée en cas d'erreur.
    */
	public static ResponseHolder doPost(String url, List<Header> headers, HttpEntity entity) throws IOException {
		logger.info("Communication : start doPost");
		HttpClient client = createHttpClient();
		HttpPost httpPost = new HttpPost(url);// POST to API
		ResponseHolder responseHolder = new ResponseHolder();

		if (headers != null && headers.size() > 0) {
			for (Header header : headers) {
				httpPost.addHeader(header);
			}
		}

		if (entity != null)
			httpPost.setEntity(entity);
		HttpResponse response = client.execute(httpPost);

		responseHolder.responseCode = response.getStatusLine().getStatusCode();
		responseHolder.responseString = getStatusString(responseHolder.responseCode);
		InputStream content = response.getEntity().getContent();// Response
		responseHolder.content = readStream(content);
		return responseHolder;
	}

	/**
	 *
	 * @param responseCode la code de la réponse obtenue.
	 * @return statusString le résultat de la conversion du code obtenu.
	 */
	private static String getStatusString(int responseCode) {
		logger.info("Communication : start getStatusString");
		return responseCode == HttpURLConnection.HTTP_OK ? "Success" : "Failed";
	}
	/**
	 * 
	 * @return un objet de type {@link HttpClient}.
	 */
	private static HttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);

		SchemeRegistry schReg = new SchemeRegistry();
		// HostnameVerifier hostnameVerifier =
		// org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		// socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", socketFactory, 443));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);

		return new DefaultHttpClient(conMgr, params);
	}

	/** 
	 * Convertir {@link InputStream} en {@link String}.
	 * 
	 * @param in donée sous format {@link InputStream}.
	 * @return la conversion de {@link InputStream} en {@link String}.
	 */
	private static String readStream(InputStream in) {
		logger.info("Communication : start readStream");
		InputStreamReader is = new InputStreamReader(in);
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(is);
			String read = br.readLine();

			while (read != null) {
				sb.append(read);
				read = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
