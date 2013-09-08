package com.asianux.database;

import java.util.ArrayList;
import java.util.List;

import com.asianux.utils.MediaUtils;
import com.asianux.utils.Mp3Info;

import android.content.Context;
import android.database.Cursor;

public class DbManager {

	private volatile static DbManager dbManager = null;
	private MusicDatabase musicDatabase = null;
	private  List<Mp3Info> musicList =  new ArrayList<Mp3Info>();

	public static DbManager getInstance(Context context ) {

		synchronized (MediaUtils.class) {
			if (dbManager == null) {
				dbManager = new DbManager(context);
			}
		}

		return dbManager;
	}

	public DbManager(Context context) {
		// TODO Auto-generated constructor stub
		super();
		
		System.out.println("********************************DbManager");
		musicDatabase = new MusicDatabase(context);
		
		musicDatabase.select();
	}
	
	public void insert(Mp3Info mp3info)
	{
		musicDatabase.insert(mp3info);
	}
	
	public List<Mp3Info> getMp3Info() {
		
		if(musicList.isEmpty())
		{
			musicList = musicDatabase.getMp3Info();
		}
		else
		{
			return musicList;
		}
		
		return null;
	}

	public void insert(List<Mp3Info> list) {
		// TODO Auto-generated method stub
		musicDatabase.insert(list);
		
		
	}
	
	public Mp3Info select(Mp3Info mp3info)
	{
		return musicDatabase.select(mp3info);
	}
	
}
