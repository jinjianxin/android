package com.example.database;

import java.util.ArrayList;
import java.util.List;

import com.example.utils.TaskData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbManager  extends SQLiteOpenHelper{
	
	private static final String DATABASE_NAME = "music.db";
	private static final int DATABASE_VERSION = 1;

	private volatile static DbManager dbManager = null;
	
	
	private static final String TABLE_NAME = "music";

	
	private static final String TITLE = "title";
	private static final String PRIORITY= "priority";
	private static final String DATE = "date";
	private static final String START_TIME = "startTime";
	private static final String END_TIME = "endTime";
	
	private List<TaskData> tmpList = null;

	public static DbManager getInstance(Context context ) {

		synchronized (DbManager.class) {
			if (dbManager == null) {
				dbManager = new DbManager(context);
			}
		}

		return dbManager;
	}

	public DbManager(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT,priority INTEGER,date TEXT,startTime,TEXT,endTime TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public void select() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "SELECT * FROM " + TABLE_NAME + ";";

		db.rawQuery(sql, null);

	}

	
	/**
	 * @param taskData
	 */
	public void insertDataItem(TaskData taskData) {
		// TODO Auto-generated method stub
		
		if(tmpList==null)
		{
			tmpList = new ArrayList<TaskData>();
		}
		
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();

		ContentValues values = new ContentValues();
		values.put(TITLE,taskData.getmTitle());
		values.put(PRIORITY,taskData.getmPriority());
		values.put(DATE, taskData.getmDate());
		values.put(START_TIME,taskData.getmStartTime());
		values.put(END_TIME, taskData.getmEndTime());
		
		
		db.insert(TABLE_NAME, null, values);

		db.setTransactionSuccessful();
		db.endTransaction();
		
		tmpList.add(taskData);
		
	}
	
	public List<TaskData> getTmpList()
	{
		return tmpList;
	}
	
	public void resetTmpList()
	{
		tmpList = null;
	}
	
	
	
	public List<TaskData> getData()
	{
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = null;

		sql = "SELECT * FROM " + TABLE_NAME + ";";

		Cursor c = db.rawQuery(sql, null);
		
		return getDetailResult(c);
	}
	
	private List<TaskData > getDetailResult(Cursor c)
	{
		
		List<TaskData> list = new ArrayList<TaskData>();
		
		if (c.getCount() != 0) 
		{
		
			while (c.moveToNext()) {
				
				TaskData taskData = new TaskData();
				taskData.setmTitle(c.getString(c.getColumnIndex(TITLE)));
				taskData.setmPriority(c.getInt(c.getColumnIndex(PRIORITY)));
				taskData.setmDate(c.getString(c.getColumnIndex(DATE)));
				taskData.setmStartTime(c.getString(c.getColumnIndex(START_TIME)));
				taskData.setmEndTime(c.getString(c.getColumnIndex(END_TIME)));
				
				list.add(taskData);
				
		
			}
		
			c.close();
			
			return list;
		}
		else	
		{
			return null;
		}

	}
}
