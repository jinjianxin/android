package com.linux.monking;

import java.util.List;

import com.linux.adapter.DetailApdater;
import com.linux.database.DbManager;
import com.linux.utils.DataItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends Activity {
	
	private TextView textView = null;
	private ListView listView = null;
	private DbManager dbManager = null;
	private DetailApdater adapter = null;
	private List<DataItem> list = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_layout);
		
		dbManager = DbManager.getInstance(this);
		
		textView = (TextView) findViewById(R.id.search_title_view);
		listView = (ListView) findViewById(R.id.search_list_view);
		
		textView.setText("搜索结果");
		
		Intent intent = getIntent();
		
		long startTime = intent.getLongExtra("startTime", 0);
		long endTime = intent.getLongExtra("endTime", 0);

		list = dbManager.getTimeFragmentResult(startTime, endTime);

		if(list !=null)
		{
			adapter = new DetailApdater(this,list);
			
			listView.setAdapter(adapter);
			
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					
					if(list !=null && !list.isEmpty())
					{
						Intent intent = new Intent();
						intent.putExtra("flag", "details");
						intent.putExtra("dataItem", list.get(arg2));
						intent.setClass(SearchActivity.this, IncomeActivity.class);
						//startActivity(intent);
						startActivity(intent);
					}
					
				}
				
				
			});
		}
		
		
	}

}
