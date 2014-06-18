package com.joymetec.push;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.xiaomi.d.m;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

public class MyReceiver extends BroadcastReceiver {

	private String mMessage = null;
	private String CLICK_ACTION = "com.joymetec.click";
	private String SHOW_NOTIFICATION = "com.joymetec.shownotify";
	private int notifyId = 0;
	private int timeOut = 60000;
	private Timer timer = null;
	private String[] key = { "url", "name", "package", "version", "tip",
			"title" };

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(CLICK_ACTION)) {
			String str = intent.getStringExtra("click");
/*			String path = intent.getStringExtra("path");
			Log.d(JoymetecApplication.TAG, "click str = " + str);
			Log.d(JoymetecApplication.TAG, "click path = " + path);
			
			Bundle bundle = intent.getExtras();
			Log.d(JoymetecApplication.TAG, "click str = " + bundle.getString("click"));
			Log.d(JoymetecApplication.TAG, "click path = " + bundle.getString("path"));*/
	
			if (str != null && str.length() > 0) {
				clickNotification(context, str);
			}
		} else if (intent.getAction().equals(SHOW_NOTIFICATION)) {
			mMessage = intent.getStringExtra("arg");
			Log.d(JoymetecApplication.TAG, "mMessage = " + mMessage);

			if (mMessage != null && mMessage.length() > 0) {
				HashMap<String, String> map = parsingArgument(mMessage);
				if (map != null) {
					
					DataInstance dataInstance = DataInstance.getInstance();
					dataInstance.addDownling(map.get(key[2]));
					
					showNotification(context, map.get(key[4]), map.get(key[5]),
							mMessage);
				}
			}
		}

	}

	public void showNotification(Context context, String tip, String title,
			String arg) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);

		CharSequence cs;

		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		Notification noti = new Notification(icon, "", when);
		noti.flags = Notification.FLAG_NO_CLEAR;

		RemoteViews remoteView = new RemoteViews(context.getPackageName(),
				R.layout.notification);
		remoteView.setTextViewText(R.id.time, str);
		remoteView.setTextViewText(R.id.tip, tip);
		remoteView.setTextViewText(R.id.title, title);
		noti.contentView = remoteView;

		Intent buttonPlayIntent = new Intent(CLICK_ACTION); // ----设置通知栏按钮广播
		
		
		HashMap<String, String> map = parsingArgument(mMessage);
		if (map != null) {
			buttonPlayIntent.putExtra("click", arg);
			PendingIntent pendButtonPlayIntent = PendingIntent.getBroadcast(
					context, 0, buttonPlayIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			remoteView.setOnClickPendingIntent(R.id.layout, pendButtonPlayIntent);// ----设置对应的按钮ID监控
		}
		
		notifyId = 2;
		NotificationManager mnotiManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mnotiManager.notify(notifyId, noti);
	}

	public void clickNotification(Context context, String str) {
		
		NotificationManager mnotiManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mnotiManager.cancel(2);
		notifyId = 0;

		HashMap<String, String> map = parsingArgument(str);
		
		if(map!=null)
		{
			DataInstance dataInstance = DataInstance.getInstance();
			if(!dataInstance.isDownloading(map.get(key[2])))
			{
				if (Network.hasNetwork(context)) {
					Intent intent = new Intent();
					intent.putExtra("url", str);
					intent.setClass(context, DownLoadService.class);
					context.startService(intent);
					Log.d(JoymetecApplication.TAG,"************* start service");
				}
				
			}
			else
			{
				Log.d(JoymetecApplication.TAG, "************* noting");
			}
		}
		
		//dataInstance.addDownling(map.get(key[1]));
		
		
		/*if(Network.hasNetwork(context))
    	{
			Intent intent = new Intent();
			intent.putExtra("url", str);
			intent.setClass(context, DownLoadService.class);
			context.startService(intent);
    	}*/
/*
		if (str.length() > 0) {
			Log.d(JoymetecApplication.TAG, "msg = " + str);
			if (Network.hasNetwork(context)) {
				boolean wifiState = Network.isWIFIConnected(context);

				if (!wifiState) {
					if (isExists(map.get(key[1]))) {
						int codeVersion = getVersionCode(context,
								map.get(key[2]));

						if (codeVersion != -1) {

							int code = Integer.parseInt(map.get(key[3]));

							final String downloadPath = Environment
									.getExternalStorageDirectory().getPath()
									+ "/download"
									+ "/"
									+ map.get(key[1])
									+ ".apk";

							if (code > codeVersion) {
								timer = new Timer();
								timer.schedule(new TimerTask() {
									@Override
									public void run() {
										// TODO Auto-generated method stub

										Message m = new Message();
										m.arg1 = 2;
										Bundle bundle = new Bundle();
										bundle.putString("path", downloadPath);
										m.setData(bundle);
										MyReceiver.this.myHandler
												.sendMessage(m);
									}
								}, timeOut);

								new MyThread(downloadPath).start();
							}

						} else {
							final String downloadPath = Environment
									.getExternalStorageDirectory().getPath()
									+ "/download"
									+ "/"
									+ map.get(key[1])
									+ ".apk";
							timer = new Timer();
							timer.schedule(new TimerTask() {
								@Override
								public void run() {
									// TODO Auto-generated method stub

									Message m = new Message();
									m.arg1 = 2;
									Bundle bundle = new Bundle();
									bundle.putString("path", downloadPath);
									m.setData(bundle);
									MyReceiver.this.myHandler.sendMessage(m);
								}
							}, timeOut);

							new MyThread(downloadPath).start();
						}

					} else {
						Uri uri = Uri.parse(map.get(key[0]));
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					}
				}
				else
				{
					
				}

			}
		} */

	}

	public HashMap<String, String> parsingArgument(String message) {
		HashMap<String, String> map = new HashMap<String, String>();
		String str = message;

		String[] info = str.split(";", 6);

		//Log.d(JoymetecApplication.TAG, "size = " + info.length);

		if (info.length != 6)
			return null;

		for (int i = 0; i < info.length; i++) {
			map.put(key[i], info[i]);
			// Log.d(JoymetecApplication.TAG, "key =" + key[i] + "  " +
			// info[i]);
		}
		return map;
	}

	public boolean isExists(String name) {
		String downloadPath = Environment.getExternalStorageDirectory()
				.getPath() + "/download" + "/" + name + ".apk";
		File file = new File(downloadPath);
		return file.exists();
	}

	private int getVersionCode(Context context, String pkName) {
		try {

			String versionName = context.getPackageManager().getPackageInfo(
					pkName, 0).versionName;
			int versionCode = context.getPackageManager().getPackageInfo(
					pkName, 0).versionCode;

			return versionCode;
		} catch (Exception e) {
		}
		return -1;

	}

	public void pmInstall(String path) {
		final String filePath = path;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				Message m = new Message();
				m.arg1 = 2;
				Bundle bundle = new Bundle();
				bundle.putString("path", filePath);
				m.setData(bundle);
				MyReceiver.this.myHandler.sendMessage(m);
			}
		}, timeOut);

		new MyThread(path).start();
	}

	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {

			int id = msg.arg1;

			Log.d(JoymetecApplication.TAG, "handleMessage");

			if (id == 1) // 成功安装
			{
				Log.d(JoymetecApplication.TAG, "install success");
				if (timer != null) {
					timer.cancel();
				}
			} else if (id == 2) // 请求root失败
			{
				Log.d(JoymetecApplication.TAG, "time out");

				Bundle bundle = msg.getData();
				String path = bundle.getString("path");

				if (path != null && path.length() > 0) {
					// packageInstall(getApplicationContext(), path);
				}

				timer.cancel();
			}

		};
	};

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
				Log.d(JoymetecApplication.TAG, "error1");
				process = Runtime.getRuntime().exec("su");
				out = process.getOutputStream();

				out.write(("pm install -r " + path + "\n").getBytes());
				in = process.getInputStream();
				int len = 0;
				byte[] bs = new byte[256];

				while (-1 != (len = in.read(bs))) {
					String state = new String(bs, 0, len);
					Log.d(JoymetecApplication.TAG, "state = " + state);
					if (state.equals("Success\n")) {

						Message m = new Message();
						m.arg1 = 1;
						MyReceiver.this.myHandler.sendMessage(m);

					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
	}
}
