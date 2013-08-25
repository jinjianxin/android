package com.asianux.musicplayer;

import java.util.List;

import com.asianux.utils.*;
import com.asianux.adapter.ListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

public class ListActivity extends Activity{
	
	private ListView listView = null;
	private List<Mp3Info > list = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listlayout);
		
		
		list = MediaUtils.getMp3Info(ListActivity.this);
		
		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(new ListAdapter(this, list));
		
		System.out.println("**********************************\n");
		
		listView.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				System.out.println("======================================\n");
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				System.out.println("======================================\n");
				
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				System.out.println("arg2 =" +arg2);
				
				Mp3Info mp3Info = list.get(arg2);
				System.out.println(mp3Info.toString()+"\n");
				
				Intent intent = new Intent();
				intent.putExtra("mp3info", mp3Info);
				intent.setClass(ListActivity.this, PlayActivity.class);
				ListActivity.this.startActivity(intent);
				// TODO Auto-generated method stub
	
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}

}
