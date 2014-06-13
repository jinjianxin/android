package com.joymetec.push;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.sax.StartElementListener;
import android.text.TextUtils;
import android.util.Log;
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
    private String[] key = { "url", "name", "package", "version" };

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
        
        if(!mMessage.isEmpty())
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
        		else
        		{
        			HashMap<String, String> map = parsingArgument(mMessage);
        			if(map !=null)
        			{
        				Uri  uri = Uri.parse(map.get(key[0]));
        				Intent  intent = new  Intent(Intent.ACTION_VIEW, uri);
        				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        				context.startActivity(intent);
        			}
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
                //MiPushClient.subscribe(context, "hssg", null);
                MiPushClient.subscribe(context, "hssg", null);
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
            String s = (String)msg.obj;
            Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        }
    }
    
    public HashMap<String, String> parsingArgument(String message) {
		HashMap<String, String> map = new HashMap<String, String>();
		String str =message;

		String[] info = str.split(";", 4);

		Log.d(JoymetecApplication.TAG, "size = " + info.length);

		if (info.length != 4)
			return null;

		for (int i = 0; i < info.length; i++) {
			map.put(key[i], info[i]);
			Log.d(JoymetecApplication.TAG, "key =" + key[i] + "  " + info[i]);
		}
		return map;
	}
    
}