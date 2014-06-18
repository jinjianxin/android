package com.joymetec.push;

import java.util.HashMap;

import android.util.Log;

public class DataInstance {

	private static DataInstance dataInstance= null;
	private HashMap<String, String> map;
	private HashMap<String, String> timeOutMap;
	private int id = 0;
	private String filePath = null;
	
	public static DataInstance getInstance() {

		synchronized (DataInstance.class) {
			if (dataInstance == null) {
				dataInstance = new DataInstance();
			}
		}

		return dataInstance;
	}
	
	public DataInstance() {
		// TODO Auto-generated constructor stub
		map = new HashMap<String, String>();
		timeOutMap = new HashMap<String, String>();
	}
	
	public boolean isDownloading(String key)
	{
		Log.d(JoymetecApplication.TAG, "key = "+key);
		Log.d(JoymetecApplication.TAG, "test "+map.containsKey(key));
			
		return map.containsKey(key);
	}
	
	public void addDownling(String key)
	{
		map.put(key, key);
	}
	
	public void removeDownling(String key)
	{
		map.remove(key);
	}

	public boolean isTimeOut(String key)
	{		
		Log.d(JoymetecApplication.TAG, "timeOutMap "+timeOutMap.containsKey(key));
		return timeOutMap.containsKey(key);
	}
	
	public void addTimeOutMap(String key)
	{
		timeOutMap.put(key, key);
	}
	
	public void removeTimeOutMap(String key)
	{
		timeOutMap.remove(key);
	
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
