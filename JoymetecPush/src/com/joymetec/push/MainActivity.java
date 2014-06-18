package com.joymetec.push;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RemoteViews;

import com.xiaomi.xmpush.server.*;



public class MainActivity extends Activity {

	private static String MY_PACKAGE_NAME = "com.joymetec.push";
	private String CLICK_ACTION = "com.joymetec.click";
	private String SHOW_NOTIFICATION = "com.joymetec.shownotify";
	private String[] key = { "url", "name", "package", "version", "tip",
	"title" };
	private Button button = null;
	private Button button2 = null;
	private String TAG = "lemi";
	//private MyReceiver receiver = null;
	private int notiId = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Constants.useOfficial();
		
		button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
		/*		String info = getAppInfo();
				if(info !=null)
				{
					Log.d(TAG, getAppInfo());
				}
				else
				{
					Log.d(TAG, "no install");
				}*/
				
				//showNotification(getApplicationContext());
				
				/*Uri uri = Uri.parse("http://www.baidu.com");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);*/
				
				//String mMessage = "http://gdown.baidu.com/data/wisegame/4ed0b298f5330322/koudairenzhe_1000000.apk;test6;com.joymetec.mnsjdz.sky1;3;test;test";
				String mMessage = "http://down.apk.hiapk.com/down?aid=2735742&em=13;test6;com.joymetec.mnsjdz.sky1;3;test;test";
			    HashMap<String, String> map = parsingArgument(mMessage);
		        if(map!=null)
		        {
		        	//showNotification(context,map.get(key[4]),map.get(key[5]));
		        	Intent intent = new Intent();
		        	intent.setAction(SHOW_NOTIFICATION);
		        	intent.putExtra("arg", mMessage);
		        	sendBroadcast(intent);
		        }
				
				
				Intent intent = new Intent();
    			intent.putExtra("url", mMessage);
    			intent.setClass(MainActivity.this, DownLoadService.class);
    			startService(intent);
				
			}
		});
		
		
		button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String mMessage = "http://gdown.baidu.com/data/wisegame/4ed0b298f5330322/koudairenzhe_1000000.apk;test7;com.joymetec.mnsjdz.sky1;3;test;test";

				HashMap<String, String> map = parsingArgument(mMessage);
				if (map != null) {
					// showNotification(context,map.get(key[4]),map.get(key[5]));
					Intent intent = new Intent();
					intent.setAction(SHOW_NOTIFICATION);
					intent.putExtra("arg", mMessage);
					sendBroadcast(intent);
				}

				Intent intent = new Intent();
				intent.putExtra("url", mMessage);
				intent.setClass(MainActivity.this, DownLoadService.class);
				startService(intent);
			}
		});

	}

	private String getAppInfo() {
 		try {
 			//String pkName = this.getPackageName();
 			String pkName = "com.test"; 
 			
 			String versionName = this.getPackageManager().getPackageInfo(
 					pkName, 0).versionName;
 			int versionCode = this.getPackageManager()
 					.getPackageInfo(pkName, 0).versionCode;
 			return pkName + "   " + versionName + "  " + versionCode;
 		} catch (Exception e) {
 		}
 		return null;
 	}
	
    public void showNotification(Context context)
    {

    	SimpleDateFormat formatter =new SimpleDateFormat("HH:mm");       
    	Date curDate=new Date(System.currentTimeMillis());//获取当前时间       
    	String str = formatter.format(curDate);      
    	
    	CharSequence cs;
    
    	
    	 CharSequence title = "";  
         int icon = R.drawable.mipush_notification_small;  
         long when = System.currentTimeMillis();  
         Notification noti = new Notification(icon, title, when);  
         noti.flags = Notification.FLAG_NO_CLEAR;
         
         RemoteViews remoteView = new RemoteViews(context.getPackageName(),R.layout.notification);  
         remoteView.setTextViewText(R.id.time, str);
         noti.contentView = remoteView;  
        
         
         
         Intent buttonPlayIntent = new Intent("com.test"); //----设置通知栏按钮广播
         PendingIntent pendButtonPlayIntent = PendingIntent.getBroadcast(this, 0, buttonPlayIntent, 0);
         remoteView.setOnClickPendingIntent(R.id.layout, pendButtonPlayIntent);//----设置对应的按钮ID监控
         
         NotificationManager mnotiManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);  
         notiId = 2;
         mnotiManager.notify(notiId, noti);  
    }
    
    
    public HashMap<String, String> parsingArgument(String message) {
		HashMap<String, String> map = new HashMap<String, String>();
		String str =message;

		String[] info = str.split(";", 6);
		if (info.length != 6)
			return null;

		for (int i = 0; i < info.length; i++) {
			map.put(key[i], info[i]);
			
		}
		return map;
	}
}
