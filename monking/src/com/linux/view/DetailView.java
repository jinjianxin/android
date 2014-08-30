package com.linux.view;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import com.linux.adapter.DetailApdater;
import com.linux.database.DbManager;
import com.linux.monking.DetailItemActivity;
import com.linux.monking.IncomeActivity;
import com.linux.monking.R;
import com.linux.utils.DataItem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class DetailView {
	
	private Context context = null;
	
	private DbManager dbManager = null;
	private String flag = null; 
	private ListView datailListview = null;
	private DetailApdater adapter = null;
	
	private List<DataItem > dataList = null;
	
	public DetailView(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		dbManager = DbManager.getInstance(context);
	}
	
	public View makeDetailView(LayoutInflater inflater,String flag)
	{
		this.flag = flag;
		
		View view = inflater.inflate(R.layout.detail_item_layout, null);
			
		if(flag.equals("month"))
		{	
			SimpleDateFormat formatter = new SimpleDateFormat ("MM");       
			Date curDate=new Date(System.currentTimeMillis());       
			String month = formatter.format(curDate);
			
			dataList = dbManager.getMonthDetail(month);
			
		}else if(flag.equals("quarter"))
		{
			SimpleDateFormat formatter = new SimpleDateFormat ("MM");       
			Date curDate=new Date(System.currentTimeMillis());       
			String  quarter= formatter.format(curDate);
			
			dataList = dbManager.getQuarterDetail(quarter);
			
		}else if(flag.equals("year"))
		{
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy");       
			Date curDate=new Date(System.currentTimeMillis());       
			String year = formatter.format(curDate);
			
			dataList = dbManager.getYearDetail(year);
		}
		
		
		datailListview = (ListView) view.findViewById(R.id.detail_list_view);
		
		adapter = new DetailApdater(context,dataList);
		
		datailListview.setAdapter(adapter);
		
		datailListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				if(dataList !=null && !dataList.isEmpty())
				{
					Intent intent = new Intent();
					intent.putExtra("flag", "details");
					intent.putExtra("dataItem", dataList.get(arg2));
					intent.setClass(context, IncomeActivity.class);
					//startActivity(intent);
					context.startActivity(intent);
				}
				
			}
			
			
		});
		
		return view;
	}

}
