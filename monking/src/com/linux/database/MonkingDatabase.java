package com.linux.database;

import java.util.ArrayList;
import java.util.List;

import com.linux.utils.DataItem;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MonkingDatabase  extends SQLiteOpenHelper{
	
	private static final String DATABASE_NAME = "monking.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String KIND_TABLE = "kind_table";
	private static final String COUNT_TABLE = "income_table";
	private static final String UNIT_TABLE ="unit_table";
	
	private static final String ID = "id";
	private static final String TIME = "time";  //整型，用来排序
	private static final String YEAR = "year";
	private static final String MONTH = "month";
	private static final String DAY = "day";
	private static final String FLAG = "flag";
	private static final String KIND = "kind";
	private static final String COUNT = "number";
	private static final String UNIT = "unit";
	private static final String DATE = "day";
	private static final String PRICE ="price";
	private static final String SUM ="sum";
	
	//private static final String 

	public MonkingDatabase(Context context) {
		super(context,DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+KIND_TABLE
				+" (id INTEGER PRIMARY KEY AUTOINCREMENT,kind TEXT,flag TEXT);"
				);
		
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+UNIT_TABLE
				+" (id INTEGER PRIMARY KEY AUTOINCREMENT,unit TEXT,flag TEXT);"
				);
		
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+COUNT_TABLE
				+" (id INTEGER PRIMARY KEY AUTOINCREMENT ,time INTEGER, year INTEGER , month INTEGER , day INTEGER , kind TEXT,number TEXT,unit TEXT,price TEXT,sum TEXT,flag TEXT);"
				);
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public void select() {
		
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "SELECT * FROM " + KIND_TABLE +";";
		
		db.rawQuery(sql, null);
		
		sql = "SELECT * FROM " + COUNT_TABLE +";";
		
		db.rawQuery(sql, null);
	}

	public void insertDataItem(DataItem item) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();

		ContentValues values = new ContentValues();
		values.put(KIND, item.getKind());
		values.put(COUNT,item.getNumber());
		values.put(UNIT, item.getUnit());
		values.put(PRICE,item.getPrice());
		values.put(SUM, item.getSum());
		values.put(YEAR, item.getYear());
		values.put(MONTH, item.getMonth());
		values.put(DAY, item.getDay());
		values.put(FLAG, item.getFlag());
		values.put(TIME, item.getTime());
		
		System.out.println(item.getTime());
		
		
		db.insert(COUNT_TABLE, null, values);

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public Intent getMonthSum(String month) {
		// TODO Auto-generated method stub
		
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "SELECT * FROM " + COUNT_TABLE + " WHERE " + "month='"
				+ month + "' " + ";";
		
		Cursor c = db.rawQuery(sql, null);
		
		return getResult(c);
			
	}

	public Intent getQuarterSum(String month) {
		// TODO Auto-generated method stub
		
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = null;
		
		int tag = Integer.parseInt(month) ;
		
		if(tag >=1 && tag <=3)
		{
			sql = "SELECT * FROM " + COUNT_TABLE + " WHERE " + "month>='"
						+ 1 + "' "+" AND "+ "month<= '"+3 + "';";
				
			
		}else if(tag >=4 && tag <=6) 
		{
			sql = "SELECT * FROM " + COUNT_TABLE + " WHERE " + "month>='"
					+ 4 + "' "+" AND "+ "month<= '"+6 + "';";
			
		}else if(tag >=7 && tag <=9)
		{
			sql = "SELECT * FROM " + COUNT_TABLE + " WHERE " + "month>='"
					+ 7 + "' "+" AND "+ "month<= '"+9 + "';";
		}else if(tag >=10 && tag <=12)
		{
			sql = "SELECT * FROM " + COUNT_TABLE + " WHERE " + "month>='"
					+ 10 + "' "+" AND "+ "month<= '"+12 + "';";	
		}
			
		Cursor c = db.rawQuery(sql, null);
		
		return getResult(c);

	}
	
	private Intent getResult(Cursor c)
	{
		float incomeSum = 0;
		int incomeCount = 0;
		float spendingSum = 0;
		int spendingCount = 0;
		
		
		if (c.getCount() != 0) 
		{
		
			while (c.moveToNext()) {
			
				String flag = c.getString(c.getColumnIndex(FLAG));
				
				if(flag.equals("0"))
				{
					incomeSum += c.getFloat(c.getColumnIndex(SUM));
					
					incomeCount+=1;
				}else if(flag.equals("1"))
				{
					spendingSum += c.getFloat(c.getColumnIndex(SUM));
					spendingCount +=1;
					
				}
			
			}

			Intent intent = new Intent();
			intent.putExtra("incomeSum", String.valueOf(incomeSum));
			intent.putExtra("incomeCount", String.valueOf(incomeCount));
			intent.putExtra("spendingSum", String.valueOf(spendingSum));
			intent.putExtra("spendingCount", String.valueOf(spendingCount));
			intent.putExtra("profit", String.valueOf(incomeSum-spendingSum));
		
			c.close();
			
			return intent;
		}
		else	
		{
			return null;
		}
	}

	public Intent getYearSum(String year) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "SELECT * FROM " + COUNT_TABLE + " WHERE " + "year='"
				+ year + "' " + ";";
		
		Cursor c = db.rawQuery(sql, null);
		
		return getResult(c);
	}

	public List<DataItem> getMonthDetail(String month) {
		
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "SELECT * FROM " + COUNT_TABLE + " WHERE " + "month='"
				+ month + "' " + ";";
		
		Cursor c = db.rawQuery(sql, null);
		
		
		return getDetailResult(c);
	}
	
	public List<DataItem> getQuarterDetail(String quarter)
	{
		
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = null;
		
		int tag = Integer.parseInt(quarter) ;
		
		if(tag >=1 && tag <=3)
		{
			sql = "SELECT * FROM " + COUNT_TABLE + " WHERE " + "month>='"
						+ 1 + "' "+" AND "+ "month<= '"+3 + "';";
				
			
		}else if(tag >=4 && tag <=6) 
		{
			sql = "SELECT * FROM " + COUNT_TABLE + " WHERE " + "month>='"
					+ 4 + "' "+" AND "+ "month<= '"+6 + "';";
			
		}else if(tag >=7 && tag <=9)
		{
			sql = "SELECT * FROM " + COUNT_TABLE + " WHERE " + "month>='"
					+ 7 + "' "+" AND "+ "month<= '"+9 + "';";
		}else if(tag >=10 && tag <=12)
		{
			sql = "SELECT * FROM " + COUNT_TABLE + " WHERE " + "month>='"
					+ 10 + "' "+" AND "+ "month<= '"+12 + "';";	
		}
		
		Cursor c = db.rawQuery(sql, null);
		
		return getDetailResult(c);
	}
	
	public List<DataItem > getYearDetail(String year)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "SELECT * FROM " + COUNT_TABLE + " WHERE " + "year='"
				+ year + "' " + ";";
		
		Cursor c = db.rawQuery(sql, null);
		
		return getDetailResult(c);
	}
	
	private List<DataItem > getDetailResult(Cursor c)
	{
		
		List<DataItem> list = new ArrayList<DataItem>();
		
		if (c.getCount() != 0) 
		{
		
			while (c.moveToNext()) {
			
				DataItem dataItem = new DataItem();
				
				dataItem.setYear(c.getInt(c.getColumnIndex(YEAR)));
				dataItem.setMonth(c.getInt(c.getColumnIndex(MONTH)));
				dataItem.setDay(c.getInt(c.getColumnIndex(DAY)));
				dataItem.setKind(c.getString(c.getColumnIndex(KIND)));
				dataItem.setNumber(c.getString(c.getColumnIndex(COUNT)));
				dataItem.setPrice(c.getString(c.getColumnIndex(PRICE)));
				dataItem.setUnit(c.getString(c.getColumnIndex(UNIT)));
				dataItem.setSum(c.getString(c.getColumnIndex(SUM)));
				dataItem.setFlag(c.getString(c.getColumnIndex(FLAG)));
		
				list.add(dataItem);
		
			}
		
			c.close();
			
			return list;
		}
		else	
		{
			return null;
		}

	}

	public void addNewKind(String kind, int flag) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();

		ContentValues values = new ContentValues();
		values.put(KIND, kind);
		values.put(FLAG,flag);
		
		db.insert(KIND_TABLE, null, values);

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public void removeKind(String kind) {
		// TODO Auto-generated method stub
		
	}

	public void updateKind(String newKind, String oldKind) {
		// TODO Auto-generated method stub
		
	}

	public List<String> getAllKind(int flag) {
		// TODO Auto-generated method stub
	
		List<String > list = new ArrayList<String>();
		
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "SELECT * FROM " + KIND_TABLE + " WHERE " + "flag='"
				+ flag + "' " + ";";
		
		Cursor c = db.rawQuery(sql, null);
		
		if (c.getCount() != 0) 
		{
		
			while (c.moveToNext()) {
			
				list.add(c.getString(c.getColumnIndex(KIND)));
			}
		}
		
		return list;
	}

	public void addNewUnit(String unit, int flag) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();

		ContentValues values = new ContentValues();
		values.put(UNIT, unit);
		values.put(FLAG,flag);
		
		db.insert(UNIT_TABLE, null, values);

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public void removeUnit(String unit) {
		// TODO Auto-generated method stub
		
	}

	public void updateUnit(String newUnit, String oldUnit) {
		// TODO Auto-generated method stub
		
	}

	public List<String> getAllUnit(int flag) {
		// TODO Auto-generated method stub
		
	List<String > list = new ArrayList<String>();
		
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "SELECT * FROM " + UNIT_TABLE + " WHERE " + "flag='"
				+ flag + "' " + ";";
		
		Cursor c = db.rawQuery(sql, null);
		
		if (c.getCount() != 0) 
		{
		
			while (c.moveToNext()) {
			
				list.add(c.getString(c.getColumnIndex(UNIT)));
			}
		}
		
		return list;
	}
	
	public List<DataItem> getTimeFragmentResult(long start,long end)
	{
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		String sql;
		
		if(start < end)
		{
			sql = "SELECT * FROM " + COUNT_TABLE + " WHERE " + "time>='"
				+ start + "' "+" AND "+ "time<= '"+end + "';";
		}
		else
		{
			sql = "SELECT * FROM " + COUNT_TABLE + " WHERE " + "time>='"
					+ end + "' "+" AND "+ "time<= '"+start + "';";
		}
		
		System.out.println(sql);
		
		
		Cursor c = db.rawQuery(sql, null);
		
		return getDetailResult(c);
	}
	
	
}
