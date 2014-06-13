package com.joymetec.push;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xiaomi.xmpush.server.*;



public class MainActivity extends Activity {

	private static String MY_PACKAGE_NAME = "com.joymetec.push";
	private Button  button = null;
	private String TAG = "lemi";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Constants.useSandbox();
		
		button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				String info = getAppInfo();
				if(info !=null)
				{
					Log.d(TAG, getAppInfo());
				}
				else
				{
					Log.d(TAG, "no install");
				}
				
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
}
