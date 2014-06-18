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
 * 1��Ϊ�˴򿪿ͻ��˵���־�������ڿ��������е��ԣ���Ҫ�Զ���һ��Application��
 *    �����Զ����applicationע����AndroidManifest.xml�ļ���
 * 2��Ϊ�����push��ע���ʣ���������Application��onCreate�г�ʼ��push����Ҳ���Ը�����Ҫ���������ط���ʼ��push��
 * @author wangkuiwei
 *
 */
public class JoymetecApplication extends Application {

    // user your appid the key. 
    public static final String APP_ID = "2882303761517223625";
    // user your appid the key.
    public static final String APP_KEY = "5501722376625";

    //��TAG��adb logcat�м����Լ�����Ҫ����Ϣ�� ֻ�����������ն����� adb logcat | grep com.xiaomi.mipushdemo
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

        // ע��push����ע��ɹ������DemoMessageReceiver���͹㲥
        // ���Դ�DemoMessageReceiver��onCommandResult������MiPushCommandMessage��������л�ȡע����Ϣ
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