package com.example.taskmanager;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.TaskAdapter;
import com.example.database.DbManager;
import com.example.utils.LogHelp;
import com.example.utils.TaskData;
import com.example.view.PullView;
import com.example.view.PullView.OnPullDownListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements OnPullDownListener,
		OnItemClickListener {

	private static final int WHAT_DID_LOAD_DATA = 0;
	private static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;

	private ListView listView;

	private PullView mPullDownView = null;
	private TaskAdapter adpater = null;
	private DbManager dbManager = null;

	private List<TaskData> taskDataList = new ArrayList<TaskData>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		dbManager = DbManager.getInstance(getApplicationContext());
		dbManager.select();
		
		
		List<TaskData> list = dbManager.getData();
		
		if(list!=null)
		{
			LogHelp.logPutPut("size = "+list.size());
			taskDataList.addAll(list);
		}
		

		mPullDownView = (PullView) findViewById(R.id.pull_view);
		mPullDownView.setOnPullDownListener(this);
		listView = mPullDownView.getListView();

		listView.setOnItemClickListener(this);

		
		adpater = new TaskAdapter(getApplicationContext(), taskDataList);

		listView.setAdapter(adpater);

		mPullDownView.enableAutoFetchMore(true, 1);

		mPullDownView.notifyDidLoad();

	}
		
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
		if(listView.getAdapter().getCount()<10)
		{
			List<TaskData> tmpList = dbManager.getTmpList();
			if(tmpList!=null)
			{
				//taskDataList.add(data);
				taskDataList.addAll(tmpList);
				
			}
			
			dbManager.resetTmpList();
		}
	
		adpater.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = mUIHandler.obtainMessage(WHAT_DID_REFRESH);
				msg.obj = "After refresh " + System.currentTimeMillis();
				msg.sendToTarget();
			}
		}).start();
	}

	@Override
	public void onMore() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
				msg.obj = "After more " + System.currentTimeMillis();
				msg.sendToTarget();
			}
		}).start();
	}

	private Handler mUIHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA: {
				mPullDownView.notifyDidLoad();
				break;
			}
			case WHAT_DID_REFRESH: {
				break;
			}

			case WHAT_DID_MORE: {
				
				/*
				TaskData data = new TaskData("test",0,"test","0","0");
				taskDataList.add(data);
				adpater.notifyDataSetChanged();*/
				
		
				List<TaskData> tmpList = dbManager.getTmpList();
				if (tmpList != null) {
					// taskDataList.add(data);
					taskDataList.addAll(tmpList);

				}

				dbManager.resetTmpList();

				adpater.notifyDataSetChanged();

				mPullDownView.notifyDidMore();
				break;
			}
			}
		}

	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		
		TaskData taskData = taskDataList.get(position);
		
		if(taskData!=null)
		{
			Intent intent = new Intent();
			intent.setClass(MainActivity.this,	AddTask.class);
			intent.putExtra("flag", "task");
			intent.putExtra("taskData", taskData);
			startActivity(intent);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "Ìí¼Ó").setIcon(

		android.R.drawable.ic_menu_add);

		
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		
		if(featureId ==0)
		{
			Intent intent = new Intent();
			intent.setClass(MainActivity.this,	AddTask.class);
			intent.putExtra("flag", "income");
			startActivity(intent);
		}
		
		
		return super.onMenuItemSelected(featureId, item);
	}

}
