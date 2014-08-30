package com.linux.database;

import java.util.ArrayList;
import java.util.List;

import com.linux.utils.DataItem;

import android.content.Context;
import android.content.Intent;

public class DbManager {

	private volatile static DbManager dbManager = null;
	private MonkingDatabase monkingDatabase = null;

	public static DbManager getInstance(Context context ) {

		synchronized (MonkingDatabase.class) {
			if (dbManager == null) {
				
				dbManager = new DbManager(context);
			}
		}

		return dbManager;
	}
	
	public DbManager(Context context) {
	
		monkingDatabase = new MonkingDatabase(context);
		monkingDatabase.select();
	}
	
	public void insertDataItem(DataItem item)
	{
		monkingDatabase.insertDataItem(item);
	}
	
	public Intent getMonthSum(String month) {
		// TODO Auto-generated method stub
		
		return monkingDatabase.getMonthSum(month);
	}

	public Intent getQuarterSum(String month) {
		// TODO Auto-generated method stub
		return  monkingDatabase.getQuarterSum(month);
	}

	public Intent getYearSum(String year) {
		// TODO Auto-generated method stub
		return monkingDatabase.getYearSum(year);
	}

	public List<DataItem> getMonthDetail(String month) {
		// TODO Auto-generated method stub
		return monkingDatabase.getMonthDetail(month);
	}
	
	public List<DataItem > getQuarterDetail(String quarter)
	{
		return monkingDatabase.getQuarterDetail(quarter);
	}
	
	public List<DataItem > getYearDetail(String year)
	{
		return monkingDatabase.getYearDetail(year);
	}
	
	public void addNewKind(String kind,int flag)
	{
		monkingDatabase.addNewKind(kind,flag);
	}
	
	public void removeKind(String kind)
	{
		monkingDatabase.removeKind(kind);
	}
	
	public void updateKind(String newKind,String oldKind)
	{
		monkingDatabase.updateKind(newKind,oldKind);
	}
	
	public List<String> getAllKind(int flag)
	{
		return monkingDatabase.getAllKind(flag);
	}
	
	public void addNewUnit(String unit,int flag)
	{
		monkingDatabase.addNewUnit(unit,flag);
	}
	
	public void removeUnit(String unit)
	{
		monkingDatabase.removeUnit(unit);
	}
	
	public void updateUnit(String newUnit,String oldUnit)
	{
		monkingDatabase.updateUnit(newUnit,oldUnit);
	}
	
	public List<String> getAllUnit(int flag)
	{
		return monkingDatabase.getAllUnit(flag);
	}
	
	public List<DataItem> getTimeFragmentResult(long start,long end)
	{
		return monkingDatabase.getTimeFragmentResult(start, end);
	}
	
}

