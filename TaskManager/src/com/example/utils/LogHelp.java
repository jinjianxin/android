
/**  
* @Title: LogHelp.java
* @Package com.example.utils
* @Description: TODO(用一句话描述该文件做什么)
* @author jjx
* @date 2014年8月29日 下午10:50:52
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
