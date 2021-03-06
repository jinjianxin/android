package com.joymetec.push;

import android.app.Application;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.provider.ContactsContract.Contacts;
import android.telephony.cdma.CdmaCellLocation;
import android.util.Log;

import com.joymetec.push.MessageReceiver.DemoHandler;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Constants;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * 1、为了打开客户端的日志，便于在开发过程中调试，需要自定义一个Application。
 *    并将自定义的application注册在AndroidManifest.xml文件中
 * 2、为了提高push的注册率，您可以在Application的onCreate中初始化push。你也可以根据需要，在其他地方初始化push。
 * @author wangkuiwei
 *
 */
public class JoymetecApplication extends Application {

    // user your appid the key. 
    public static final String APP_ID = "2882303761517223625";
    // user your appid the key.
    public static final String APP_KEY = "5501722376625";

    //此TAG在adb logcat中检索自己所需要的信息， 只需在命令行终端输入 adb logcat | grep com.xiaomi.mipushdemo
    public static final String TAG = "lemi";

    private static DemoHandler handler = null;

    @Override
    public void onCreate() {
        super.onCreate();

       Constants.useOfficial();
       //Constants.useSandbox();

       /*LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };


        Logger.setLogger(this, newLogger); */
        
        
        if(handler == null)
            handler = new DemoHandler(getApplicationContext());

        // 注册push服务，注册成功后会向DemoMessageReceiver发送广播
        // 可以从DemoMessageReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
        if (shouldInit()) {
        	
        	Log.d(TAG, "register success");
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }

    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static DemoHandler getHandler() {
        return handler;
    }
}
