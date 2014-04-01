package com.asianux.musicplayer;

import java.util.List;
import java.util.Locale;

import com.asianux.adapter.SettingAdapter;
import com.asianux.database.DbManager;
import com.asianux.musicplayer.MusicFragment.MusicFragmentLinster;
import com.asianux.musicplayer.MusicPlayFragment.MusicPlayFragmentListener;
import com.asianux.service.BaiduDownloadService;
import com.asianux.service.LrcParserService;
import com.asianux.service.MusicPlayer;
import com.asianux.service.ServicePlayer;
import com.asianux.utils.LrcInfo;
import com.asianux.utils.LrcItem;
import com.asianux.utils.MediaUtils;
import com.asianux.utils.Mp3Info;
import com.asianux.musicplayer.MusicViewGroup.InterceptListener;

import android.R.bool;
import android.app.Activity;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.GpsStatus.NmeaListener;
import android.media.audiofx.Visualizer;
import android.media.audiofx.Visualizer.OnDataCaptureListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.ContextMenu;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RemoteViews;

public class MainActivity extends Activity {
	
	private	MusicViewGroup musicViewGroup = null;
	private boolean settinIsOpen = false;
	private boolean bottomIsOpen = false;
	private ListView setting_list = null;
	private SettingAdapter settingAdapter = null;
	private ServicePlayer servicePlayer = null;
	private MusicFragment musicFragment = null;
	private MusicPlayFragment musicPlayFragment = null;
	private MediaUtils mediaUtils = null;
	private MyReceiver myReceiver = null;
	private LrcInfo lrcInfo = null;
	private static int handlerTime = 300;
	private final String MYRECEIVER = "Music.Player.MUSIC_SERVICE_BROADCAST";
	private final String MUSICLRC = "Music.Player.LRC_BROADCAST";
	private final String MUSICLRCINFO = "Music.Player.LRC_INFO_BROADCAST";
	private Visualizer mVisualizer = null;
	
	private static final String PREV = "prev";
	private static final String PAUSE = "pause";
	private static final String NEXT = "next";
	private static final int NOTIFY_ID = 1;
	private RemoteViews remoteView = null;
	private NotificationManager mnotiManager = null;
	private Notification noti  = null;

	private String lrcPath = null;
	
	private Handler myHandler = new Handler();
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			servicePlayer = ServicePlayer.Stub.asInterface(service);
					
