package com.linux.monking;

import com.linux.dialog.BackupDialog;
import com.linux.monking.R;
import com.linux.monking.R.xml;
import com.linux.setting.BackupActivity;
import com.linux.setting.DateSelectActivity;
import com.linux.utils.FileUtils;

import android.R.bool;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class SettingActivity extends PreferenceActivity implements OnPreferenceClickListener {
	
	private PreferenceScreen show_screen = null;
	private PreferenceScreen backup_screen = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.setting_layout);
		
		show_screen = (PreferenceScreen) findPreference("setting_show");	
		backup_screen = (PreferenceScreen) findPreference("setting_backup");
		
		show_screen.setOnPreferenceClickListener(this);
		backup_screen.setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		
		if(preference == show_screen)
		{
			Intent intent = new Intent();
			intent.setClass(this, DateSelectActivity.class);
			startActivity(intent);
			
		}else if(preference == backup_screen)
		{
			launchBackDialog();
		}
		
		return false;
	}
	
	private void launchBackDialog()
	{	
		FileUtils fileUtils = FileUtils.getInstance();
		
		if(fileUtils.isSDcardExist())
		{
	/*		BackupDialog backupDialog = new BackupDialog(this);
			backupDialog.show();*/
			
			Intent intent = new Intent();
			intent.setClass(this, BackupActivity.class);
			startActivity(intent);
			
		}
		else
		{
			 AlertDialog.Builder builder = new Builder(this);
			 builder.setMessage("系统不存在SD卡，无法执行此操作");

			 builder.setTitle("警告");
	
			 
			builder.setPositiveButton("确认", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			 
			 builder.create().show();
			 
		}
		
	}

}
