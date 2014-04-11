package com.example.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.os.Environment;
import android.util.Log;

public class FileUtils {

	private String TAG = "shentan";
	private static String root = null;
	private static FileUtils fileUtils = null;
	private static String ROOT_PATH = "/GameSetting";

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

		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);

		if (sdCardExist) {
			root = Environment.getExternalStorageDirectory().toString();
		} else {
			root = Environment.getDataDirectory().toString();
		}

		String path = root + ROOT_PATH;

		System.out.println(path);

		File rootFile = new File(path);

		if (!rootFile.exists()) {
			rootFile.mkdirs();
		}

	}

	public boolean isFileExists(String name) {
		String filename = root + ROOT_PATH + "/" + "config";

		File file = new File(filename);

		return file.exists();

	}

	public boolean isFileExists() {
		String filename = root + ROOT_PATH + "/" + "config";

		File file = new File(filename);

		return file.exists();

	}
	
	public void write(String filePath, String content) {
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new FileWriter(filePath));
			bw.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					bw = null;
				}
			}
		}
	}
	
	
	public String read(String filePath,ArrayList<String> keyList,ArrayList<String> mapList) {
		BufferedReader br = null;
		String line = null;
		StringBuffer buf = new StringBuffer();
		
		
		try {
			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {
									
				if(line.startsWith("name="+mapList.get(0)))
				{
					buf.append(line);
					buf.append(System.getProperty("line.separator"));
					for(int i = 1;i<keyList.size();i++)
					{
						buf.append(keyList.get(i)+"="+mapList.get(i));
						buf.append(System.getProperty("line.separator"));
						br.readLine();
					}
				}
				else
				{	
					buf.append(line);
				}
				buf.append(System.getProperty("line.separator"));
					
			}
			
			for(int i = 0;i<keyList.size();i++)
			{
				buf.append(keyList.get(i)+"="+mapList.get(i));
				buf.append(System.getProperty("line.separator"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// ¹Ø±ÕÁ÷
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					br = null;
				}
			}
		}
		
		return buf.toString();
	}
	
	
	
	public void saveConfig(ArrayList<String> keyList, ArrayList<String> mapList)
	{
		String filetest = root + ROOT_PATH + "/" + "config";
		Log.d(TAG, "path = "+filetest);
		if(isFileExists())
		{
			Log.d(TAG, "file is exists 1");

			String filename = root + ROOT_PATH + "/" + "config";
			write(filename, read(filename,keyList,mapList));
			
		}
		else
		{
			Log.d(TAG, "create new file");
	
			FileWriter fw;
			try {
				fw = new FileWriter(root+ROOT_PATH+"/"+"config");

				int size = keyList.size();
				for (int i = 0; i < size; i++) {
					Log.d(TAG, keyList.get(i)+"="+mapList.get(i));
					fw.write(keyList.get(i)+"="+mapList.get(i)+"\r\n");
				}
							
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
