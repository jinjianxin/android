package com.asianux.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParserException;

import com.asianux.database.DbManager;
import com.asianux.utils.BaiduInfo;
import com.asianux.utils.BaiduParse;
import com.asianux.utils.FileUtils;
import com.asianux.utils.Mp3Info;

import android.app.IntentService;
import android.content.Intent;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月8日 下午4:24:25 类说明
 */

public class BaiduDownloadService extends IntentService {

	protected static final String SONGINFO_BASE_URL = "http://box.zhangmen.baidu.com/x";
	protected static final String LYRIC_BASE_URL = "http://box.zhangmen.baidu.com/bdlrc";
	private final String MUSICLRC = "Music.Player.LRC_BROADCAST";

	private int COLLCITION_TIMEOUT = 10000;
	private int READ_TIMEOUT = 15000;

	public BaiduDownloadService() {
		this("test");
	}

	public BaiduDownloadService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

		Mp3Info musciInfo = (Mp3Info) intent.getSerializableExtra("mp3info");

		FileUtils fileUtils = FileUtils.getInstance();	
		
		if (fileUtils.isFileExists(musciInfo.getTitle())) {
			
			//System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^1");
			String lrcPath = fileUtils.getLrcPath()+"/"+musciInfo.getTitle() +".lrc";
			musciInfo.setLrc(lrcPath);
			
			DbManager dbManager = DbManager
					.getInstance(BaiduDownloadService.this);
			dbManager.insert(musciInfo);

			Intent lrcIntent = new Intent();
			lrcIntent.putExtra("lrcPath", lrcPath);
			lrcIntent.setAction(MUSICLRC);
			sendBroadcast(lrcIntent);
			
		} else {
			//System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^2\t"+musciInfo.getTitle());
			if (musciInfo.getArtist().length() != 0
					&& musciInfo.getTitle().length() != 0) {

				try {
					String url = getLrcUrl(musciInfo.getTitle(),
							musciInfo.getArtist());
					String data = useHttpClient(url);

					List<BaiduInfo> list = BaiduParse.getBaidunfo(data);

					if (!list.isEmpty()) {

						String lrcUrl = getLrcUrlBySongInfo(list.get(0));

						if (lrcUrl !=null&&lrcUrl.length() != 0) {
							String lrcPath = useHttpClientLrc(lrcUrl, musciInfo);
							musciInfo.setLrc(lrcPath);

							DbManager dbManager = DbManager
									.getInstance(BaiduDownloadService.this);
							dbManager.insert(musciInfo);

							Intent lrcIntent = new Intent();
							lrcIntent.putExtra("lrcPath", lrcPath);
							lrcIntent.setAction(MUSICLRC);
							sendBroadcast(lrcIntent);

						}
					}

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	private String useHttpClient(String url) throws ClientProtocolException,
			IOException {
		String xmlData = null;

		BasicHttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				COLLCITION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParameters, READ_TIMEOUT);

		HttpGet httpRequest = new HttpGet(url);
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse httpResponse = httpclient.execute(httpRequest);

		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// 取得返回的字符串
			xmlData = EntityUtils.toString(httpResponse.getEntity());

			return xmlData;
		} else {
			return null;
		}
	}

	private String useHttpClientLrc(String url, Mp3Info mp3info)
			throws ClientProtocolException, IOException {
		String xmlData = null;

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

			String path = fileUtils.saveLrcFile(in, mp3info.getTitle());

			return path;
		} else {
			return null;
		}
	}

	private String getLrcUrl(String title, String artist)
			throws UnsupportedEncodingException {

		StringBuffer sb = new StringBuffer();

		sb.append(SONGINFO_BASE_URL);
		sb.append("?");
		sb.append("op=12");
		sb.append("&");
		sb.append("count=1");
		sb.append("&");
		sb.append("title=");
		sb.append(URLEncoder.encode(title, "utf-8"));
		sb.append("$$");
		sb.append(URLEncoder.encode(artist, "utf-8"));
		sb.append("$$$$");

		return sb.toString();
	}

	private String getLrcUrlBySongInfo(BaiduInfo info) {
		
		if (info.getLrcid() != null) {
			
			int lrcid = Integer.parseInt(info.getLrcid());
			int postfix = lrcid / 100;

			StringBuffer sb = new StringBuffer();
			sb.append(LYRIC_BASE_URL);
			sb.append("/");
			sb.append(postfix);
			sb.append("/");
			sb.append(lrcid);
			sb.append(".lrc");

			return sb.toString();
		} else {
			return null;
		}
	}
}
