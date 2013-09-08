package com.asianux.service;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月8日 上午9:56:08
 * 类说明
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import com.asianux.utils.FileUtils;
import com.asianux.utils.GecimiInfo;
import com.asianux.utils.GecimiParse;
import com.asianux.utils.Mp3Info;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class GecimiDownloadService extends IntentService {

	private int COLLCITION_TIMEOUT = 10000;
	private int READ_TIMEOUT = 15000;
	private static String SONGHEAD = "http://geci.me/api/lyric/";
	private static String SINGERHEAD = "http://geci.me/api/lyric/";

	public GecimiDownloadService() {
		// TODO Auto-generated constructor stub
		this("hhx");
	}

	public GecimiDownloadService(String name) {
		super("test");
		// TODO Auto-generated constructor stub
	}

	private MyBinder myBinder = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return myBinder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		myBinder = new MyBinder();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

		Mp3Info musciInfo = (Mp3Info) intent.getSerializableExtra("mp3info");

		try {
			String songjson = null;
			
			
			System.out.println("===========================================");
			
			songjson = useHttpClient(musciInfo.getTitle());
			
			if (songjson != null) {
				GecimiInfo info = GecimiParse.getGecimiInfo(songjson);
				String lrcData = useHttpClientLrc(info, musciInfo);
			}
			
			System.out.println("songname = \t" + songjson);
			
			/*
			if(musciInfo.getArtist() == null || musciInfo.getArtist().length() ==0)
			{
			
				songjson = useHttpClient(musciInfo.getTitle());
				
				if (songjson != null) {
					GecimiInfo info = GecimiParse.getGecimiInfo(songjson);
					String lrcData = useHttpClientLrc(info, musciInfo);
				}
				System.out.println("songname = \t" + songjson);
			}
			else
			{
				songjson = useHttpClient(musciInfo.getArtist(), musciInfo.getTitle());
				System.out.println("singername = \t" + songjson);
			}
			
			*/
			
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String useHttpURLConnection() {
		String jsonData = null;
		String path = "表白";

		try {
			URL url = new URL(SONGHEAD + URLEncoder.encode(path, "UTF-8"));

			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			urlConnection.setConnectTimeout(COLLCITION_TIMEOUT);
			urlConnection.setReadTimeout(READ_TIMEOUT);

			InputStreamReader reader = new InputStreamReader(
					urlConnection.getInputStream());

			BufferedReader bufferReader = new BufferedReader(reader);

			String inputLine = null;

			while ((inputLine = bufferReader.readLine()) != null) {
				// System.out.println("inputLine = \t"+inputLine);
				jsonData += inputLine;
			}

			reader.close();

			urlConnection.disconnect();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonData;
	}

	/*
	 * useHttpClient 根据歌曲名下载数据 (这里描述这个方法适用条件 – 可选)
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOExceptionString
	 * @exception
	 * @since 1.0.0
	 */
	private String useHttpClient(String title) throws ClientProtocolException,IOException {
		String jsonData = null;

		String url = null;
		url = SONGHEAD + URLEncoder.encode(title, "UTF-8");
		
		System.out.println(url);

		BasicHttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				COLLCITION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParameters, READ_TIMEOUT);

		HttpGet httpRequest = new HttpGet(url);
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse httpResponse = httpclient.execute(httpRequest);

		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

			jsonData = EntityUtils.toString(httpResponse.getEntity());
			return jsonData;
		} else {
			return null;
		}

	}
	
	private String useHttpClient(String artist,String title) throws ClientProtocolException,IOException {
		
		String jsonData = null;

		String url = null;
		url = SINGERHEAD + URLEncoder.encode(title, "UTF-8")+"/"+URLEncoder.encode(artist, "UTF-8");
		
		System.out.println(url);
		

		BasicHttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				COLLCITION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParameters, READ_TIMEOUT);

		HttpGet httpRequest = new HttpGet(url);
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse httpResponse = httpclient.execute(httpRequest);

		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

			jsonData = EntityUtils.toString(httpResponse.getEntity());
			
			System.out.println(jsonData);
			
			return jsonData;
		} else {
			return null;
		}
		
	}


	private String useHttpClientLrc(GecimiInfo info, Mp3Info musicInfo)
			throws ClientProtocolException, IOException {
		String jsonData = null;

		String url = info.getLrc();

		BasicHttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				COLLCITION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParameters, READ_TIMEOUT);

		HttpGet httpRequest = new HttpGet(url);
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse httpResponse = httpclient.execute(httpRequest);

		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			InputStream in = httpResponse.getEntity().getContent();

			FileUtils fileUtils = FileUtils.getInstance();
			String path = fileUtils.saveLrcFile(in, musicInfo.getTitle()
					+ ".lrc");
			return path;
		} else {
			return null;
		}

	}

	public class MyBinder extends Binder {

	}

}
