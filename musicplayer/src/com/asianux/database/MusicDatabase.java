package com.asianux.database;

import java.util.List;

import com.asianux.utils.Mp3Info;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MusicDatabase extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "music.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "music";
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String SIGNER = "signer";
	private static final String PATH = "path";
	private static final String LRC = "lrc";
	private static final String IMAGE = "image";
	private static final String COLLECT = "collect";

	public MusicDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		System.out
				.println("MusicDatabase-----------------------------------------\n");

		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, signer TEXT, path TEXT,lrc TEXT,image TEXT,collect TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public Cursor select() {
		return null;
		/*
		 * System.out.println("*********************select\n"); SQLiteDatabase
		 * db=this.getReadableDatabase(); Cursor cursor=db.query(TABLE_NAME,
		 * null, null, null, null, null, " id desc"); return cursor;
		 */
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
			values.put(NAME, mp3info.getTitle());
			values.put(SIGNER, mp3info.getArtist());
			values.put(PATH, mp3info.getUrl());
			values.put(COLLECT, "0");

			db.insert(TABLE_NAME, null, values);

		}

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public Mp3Info select(Mp3Info mp3info) {
		// TODO Auto-generated method stub

		System.out.println(mp3info.toString());

		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + "name='"
				+ mp3info.getTitle() + "' AND " + "signer='"
				+ mp3info.getArtist() + "';";

		System.out.println("sql=" + sql);

		Cursor c = db.rawQuery(sql, null);
		
		if(c.getCount() !=0)
		{
			Mp3Info info = new Mp3Info();
			
		while (c.moveToNext()) {
			String name = c.getString(c.getColumnIndex(LRC));
			System.out.println("select name = " + name);
			info.setLrc(c.getString(c.getColumnIndex(LRC)));
			
		}
			c.close();
			
			return info;
		}
		else
		{
			c.close();
			return null;
		}
	}

}
