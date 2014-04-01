package com.asianux.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.asianux.utils.MediaUtils;
import com.asianux.utils.Mp3Info;

import android.content.Context;
import android.database.Cursor;

public class DbManager {

	private volatile static DbManager dbManager = null;
	private MusicDatabase musicDatabase = null;
	private List<String > collectList = null;
	private List<String > playList = null;

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
		musicDatabase = new MusicDatabase(context);
		collectList = new ArrayList<String>();
		playList = new ArrayList<String>();
		
		System.out.println("-------------------init\n");
		
		musicDatabase.select();
		
		selectAllCollect();
		selectPlaylist();
		
	}
	
	public void insert(Mp3Info mp3info)
	{
		musicDatabase.insert(mp3info);
	}
	
	public void selectAllCollect()
	{
		collectList = musicDatabase.selectCollectAll();
		
		if(collectList !=null &&!collectList.isEmpty())
		{
			System.out.println("collectList size = "+collectList.size());
		}
	}
	
	public void selectPlaylist()
	{	
		playList = musicDatabase.selectPlayListAll();
		
		if(playList !=null &&!playList.isEmpty())
		{
			System.out.println("playList size = "+playList.size());
		}
	}
	
	public void insertCollect(Mp3Info mp3Info)
	{
		musicDatabase.insertCollect(mp3Info);
		
		if(collectList == null)
		{
			collectList =new ArrayList<String>();
			collectList.add(mp3Info.getUrl());
		}

	}
	
	public void remoevCollect(Mp3Info mp3Info)
	{
		musicDatabase.remoevCollect(mp3Info);
		if(collectList !=null &&!collectList.isEmpty())
		{
			collectList.remove(mp3Info.getUrl());
		}
	}
	
	public boolean isCollect(String str)
	{
		if(collectList == null ||collectList.isEmpty() )
		{
			return false;
		}
		else
		{
			return collectList.contains(str);
		}
	}
	
	public List<String >  getCollectList()
	{
		if(collectList == null || collectList.isEmpty())
		{
			return null;
		}
		else
		{
			return collectList;
		}
	}
	
	public void addCollectList(String str)
	{
		collectList.add(str);
	}
	
	public void remoevCollectList(String str)
	{
		collectList.remove(str);
	}
	
	public void insertPlaylistDb(String str)
	{
		musicDatabase.insertPlayList(str);
	}
	
	public void removePlayListDb(String str)
	{
		musicDatabase.removePlayList(str);
	}
	
	public void removePlayListDb(String  playList,String path)
	{
		musicDatabase.removePlayList(playList,path);
	}

	public void addPlayList(String str)
	{
		System.out.println("addPlayList =="+str);
		
		if(playList == null)
		{
			playList = new ArrayList<String>();
			playList.add(str);
		}
		else
		{
			playList.add(str);
		}
	}
	
	public void removePlayList(String str)
	{
		playList.remove(str);
	}
	
	public void renamePlayList(String src,String dest)
	{
		if(playList !=null)
		{
			musicDatabase.renameListDb(src, dest);
			playList.remove(src);
			playList.add(dest);
		}
		
	}
	
	public List<String >  getPlayList()
	{
		if(playList == null || playList.isEmpty())
		{
			return null;
		}
		else
		{
			return playList;
		}
	}
	
	public boolean findPlayList(String str)
	{
		
		if(playList == null || playList.isEmpty())
		{
			return true;
		}
		else
		{
			if(playList.contains(str))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
	}
	
	public List<String > getListWithName(String str)
	{
		return musicDatabase.getListWithName(str);
	}
	
	public void cleanMusicDb()
	{
		musicDatabase.clean();
	}
	
	public void insert(List<Mp3Info> list) {
		// TODO Auto-generated method stub
		musicDatabase.insert(list);	
	}
	
	public List<Mp3Info> getAllMusic()
	{
		return musicDatabase.getAllMusic();
	}
	
	
	public String select(Mp3Info mp3info)
	{
		return musicDatabase.select(mp3info);
	}
	
	public void insertListDb(String name,String url)
	{
		musicDatabase.insertListDb(name, url);
	}
	
	public void removeListDb(String name)
	{
		musicDatabase.removeListDb(name);
	}

}
