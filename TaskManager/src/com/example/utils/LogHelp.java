
/**  
* @Title: LogHelp.java
* @Package com.example.utils
* @Description: TODO(��һ�仰�������ļ���ʲô)
* @author jjx
* @date 2014��8��29�� ����10:50:52
* @version V1.0 
*/

 
package com.example.utils;

import android.util.Log;


/**
 * @author jjx
 *
 */

public class LogHelp {

	private static String TAG = "task";
	private static boolean DEBUG = true;
	
	public LogHelp()
	{
		
	}
	
	public static void logPutPut(String log)
	{
		if(DEBUG)
		{
			Log.d(TAG, log);
		}
	}
}