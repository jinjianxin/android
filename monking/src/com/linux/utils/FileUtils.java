package com.linux.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.test.mock.MockApplication;
import android.util.Log;

public class FileUtils {

	private volatile static FileUtils fileUtils = null;
	private String sdPath = null;
	private String BACKUP_PATH = "/monking";

	public static FileUtils getInstance() {

		synchronized (FileUtils.class) {
			if (fileUtils == null) {
				
				fileUtils = new FileUtils();
			}
		}

		return fileUtils;
	}
	
	public FileUtils()
	{
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

		if(sdCardExist)
		{
		    sdPath = getSDcardPath();
		}
		
		if(sdPath !=null)
		{
			 String path = sdPath+BACKUP_PATH;

			 File rootFile = new File(path);

			 if(!rootFile.exists())
			 {
			     rootFile.mkdirs();
			 }
		}
	}
	
	public boolean isSDcardExist()
	{
		if(sdPath!=null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	

	private String getSDcardPath()
	{
		try {
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			String line;
			String mount = new String();
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				if (line.contains("secure")) continue;
				if (line.contains("asec")) continue;
				
				if (line.contains("fat")) {
					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1) {
						mount = mount.concat(columns[1]);
						break;
					}
				}
			}
			
			return mount;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public String getBackupPath() {
		// TODO Auto-generated method stub
		
		if(sdPath!=null)
		{
			return sdPath+BACKUP_PATH;
		}
		else
		{
			return null;
		}
	}
}
