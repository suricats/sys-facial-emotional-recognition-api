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

public final class RequestEmotion {

	/**
	 * Sends https post with {@link HttpEntity} param to server
	 *
	 * @param url
	 *            Requested url
	 * @param header
	 *            Data to post to server
	 * @return ResponseHolder the {@link ResponseHolder} holds response and
	 *         status code
	 */
	public static ResponseHolder doGet(String url, Header header) throws IOException
	{
		HttpClient client = createHttpClient();
		HttpGet get = new HttpGet(url);// POST to API
		ResponseHolder responseHolder =new ResponseHolder();

		if (header != null)
		{
			get.addHeader(header);
		}

		HttpResponse response = client.execute(get);

		responseHolder.responseCode = response.getStatusLine().getStatusCode();
		responseHolder.responseString = getStatusString(responseHolder.responseCode);
		InputStream content = response.getEntity().getContent();// Response
		responseHolder.content = readStream(content);
		return responseHolder;
	}

	public static ResponseHolder doPost(String url, List<Header> headers, HttpEntity entity) 
			throws IOException
	{
		HttpClient client = createHttpClient();
		HttpPost httpPost = new HttpPost(url);// POST to API
		ResponseHolder responseHolder =new ResponseHolder();

		if (headers != null && headers.size()>0)
		{
			for(Header header : headers) {
				httpPost.addHeader(header);
			}
		}


		if(entity!=null)
			httpPost.setEntity(entity);
		HttpResponse response = client.execute(httpPost);

		responseHolder.responseCode = response.getStatusLine().getStatusCode();
		responseHolder.responseString = getStatusString(responseHolder.responseCode);
		InputStream content = response.getEntity().getContent();// Response
		responseHolder.content = readStream(content);
		return responseHolder;
	}

	/**
	 * Convert the status code to user friendly {@link String}
	 *
	 * @param responseCode
	 * @return statusString
	 */
	private static String getStatusString(int responseCode)
	{
		return responseCode == HttpURLConnection.HTTP_OK ? "Success" : "Failed";
	}

	private static HttpClient createHttpClient()
	{
		HttpParams params = new BasicHttpParams();

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);

		SchemeRegistry schReg = new SchemeRegistry();
		//HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		//socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", socketFactory, 443));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);

		return new DefaultHttpClient(conMgr, params);
	}

	/**
	 * Convert from {@link InputStream} to {@link String}
	 *
	 * @param in
	 *            the {@link InputStream}
	 * @return {@link String}
	 */
	private static String readStream(InputStream in)
	{
		InputStreamReader is = new InputStreamReader(in);
		StringBuilder sb = new StringBuilder();
		try
		{
			BufferedReader br = new BufferedReader(is);
			String read = br.readLine();

			while (read != null)
			{
				sb.append(read);
				read = br.readLine();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}
}
