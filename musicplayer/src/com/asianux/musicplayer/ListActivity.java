package com.asianux.musicplayer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.asianux.service.BaiduDownloadService;
import com.asianux.service.GecimiDownloadService;
import com.asianux.service.MusicPlayer;
import com.asianux.service.ServicePlayer;
import com.asianux.utils.*;
import com.asianux.adapter.ListAdapter;
import com.asianux.database.DbManager;
import com.asianux.dialog.AddDialog;
import com.asianux.dialog.AddDialog.AddDialogListener;
import com.asianux.dialog.RemoveDialog;
import com.asianux.dialog.RemoveDialog.RemoveDialogListener;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ListActivity extends Activity {

	private ListView listView = null;
	private List<String > list = null;

	private DbManager dbManager = null;
	private MediaUtils mediaUtils = null;
	private final String MYRECEIVER = "Music.Player.MUSIC_SERVICE_BROADCAST";
	private final String MUSICLRC = "Music.Player.LRC_BROADCAST";
	private String arg = null; //用来区分在那个列表下

	private ImageButton albumButton = null;
	private Button playButton = null;
	private Button nextButton = null;
	private SeekBar seekBar = null;
	private int position = 0;
	private MyReceiver myReceiver = null;
	private ListAdapter listAdapter = null;
	
	private final static String ALLMUSIC = "allmusic";
	private final static String COLLECTMUSIC = "collectmusic";
	private final static String CUSTOMLIST = "custom";
	private final static String SINGERMUSIC = "singer";
	private final static String ALBUMMUSIC = "album";
	
	/**
	 * 表明处于那种模式
	 */
	private int ALL = 0;
	private int SINGER = 1;
	private int ALBUM = 2;
	private int PLAYLIST = 3;
	private int COLLECT = 4;
	
	private int viewTag = 0;

	private ServicePlayer servicePlayer = null;
	
	/**
	 * 系统提示组件
	 */
	private static final String PREV = "prev";
	private static final String PAUSE = "pause";
	private static final String NEXT = "next";
	private static final int NOTIFY_ID = 2;
	private RemoteViews remoteView = null;
	private NotificationManager mnotiManager = null;
	private Notification noti  = null;

	private Handler myHandler = new Handler();
	

	private Runnable myRunnable = new Runnable() {

		@Override
		public void run() {

			if (servicePlayer != null) {
				// TODO Auto-generated method stub
				try {
					seekBar.setProgress(servicePlayer.getCurrentTime());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			myHandler.postDelayed(myRunnable, 500);

		}
	};

	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			// playService = ((PlayService.MyBinder) service).getService();
			servicePlayer = ServicePlayer.Stub.asInterface(service);
			
			if(servicePlayer !=null)
			{
				try {
					if(servicePlayer.isPlaying())
					{
						seekBar.setMax(servicePlayer.getDuration());
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listlayout);
		
		initView();
		
		Intent playIntent = new Intent();
		playIntent.setClass(ListActivity.this, MusicPlayer.class);
		bindService(playIntent, mServiceConnection, TRIM_MEMORY_BACKGROUND);
		startService(playIntent);

	 	myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MYRECEIVER);

		registerReceiver(myReceiver, filter);

		myHandler.post(myRunnable);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		initView();
	}
	
	private void initView()
	{
		albumButton = (ImageButton) findViewById(R.id.albumButton);
		seekBar = (SeekBar) findViewById(R.id.seekBar);

		playButton = (Button) findViewById(R.id.playButton);
		nextButton = (Button) findViewById(R.id.nextButton);
		listView = (ListView) findViewById(R.id.listView);

		Intent intent = this.getIntent();

		String tag = intent.getStringExtra("tag");

		mediaUtils = MediaUtils.getInstance(ListActivity.this);
		dbManager = DbManager.getInstance(ListActivity.this);

		if (tag.equals(ALLMUSIC)) {
			viewTag = ALL;

			arg = ALLMUSIC;
			list = mediaUtils.getMp3infoList();
			
			//System.out.println(" key list size = "+list.size());
			
			listAdapter = new ListAdapter(this, list);
			listAdapter.notifyDataSetChanged();

			listView.setAdapter(listAdapter);

		} else if (tag.equals(SINGERMUSIC)) {
			viewTag = SINGER;
			arg = intent.getStringExtra("arg");

			list = mediaUtils.getSingerList(arg);

			if (list != null && !list.isEmpty()) {
				listAdapter = new ListAdapter(this, list);
				listAdapter.notifyDataSetChanged();

				listView.setAdapter(listAdapter);
			}

		} else if (tag.equals(ALBUMMUSIC)) {
			viewTag = ALBUM;
			arg = intent.getStringExtra("arg");

			list = mediaUtils.getAlbumList(arg);

			if (list != null && !list.isEmpty()) {
				listAdapter = new ListAdapter(this, list);
				listAdapter.notifyDataSetChanged();

				listView.setAdapter(listAdapter);
			}
		} else if (tag.equals(COLLECTMUSIC)) {
			viewTag = COLLECT;

			list = dbManager.getCollectList();

			if (list != null && !list.isEmpty()) {

				listAdapter = new ListAdapter(this, list, 0);
				listAdapter.notifyDataSetChanged();

				listView.setAdapter(listAdapter);
			}
		} else if (tag.equals(CUSTOMLIST)) {
			viewTag = PLAYLIST;

			arg = intent.getStringExtra("listname");

			list = dbManager.getListWithName(arg);

			if (list != null && !list.isEmpty()) {

				listAdapter = new ListAdapter(this, list);
				listAdapter.notifyDataSetChanged();

				listView.setAdapter(listAdapter);
			}

		}

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (servicePlayer == null) {
					Intent intent = new Intent();
					intent.putExtra("mp3id", arg2);
					intent.putExtra("viewTag", viewTag);
					intent.putExtra("arg", arg);
					intent.setClass(ListActivity.this, MusicPlayer.class);
					bindService(intent, mServiceConnection,
							TRIM_MEMORY_BACKGROUND);
					startService(intent);

					myHandler.post(myRunnable);

					playButton
							.setBackgroundResource(R.drawable.btn_play_normal);

					position = arg2;
					
					showNotification();
					
				} else {
					Intent intent = new Intent();
					intent.putExtra("mp3id", arg2);
					intent.putExtra("viewTag", viewTag);
					intent.putExtra("arg", arg);
					intent.setClass(ListActivity.this, MusicPlayer.class);
					bindService(intent, mServiceConnection,
							TRIM_MEMORY_BACKGROUND);
					startService(intent);
					position = arg2;
				
					playButton
							.setBackgroundResource(R.drawable.btn_play_normal);
					
					showNotification();
				}

				// TODO Auto-generated method stub
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				// Mp3Info mp3info = list.get(arg2);

				Mp3Info mp3info = mediaUtils.getMp3infoWithKey(list.get(arg2));
				if (mp3info != null) {
					AddDialog addDialog = new AddDialog(ListActivity.this,
							mp3info, new AddDialogListener() {

								@Override
								public void removedialog(Mp3Info mp3Info) {
									// TODO Auto-generated method stub
									launchRemoveDialog(mp3Info);
								}

							});
					addDialog.show();
				}

				return false;
			}
		});

		albumButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (servicePlayer != null) {
					try {
						if (servicePlayer.isPlaying()) {

							String str = servicePlayer.getPlayingInfo();

							if (str != null) {
								String[] array = str.split("\\|");

								position = Integer.parseInt(array[0]);
								viewTag = Integer.parseInt(array[1]);

								if (viewTag == 0) {
									arg = ALLMUSIC;
								} else if (viewTag == 1) {
									arg = array[2];
								} else if (viewTag == 2) {
									arg = array[2];
								} else if (viewTag == 3) {
									arg = array[2];
								} else if (viewTag == 4) {
									arg = array[2];
								}

								System.out.println(viewTag);

								Intent intent = new Intent();
								intent.putExtra("mp3id", position);
								intent.putExtra("viewTag", viewTag);
								intent.putExtra("arg", arg);
								intent.setClass(ListActivity.this,
										PlayView.class);
								ListActivity.this.startActivity(intent);
							}

						} else {
							Intent intent = new Intent();
							intent.putExtra("mp3id", position);
							intent.putExtra("viewTag", viewTag);
							intent.putExtra("arg", arg);
							intent.setClass(ListActivity.this, PlayView.class);
							ListActivity.this.startActivity(intent);
						}
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

				if (servicePlayer != null) {
					try {
						servicePlayer.setCurrent(seekBar.getProgress());
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub

			}
		});

		playButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					if (servicePlayer != null) {
						try {
							if (servicePlayer.isPlaying()) {
								playButton
										.setBackgroundResource(R.drawable.btn_play_pressed);
							} else {
								playButton
										.setBackgroundResource(R.drawable.pause_pressed);
							}
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if (servicePlayer != null) {

						try {
							if (servicePlayer.isPlaying()) {
								playButton
										.setBackgroundResource(R.drawable.pause_normal);
								servicePlayer.pause();
								
								closeNotification();

								myHandler.removeCallbacks(myRunnable);
							} else {
								playButton
										.setBackgroundResource(R.drawable.btn_play_normal);

								servicePlayer.play();
								
								showNotification();
								

								myHandler.postDelayed(myRunnable, 500);
							}
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}

				return false;
			}
		});

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (servicePlayer != null) {
					try {
						servicePlayer.next();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	
	private void showNotification()
	{
		CharSequence title = "音乐播放器";  
        int icon = R.drawable.album_128;  
        long when = System.currentTimeMillis();  
        noti = new Notification(icon, title, when);
    
        noti.flags = Notification.FLAG_NO_CLEAR;  
          
        remoteView = new RemoteViews(this.getPackageName(),R.layout.notification); 
        
        noti.contentView = remoteView;  
        
        Intent prevIntent = new Intent(PREV);
        PendingIntent prePendingIntent = null;
        prePendingIntent = PendingIntent.getBroadcast(ListActivity.this , 0,prevIntent,0);
        
        remoteView.setOnClickPendingIntent(R.id.prev, prePendingIntent);
        
        Intent playIntent = new Intent(PAUSE);
        PendingIntent playPendingIntent = null;
        playPendingIntent = PendingIntent.getBroadcast(ListActivity.this, 0, playIntent, 0);
        
        remoteView.setOnClickPendingIntent(R.id.play,playPendingIntent);
        
        Intent nextIntent = new Intent(NEXT);
        PendingIntent nextPendingIntent = null;
        nextPendingIntent = PendingIntent.getBroadcast(ListActivity.this, 0, nextIntent,0);
        
        remoteView.setOnClickPendingIntent(R.id.next, nextPendingIntent);
        
        mnotiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
        mnotiManager.notify(NOTIFY_ID, noti);  
        
	}
	
	private void closeNotification()
	{
		 NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
         
	     mNotificationManager.cancel(NOTIFY_ID);
	     remoteView = null;
	}
	

	private void launchRemoveDialog(Mp3Info mp3Info) {
		// TODO Auto-generated method stub
		RemoveDialog dialog = new RemoveDialog(ListActivity.this,mp3Info,new RemoveDialogListener() {
			
			@Override
			public void removeWithSource(Mp3Info mp3Info) {
				// TODO Auto-generated method stub	
				removeListViewItem(mp3Info);		
			}
			
			@Override
			public void removeWithList(Mp3Info mp3Info) {
				// TODO Auto-generated method stub
				removeListViewItem(mp3Info);
			}
		});
		dialog.show();
	}
	
	/**
	 * 
	 * removeListViewItemv从listeview中删除item
	 * (这里描述这个方法适用条件 – 可选)
	 * @param mp3Info 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	private void removeListViewItem(Mp3Info mp3Info)
	{
		if(listAdapter !=null)
		{
			listAdapter.remoeveItem(mp3Info.getUrl());
			listAdapter.notifyDataSetChanged();
			
		}
		
		if(viewTag == ALL)
		{
			mediaUtils.removeMp3infoItem(mp3Info.getUrl());
		}
		else if(viewTag == SINGER)
		{
			mediaUtils.removeSingerList(mp3Info);
		}else if(viewTag == ALBUM)
		{
			mediaUtils.removeAlbumList(mp3Info);
		}else if(viewTag == PLAYLIST)
		{
			dbManager.removePlayListDb(arg, mp3Info.getUrl());
			
		}else if(viewTag == COLLECT)
		{
			dbManager.remoevCollect(mp3Info);
		}
		
	}
	
	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			String action = intent.getAction();

			if (action.equals(MYRECEIVER)) {

				Bundle bundle = intent.getExtras();
				int position = bundle.getInt("postion");
				int during = bundle.getInt("during");

				seekBar.setMax(during);
				
				setMp3Info();
				
			} else if (action.equals(MUSICLRC)) {
				
			}
		}

	}

	
	private void setMp3Info()
	{
		
		if(servicePlayer !=null)
		{
			try {
				

				List<String> list = servicePlayer.getMp3info();
				
				if(list !=null && !list.isEmpty())
				{	
					if(remoteView !=null)
					{
						remoteView.setTextViewText(R.id.music_name, list.get(0));
						remoteView.setTextViewText(R.id.singer_name, list.get(1));
						
						mnotiManager.notify(NOTIFY_ID, noti);
					}
					else 
					{
					}
					
				}
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();	
		unbindService(mServiceConnection);
		ListActivity.this.unregisterReceiver(myReceiver);
		
		myHandler.removeCallbacks(myRunnable);
	}

}
