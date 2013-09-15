package com.asianux.service;

import java.io.IOException;
import java.nio.charset.Charset;

import com.asianux.utils.LrcInfo;
import com.asianux.utils.LrcParser;

import android.app.IntentService;
import android.content.Intent;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月10日 下午10:31:51 类说明
 */

public class LrcParserService extends IntentService {

	private final String MUSICLRCINFO = "Music.Player.LRC_INFO_BROADCAST";

	public LrcParserService() {
		// TODO Auto-generated constructor stub
		this("lrcParser");
	}

	public LrcParserService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

		String lrcPath = intent.getStringExtra("lrcPath");

		LrcParser lrcParser = new LrcParser();
		try {
			LrcInfo lrcInfo = lrcParser.parseLrcInfo(lrcPath,
					Charset.forName("gbk"));

			if (!lrcInfo.isEmpty()) {
				
				System.out.println("*************************************");

				Intent lrIntent = new Intent();
				lrIntent.putExtra("lrcInfo", lrcInfo);
				lrIntent.setAction(MUSICLRCINFO);
				sendBroadcast(lrIntent);
			}
			else
			{
				System.out.println("=====================================");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
