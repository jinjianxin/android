package com.linux.monking;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.linux.adapter.DetailApdater;
import com.linux.database.DbManager;
import com.linux.utils.DataItem;

import android.app.Activity;
import android.content.Intent;
import android.net.http.SslCertificate.DName;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class DetailItemActivity extends Activity{

	private DbManager dbManager = null;
	private String flag = null; 
	private ListView detailListview = null;
	private DetailApdater adapter = null;
	
	private List<DataItem > dataList = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.detail_item_layout);
		
		
		detailListview = (ListView) findViewById(R.id.detail_list_view);
		
		dbManager = DbManager.getInstance(this);
		
		
		Intent intent = getIntent();
		flag = intent.getStringExtra("flag");
		
		if(flag.equals("first"))
		{	
			SimpleDateFormat formatter = new SimpleDateFormat ("MM");       
			Date curDate=new Date(System.currentTimeMillis());       
			String month = formatter.format(curDate);
			
			dataList = dbManager.getMonthDetail(month);
			
		}else if(flag.equals("second"))
		{
			SimpleDateFormat formatter = new SimpleDateFormat ("MM");       
			Date curDate=new Date(System.currentTimeMillis());       
			String  quarter= formatter.format(curDate);
			
			dataList = dbManager.getQuarterDetail(quarter);
			
		}else if(flag.equals("thrid"))
		{
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy");       
			Date curDate=new Date(System.currentTimeMillis());       
			String year = formatter.format(curDate);
			
			dataList = dbManager.getYearDetail(year);
		}
		
		adapter = new DetailApdater(this,dataList);
		
		detailListview.setAdapter(adapter);
		
		detailListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				if(dataList !=null && !dataList.isEmpty())
				{
					Intent intent = new Intent();
					intent.putExtra("flag", "details");
					intent.putExtra("dataItem", dataList.get(arg2));
					intent.setClass(DetailItemActivity.this, IncomeActivity.class);
					startActivity(intent);
				}
				
			}
			
			
		});
		
	}
	
}
