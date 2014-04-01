package com.asianux.database;

import java.util.ArrayList;
import java.util.List;

import com.asianux.utils.Mp3Info;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.mock.MockApplication;
import android.widget.ArrayAdapter;

public class MusicDatabase extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "music.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String TABLE_NAME = "music";
	private static final String COLLECT_TABLE = "collect";
	private static final String PLAYlIST_TABLE = "playlist";
	private static final String lIST_TABLE = "listdb";
	
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String SIGNER = "signer";
	private static final String PATH = "path";
	private static final String LRC = "lrc";
	private static final String IMAGE = "image";
	private static final String COLLECT = "collect";
	private static final String LIST = "list";
	private static final String URL = "url";
	private static final String ALBUM = "album";
	private static final String TITLE = "title";

	public MusicDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		System.out.println("MusicDatabase\n");

		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, signer TEXT, path TEXT,lrc TEXT,image TEXT,collect TEXT,album TEXT,title TEXT);");

		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ COLLECT_TABLE
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, path TEXT);");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ PLAYlIST_TABLE
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ lIST_TABLE
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, list TEXT,url TEXT);");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public Cursor select() {
		
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "SELECT * FROM " + TABLE_NAME +";";
		
		db.rawQuery(sql, null);
		
		sql = "SELECT * FROM " + COLLECT_TABLE +";";
		
		db.rawQuery(sql, null);
		
		sql = "SELECT * FROM " + PLAYlIST_TABLE +";";
		
		db.rawQuery(sql, null);
		
		return null;
	}

	public void insert(Mp3Info mp3info) {
		// System.out.println("title = "+mp3info.getUrl());

		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();

		ContentValues values = new ContentValues();
		values.put(NAME, mp3info.getTitle());
		values.put(SIGNER, mp3info.getArtist());
		values.put(PATH, mp3info.getUrl());
		values.put(LRC, mp3info.getLrc());
		values.put(COLLECT, "0");

		db.insert(TABLE_NAME, null, values);

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public List<Mp3Info> getMp3Info() {

		return null;
	}

	public void insert(List<Mp3Info> list) {
		// TODO Auto-generated method stub

		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();

		for (Mp3Info mp3info : list) {

			ContentValues values = new ContentValues();
			if(mp3info.getTitle() != null && !mp3info.getTitle().isEmpty())
			{
				values.put(NAME, mp3info.getTitle());
			}
			else
			{
				values.put(NAME, "NULL");
			}
			
			if(mp3info.getArtist() !=null && !mp3info.getArtist().isEmpty())
			{
			//	System.out.println(mp3info.getArtist());
				values.put(SIGNER, mp3info.getArtist());
			}
			else
			{
				values.put(SIGNER, "NULL");
			}
			
			if(mp3info.getAlbum() !=null && !mp3info.getAlbum().isEmpty())
			{
				values.put(ALBUM, mp3info.getAlbum());
			}
			else
			{
				values.put(ALBUM, "NULL");
			}
			
			if(mp3info.getTitle() !=null && !mp3info.getTitle().isEmpty())
			{
				values.put(TITLE, mp3info.getTitle());
			}
			else
			{
				values.put(TITLE, "NULL");
			}
			
			values.put(PATH, mp3info.getUrl());
			values.put(COLLECT, "0");

			db.insert(TABLE_NAME, null, values);

		}

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public String select(Mp3Info mp3info) {
		// TODO Auto-generated method stub
		String name = null;

		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + "name='"
				+ mp3info.getTitle() + "' AND " + "signer='"
				+ mp3info.getArtist() + "';";

		Cursor c = db.rawQuery(sql, null);

		if (c.getCount() != 0) {
			Mp3Info info = new Mp3Info();

			while (c.moveToNext()) {
				name = c.getString(c.getColumnIndex(LRC));
				// info.setLrc(c.getString(c.getColumnIndex(LRC)));
				break;
			}
			c.close();

			return name;
		} else {
			c.close();
			return null;
		}
	}

	public void insertCollect(Mp3Info mp3Info) {
		
		if (!isCollect(mp3Info)) {

			System.out.println(mp3Info.toString());
			
			SQLiteDatabase db = this.getWritableDatabase();
			db.beginTransaction();

			ContentValues values = new ContentValues();
			values.put(PATH, mp3Info.getUrl());

			db.insert(COLLECT_TABLE, null, values);

			db.setTransactionSuccessful();
			db.endTransaction();
		}
	}

	public void remoevCollect(Mp3Info mp3Info) {
		// TODO Auto-generated method stub

		
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "DELETE  FROM " + COLLECT_TABLE + " WHERE " + "path='"
				+ mp3Info.getUrl() + "';";
		
		System.out.println("sql = "+sql);

		db.execSQL(sql);
	}

	public boolean isCollect(Mp3Info mp3Info) {
		
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "SELECT * FROM " + COLLECT_TABLE + " WHERE " + "path='"
				+ mp3Info.getUrl()+ "';";

		Cursor c = db.rawQuery(sql, null);

		if (c.getCount() != 0) {
			c.close();
			return true;
		} else {
			c.close();
			return false;
		}
	}

	public List<String> selectCollectAll() {
		// TODO Auto-generated method stub
		
		List<String > list = new ArrayList<String>();
		
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "SELECT * FROM " + COLLECT_TABLE +";";
		
		Cursor c = db.rawQuery(sql, null);
		
		if (c.getCount() != 0) {
			
			while (c.moveToNext()) {
				String name = c.getString(c.getColumnIndex(PATH));
				// info.setLrc(c.getString(c.getColumnIndex(LRC)));			
				list.add(name);
			}
			c.close();
			
			return list;

		} else {
			c.close();
			
			return null;
		}
		
	}
	
	public void insertPlayList(String str) {
			
			SQLiteDatabase db = this.getWritableDatabase();
			db.beginTransaction();

			ContentValues values = new ContentValues();
			values.put(NAME, str);

			db.insert(PLAYlIST_TABLE, null, values);

			db.setTransactionSuccessful();
			db.endTransaction();
	}

	/**
	 * removePlayList删除自定义列表
	 * (这里描述这个方法适用条件 – 可选)
	 * @param str 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public void removePlayList(String str)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "DELETE  FROM " + PLAYlIST_TABLE + " WHERE " + "name='"
				+ str + "';";
		
		System.out.println("sql = "+sql);

		db.execSQL(sql);
	}
	
	/**
	 * removePlayList删除自定义列表单个item
	 * (这里描述这个方法适用条件 – 可选)
	 * @param playList
	 * @param path 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public void removePlayList(String playList, String path) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "DELETE  FROM " + lIST_TABLE+ " WHERE " + "list='"
				+ playList +"' AND " +" url='"+path + "';";
		
		db.execSQL(sql);
	} 
	
	public List<String> selectPlayListAll() {
		// TODO Auto-generated method stub
		
		List<String > list = new ArrayList<String>();
		
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "SELECT * FROM " + PLAYlIST_TABLE +";";
		
		Cursor c = db.rawQuery(sql, null);
		
		if (c.getCount() != 0) {
			
			while (c.moveToNext()) {
				String name = c.getString(c.getColumnIndex(NAME));
				// info.setLrc(c.getString(c.getColumnIndex(LRC)));
				list.add(name);
			}
			c.close();
			
			return list;

		} else {
			c.close();
			
			return null;
		}
		
	}
	
	public void insertListDb(String name,String url) {
		
		if (!isListDb(name, url)) {

			SQLiteDatabase db = this.getWritableDatabase();
			db.beginTransaction();

			ContentValues values = new ContentValues();
			values.put(LIST, name);
			values.put(URL, url);

			db.insert(lIST_TABLE, null, values);

			db.setTransactionSuccessful();
			db.endTransaction();
		}
	}


	public void removeListDb(String name) {
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "DELETE  FROM " + lIST_TABLE+ " WHERE " + "list='"
				+ name + "';";

		System.out.println("sql = " + sql);

		db.execSQL(sql);
		
		sql = "DELETE  FROM " + PLAYlIST_TABLE+ " WHERE " + "name='"
				+ name + "';";
		db.execSQL(sql);
		
	}
	
	public void renameListDb(String src,String dest)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "UPDATE " + PLAYlIST_TABLE+ " SET "+ "name='"+dest+"'" +" WHERE " + "name='"
				+ src + "';";

		db.execSQL(sql);
		
		//UPDATE listdb SET list='test1' WHERE list='test';
		sql = "UPDATE "+lIST_TABLE+" SET "+" list='"+dest+"'"+"WHERE " + "list='"+ src + "';";;
		
		db.execSQL(sql);
		
	}

	public boolean isListDb(String name,String url) {
		
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "SELECT * FROM " + lIST_TABLE + " WHERE " + "list='"
				+ name + "' AND " + "url='"+url + "';";

		Cursor c = db.rawQuery(sql, null);

		if (c.getCount() != 0) {
			c.close();
			return true;
		} else {
			c.close();
			return false;
		}
	}
	
	/**
	 * getListWithName 查询自定义播放列表
	 * (这里描述这个方法适用条件 – 可选)
	 * @param name
	 * @return 
	 *List<String>
	 * @exception 
	 * @since  1.0.0
	 */
	
	public List<String > getListWithName(String name)
	{
		
		List<String> list = new ArrayList<String>();

		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "SELECT * FROM " + lIST_TABLE +" WHERE " + "list='"
				+ name+ "';";

		Cursor c = db.rawQuery(sql, null);

		if (c.getCount() != 0) {

			while (c.moveToNext()) {
				String url = c.getString(c.getColumnIndex(URL));
				list.add(url);
			}
			c.close();

			return list;

		} else {
			c.close();

			return null;
		}
	}

	public void clean() {
		// TODO Auto-generated method stub
		
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "DELETE  FROM " + TABLE_NAME+ ";";
		
		//System.out.println("sql = "+sql);

		db.execSQL(sql);
		
	}

	public List<Mp3Info> getAllMusic() {
		// TODO Auto-generated method stub
		
		List<Mp3Info > list = new ArrayList<Mp3Info>();
		
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "SELECT * FROM " + TABLE_NAME+";";
		
		Cursor c = db.rawQuery(sql, null);
		
		if (c.getCount() != 0) {
			
			while (c.moveToNext()) {
				Mp3Info mp3Info = new Mp3Info();
				
				String album = c.getString(c.getColumnIndex(ALBUM));
				String path = c.getString(c.getColumnIndex(PATH));
				String singer =c.getString(c.getColumnIndex(SIGNER));
				String title = c.getString(c.getColumnIndex(TITLE));	

				if(album == null)
				{
				//	System.out.println("path = "+path);
					album = "NULL";
				}
			
				if(title == null)
				{
					title ="NULL";
				}
				
				if(singer == null)
				{
					singer = "NULL";
				}
			
				mp3Info.setAlbum(album);
				mp3Info.setArtist(singer);
				mp3Info.setUrl(path);
				mp3Info.setTitle(title);
				
				list.add(mp3Info);
			}
			c.close();
			
			return list;

		} else {
			c.close();
			
			return null;
		}
	}

}
