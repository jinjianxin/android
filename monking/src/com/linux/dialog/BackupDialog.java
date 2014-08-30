package com.linux.dialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import com.linux.monking.R;
import com.linux.utils.FileUtils;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class BackupDialog extends Dialog{
	
	private Context context = null;
	private FileUtils fileUtils = null;

	public BackupDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		fileUtils = FileUtils.getInstance();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setTitle("备份数据");
		
		setContentView(R.layout.backup_dialog_layout);
		

	}
	

	
}
