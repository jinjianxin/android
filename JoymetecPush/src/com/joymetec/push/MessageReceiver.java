package com.joymetec.push;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.sax.StartElementListener;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;


public class MessageReceiver extends PushMessageReceiver {
    private String mRegId;
    private long mResultCode = -1;
    private String mReason;
    private String mCommand;
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mStartTime;
    private String mEndTime;
    private String[] key = { "url", "name", "package", "version" ,"tip","title"};
    private int notifyId = 0;
    private String CLICK_ACTION = "com.joymetec.click"; 
    private String SHOW_NOTIFICATION = "com.joymetec.shownotify";
    
    @Override
    public void onReceiveMessage(Context context, MiPushMessage message) {
       Log.v(JoymetecApplication.TAG, "onReceiveMessage is called. " + message.toString());
        mMessage = message.getContent();
        if(!TextUtils.isEmpty(message.getTopic())) {
            mTopic=message.getTopic();
        } else if(!TextUtils.isEmpty(message.getAlias())) {
            mAlias=message.getAlias();
        }
               
        Message msg = Message.obtain();
        msg.obj = message.toString();
        JoymetecApplication.getHandler().sendMessage(msg);
        
        
        HashMap<String, String> map = parsingArgument(mMessage);
        if(map!=null)
        {
        	//showNotification(context,map.get(key[4]),map.get(key[5]));
        	Intent intent = new Intent();
        	intent.setAction(SHOW_NOTIFICATION);
        	intent.putExtra("arg", mMessage);
        	context.sendBroadcast(intent);
        }
       
        
        if(map!=null &&mMessage.length()>0)
        {
        	Log.d(JoymetecApplication.TAG, "msg = "+mMessage);
        	if(Network.hasNetwork(context))
        	{
        		boolean wifiState =  Network.isWIFIConnected(context);
        		if(wifiState)
        		{		
        			Intent intent = new Intent();
        			intent.putExtra("url", mMessage);
        			intent.setClass(context, DownLoadService.class);
        			context.startService(intent);
        		}
        	}
       }
    }

    
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        Log.v(JoymetecApplication.TAG, "onCommandResult is called. " + message.toString());
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        if(arguments != null) {
            if(MiPushClient.COMMAND_REGISTER.equals(command)
                    && arguments.size() == 1) {
                mRegId = arguments.get(0);
                MiPushClient.subscribe(context, "hssg", null);
                MiPushClient.setAlias(context, "hssg", null);
                Log.d(JoymetecApplication.TAG, "mRegId = "+mRegId);
            } else if((MiPushClient.COMMAND_SET_ALIAS .equals(command)
                    || MiPushClient.COMMAND_UNSET_ALIAS.equals(command))
                    && arguments.size() == 1) {
                mAlias = arguments.get(0);
            } else if((MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)
                    || MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command))
                    && arguments.size() == 1) {
                mTopic = arguments.get(0);
            } else if(MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)
                    && arguments.size() == 2) {
                mStartTime = arguments.get(0);
                mEndTime = arguments.get(1);
            }
        }
        mResultCode = message.getResultCode();
        mReason = message.getReason();

        Message msg = Message.obtain();
        msg.obj = message.toString();
        JoymetecApplication.getHandler().sendMessage(msg);
    }
   
    public static class DemoHandler extends Handler {
  
        private Context context;

        public DemoHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
          
        }
    }
    
    public HashMap<String, String> parsingArgument(String message) {
		HashMap<String, String> map = new HashMap<String, String>();
		String str =message;

		String[] info = str.split(";", 6);

	//	Log.d(JoymetecApplication.TAG, "size = " + info.length);

		if (info.length != 6)
			return null;

		for (int i = 0; i < info.length; i++) {
			map.put(key[i], info[i]);
			//Log.d(JoymetecApplication.TAG, "key =" + key[i] + "  " + info[i]);
		}
		return map;
	}
   
}