			try {
				if(servicePlayer.isPlaying())
				{
					musicFragment.setPlayStatus(true);
					
					myHandler.postDelayed(myRunnable, handlerTime);
					setMp3Info();
	
					loadLrc();
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	};

	private Runnable myRunnable = new Runnable() {

		@Override
		public void run() {

			if (servicePlayer != null) {
				
				try {
					musicFragment.setTime(formatTime(servicePlayer.getCurrentTime()));
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// TODO Auto-generated method stub

				try {
					int currentTime = servicePlayer.getCurrentTime();
					musicPlayFragment.setMaxSeekBar(servicePlayer.getDuration());
					musicPlayFragment.setSeekProgress(currentTime);
					
					if(lrcInfo != null && !lrcInfo.isEmpty())
					{			
						int index = lrcInfo.find(currentTime);
						
						musicPlayFragment.setLrcIndex(index);
						musicPlayFragment.setLrcText(lrcInfo.getLrc(index));
					}
			
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			myHandler.postDelayed(myRunnable, handlerTime);

		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		setContentView(R.layout.my_view_group);
		
		mediaUtils = MediaUtils.getInstance(MainActivity.this);
		
		musicViewGroup = (MusicViewGroup) findViewById(R.id.mySlideMenu);	
		
		musicFragment = new MusicFragment(new MusicFragmentLinster() {
			
			@Override
			public void play() {
				// TODO Auto-generated method stub
				MainActivity.this.play();
			}
			
			@Override
			public void pause() {
				// TODO Auto-generated method stub
				MainActivity.this.pasue();
			}
			
			
			@Override
			public void next() {
				// TODO Auto-generated method stub
				playNext();
			}

			@Override
			public void to_playui() {
				// TODO Auto-generated method stub
				
				musicViewGroup.openSlideBottom();
				bottomIsOpen = true;
				
			}

		});
		getFragmentManager().beginTransaction().replace(R.id.my_fragment, musicFragment).commit();
		
		musicPlayFragment = new MusicPlayFragment(
				new MusicPlayFragmentListener() {

					@Override
					public void setSeekBarProgress(int currentTime) {
						// TODO Auto-generated method stub

						if (servicePlayer != null) {
							try {
								servicePlayer.play();
								servicePlayer.setCurrent(currentTime);
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}

					@Override
					public void prev() {
						// TODO Auto-generated method stub

						playPrev();

					}

					@Override
					public void play() {
						// TODO Auto-generated method stub

						play();

					}

					@Override
					public void pause() {
						// TODO Auto-generated method stub

						MainActivity.this.pasue();

					}

					@Override
					public void next() {
						// TODO Auto-generated method stub
						playNext();
					}
				});
		getFragmentManager().beginTransaction().replace(R.id.my_fragment_bootom, musicPlayFragment).commit();
		
		setting_list = (ListView) findViewById(R.id.music_setting_list);
		
		//musicFragment = findViewById(R.id.myfragment);
		
		settingAdapter = new SettingAdapter(MainActivity.this);
		
		setting_list.setAdapter(settingAdapter);
				
		setting_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				settinIsOpen=false;
				musicViewGroup.closeSlide();
			
				if(arg2 ==0)
				{
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, ScanMusicActiviry.class);
					MainActivity.this.startActivity(intent);
				}
				
			}
		});
		
		Intent playIntent = new Intent();
		playIntent.setClass(MainActivity.this, MusicPlayer.class);
		bindService(playIntent, mServiceConnection, TRIM_MEMORY_BACKGROUND);
		startService(playIntent);

		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MYRECEIVER);
		filter.addAction(MUSICLRCINFO);
		filter.addAction(MUSICLRC);
		filter.addAction(PREV);
		filter.addAction(PAUSE);
		filter.addAction(NEXT);
		
		MainActivity.this.registerReceiver(myReceiver, filter);
		
		musicViewGroup.setListener(new InterceptListener() {

			@Override
			public void open() {
				// TODO Auto-generated method stub
			
				if (!settinIsOpen) {
					settinIsOpen = true;
					musicViewGroup.openSlide();
				}		
			}

			@Override
			public void close() {
				// TODO Auto-generated method stub
				if(settinIsOpen)
				{
					settinIsOpen=false;
					musicViewGroup.closeSlide();
				}	
			}

			@Override
			public void open_bottom() {
				// TODO Auto-generated method stub
				
				if(!bottomIsOpen)
				{
					musicViewGroup.openSlideBottom();
					bottomIsOpen = true;
				}
				
			}

			@Override
			public void close_bottom() {
				// TODO Auto-generated method stub
				if(bottomIsOpen)
				{
					musicViewGroup.closeSlideBottom();
					bottomIsOpen = false;
				}
				
			}
			
		});
		
	}
	

	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			// TODO Auto-generated method stub

			String action = intent.getAction();

			if (action.equals(MYRECEIVER)) {
							
				setMp3Info();
					
				loadLrc();

			} else if (action.equals(MUSICLRC)) {
				
				Bundle bundle = intent.getExtras();
				lrcPath = null;
			    lrcPath = bundle.getString("lrcPath");
			    				
				Intent parserIntent = new Intent();
				parserIntent.putExtra("lrcPath", lrcPath);
				parserIntent.setClass(MainActivity.this, LrcParserService.class);
				startService(parserIntent);
				
			}
			else if(action.equals(MUSICLRCINFO))
			{
				String lrc = null;
				
				lrcInfo = null;
				lrcInfo = (LrcInfo) intent.getSerializableExtra("lrcInfo");
				
				if(lrcInfo !=null)
				{
					musicPlayFragment.setMusicPlayInfo(lrcInfo);
				}
		
			}else if(action.equals(PREV))
			{
				System.out.println("-----------------prev");
				
				playPrev();
				
			}else if(action.equals(PAUSE))
			{
			
				System.out.println("-----------------play");
				
				MainActivity.this.pasue();
			
				
			}else if(action.equals(NEXT))
			{
				System.out.println("-----------------next");	
				playNext();
			}
		}

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if(servicePlayer !=null)
		{
			try {
				if(servicePlayer.isPlaying())
				{
					myHandler.post(myRunnable);
					
					musicPlayFragment.setPlayStatus(true);
					
					if(mVisualizer == null)
					{
						initSpectrum();
					}
					
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		musicFragment.initView();
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindService(mServiceConnection);
		unregisterReceiver(myReceiver);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			
		/*	if(!settinIsOpen)
			{
				settinIsOpen=true;
				musicViewGroup.openSlide();
			}
			else
			{
				settinIsOpen=false;
				musicViewGroup.closeSlide();
			}	*/
			return true;
		}else if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			Intent intent = new Intent(); 
			intent.setAction(Intent.ACTION_MAIN); 
			intent.addCategory(Intent.CATEGORY_HOME);           
			startActivity(intent);
			return true;
		}
		else
		{
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub	

		if(settinIsOpen)
		{
			settinIsOpen=false;
			musicViewGroup.closeSlide();
		}else if(bottomIsOpen)
		{
			bottomIsOpen = false;
			musicViewGroup.closeSlideBottom();
		}
		else
		{
			super.onBackPressed();
			finish();
		}
	}
	
	/**
	 * setMp3Info 设置toolbar上的mp3信息
	 * (这里描述这个方法适用条件 – 可选) 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	private void setMp3Info()
	{
		
		if(servicePlayer !=null)
		{
			try {
				
				if(servicePlayer.isPlaying())
				{
					musicFragment.setPlayStatus(true);
				}
				else
				{
					musicFragment.setPlayStatus(false);
				}
				
				List<String> list = servicePlayer.getMp3info();
				
				if(list !=null && !list.isEmpty())
				{
					musicFragment.setMp3Info(list);
					
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
	
	private String formatTime(long time)
	{
		String min = time / (1000 * 60) + "";
		String sec = time % (1000 * 60) + "";
		if (min.length() < 2) {
			min = "0" + time / (1000 * 60) + "";
		} else {
			min = time / (1000 * 60) + "";
		}
		if (sec.length() == 4) {
			sec = "0" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 3) {
			sec = "00" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 2) {
			sec = "000" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 1) {
			sec = "0000" + (time % (1000 * 60)) + "";
		}
		
		return min + ":" + sec.trim().substring(0, 2);
	}

	private void loadLrc()
	{
		
		String url;
		try {
			url = servicePlayer.getPlayingUrl();
			
			if(url != null&& !url.isEmpty())
			{
				DbManager dbManager = DbManager.getInstance(MainActivity.this);
				
				Mp3Info mp3info = mediaUtils.getMp3infoWithKey(url);

				lrcPath = dbManager.select(mp3info);

				if (lrcPath == null) {		
									
					Intent dlIntent = new Intent();
					dlIntent.putExtra("mp3info", mp3info);
					dlIntent.setClass(MainActivity.this, BaiduDownloadService.class);
					startService(dlIntent);
				} else {
					Intent parserIntent = new Intent();
					System.out.println("lrcPath = "+lrcPath);
					parserIntent.putExtra("lrcPath", lrcPath);
					parserIntent.setClass(MainActivity.this, LrcParserService.class);
					startService(parserIntent);

				}
			}
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 初始化频谱相关
	 */
	private void initSpectrum()
	{
		try {

			final int maxCR = Visualizer.getMaxCaptureRate();
			
			mVisualizer = new Visualizer(servicePlayer.getAudioSessionId());

			mVisualizer.setCaptureSize(256);
			mVisualizer.setDataCaptureListener(new OnDataCaptureListener() {

				@Override
				public void onWaveFormDataCapture(Visualizer visualizer,
						byte[] waveform, int samplingRate) {

					MainActivity.this.musicPlayFragment.updateVisualizer(waveform);
				}

				@Override
				public void onFftDataCapture(Visualizer visualizer, byte[] fft,
						int samplingRate) {
					MainActivity.this.musicPlayFragment.updateVisualizer(fft);
				}
			}, maxCR/2, false, true);
			
			mVisualizer.setEnabled(true);
			
		} catch (UnsupportedOperationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (RuntimeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
        prePendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,prevIntent,0);
        
        remoteView.setOnClickPendingIntent(R.id.prev, prePendingIntent);
        
        Intent playIntent = new Intent(PAUSE);
        PendingIntent playPendingIntent = null;
        playPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, playIntent, 0);
        
        remoteView.setOnClickPendingIntent(R.id.play,playPendingIntent);
        
        Intent nextIntent = new Intent(NEXT);
        PendingIntent nextPendingIntent = null;
        nextPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, nextIntent,0);
        
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
	
	private void playPrev()
	{
		if(servicePlayer !=null)
		{
			try {
				servicePlayer.previous();
				loadLrc();
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void playNext()
	{
		if(servicePlayer != null)
		{
			try {
				servicePlayer.next();
				
				loadLrc();
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void play()
	{
		if(servicePlayer !=null)
		{
			try {
				servicePlayer.play();
				
				myHandler.post(myRunnable);
				
				showNotification();
				
				loadLrc();

				if(mVisualizer == null)
				{
					initSpectrum();
				}
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		setMp3Info();
	}
	
	private void pasue()
	{
		if(servicePlayer !=null)
		{
			try {
				servicePlayer.pause();
				
				myHandler.removeCallbacks(myRunnable);
				
				closeNotification();
				
				musicFragment.setPlayStatus(false);
				musicPlayFragment.setPlayStatus(false);
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}