package com.asianux.utils;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月8日 上午9:59:06
 * 类说明
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class FileUtils  {
	
	private static String root = null;
	private static FileUtils fileUtils = null;
	private static String ROOT_PATH = "/Asmusic";
	private static String LRC_PATH="/lrc";
	private static String ALBUM_PATH = "/album";
	private String lrcPath = null;
	private String albumPath = null;
	
	
	
	public static FileUtils getInstance() {

		synchronized (FileUtils.class) {
			if (fileUtils == null) {
				fileUtils = new FileUtils();
			}
		}

		return fileUtils;
	}
	
	public FileUtils() {
		// TODO Auto-generated constructor stub

		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		
				
		if(sdCardExist)
		{
			root = Environment.getExternalStorageDirectory().toString();
		}
		else
		{
			root=Environment.getDataDirectory().toString();
		}
		
		String path = root+ROOT_PATH;
		
		System.out.println(path);
	
		File rootFile = new File(path);
		
		if(!rootFile.exists())
		{	
			rootFile.mkdirs();
		}	
		
		File lrcFile = new File(root+ROOT_PATH+LRC_PATH);
		
		if(!lrcFile.exists())
		{	
			lrcFile.mkdirs();
		}
		
		File albumFile = new File(root+ROOT_PATH+ALBUM_PATH);
		
		if(!albumFile.exists())
		{
			albumFile.mkdirs();
		}
		
	}
	
	public String saveLrcFile(InputStream in,String name) throws IOException
	{
		String filename = root+ROOT_PATH+LRC_PATH+"/"+name +".lrc";
		
		File file = new File(filename);
		
		if(!file.exists())
		{
			FileOutputStream output = new FileOutputStream(file);
			byte[] buffer = new byte[1024];

			int length;
			while ((length = (in.read(buffer))) > 0) {
				output.write(buffer, 0, length);
			}

			output.flush();
			in.close();
			
			return filename;
		}
		else
		{
			return filename;
		}
	}
	
	public boolean isFileExists(String name)
	{
		String filename = root+ROOT_PATH+LRC_PATH+"/"+name +".lrc";
		
		File file = new File(filename);
		
		return file.exists();
				
	}
	
	public String getLrcPath()
	{
		return root+ROOT_PATH+LRC_PATH;
	}

}
