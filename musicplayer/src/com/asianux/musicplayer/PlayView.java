package com.asianux.musicplayer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.asianux.adapter.ListAdapter;
import com.asianux.database.DbManager;
import com.asianux.service.BaiduDownloadService;
import com.asianux.service.LrcParserService;
import com.asianux.service.MusicPlayer;
import com.asianux.service.ServicePlayer;
import com.asianux.utils.LrcInfo;
import com.asianux.utils.LrcItem;
import com.asianux.utils.MediaUtils;
import com.asianux.utils.Mp3Info;

import android.R.menu;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.audiofx.Visualizer;
import android.media.audiofx.Visualizer.OnDataCaptureListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.style.LeadingMarginSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class PlayView extends Activity {

	private final String MYRECEIVER = "Music.Player.MUSIC_SERVICE_BROADCAST";
	private final String MUSICLRC = "Music.Player.LRC_BROADCAST";
	private final String MUSICLRCINFO = "Music.Player.LRC_INFO_BROADCAST";
	private static final int playNormal = 0;
	private static final int playCycle = 1;
	private static final int allCycle = 2;
	private static final int playRandom = 3;
	private static int handlerTime = 300;
	private Visualizer mVisualizer = null;

	private int mode = 0;

	private List<View> viewList = null;
	private List<String> list = null;
	private ViewPager viewPage = null;
	private SeekBar seekBar = null;

	private Button volumeButton = null;
	private Button prevButton = null;
	private Button playButton = null;
	private Button nextButton = null;
	private Button modeButton = null;
	private ServicePlayer servicePlayer = null;
	
	/*
	 * 三个标签页
	 */
	private View albumView = null;
	private View lrcView = null;
	private View playViewVisualizer = null;
	
	
	private MyReceiver myReceiver;
	private MediaUtils mediaUtils = null;

	private String lrcPath = null;
	private LrcInfo lrcInfo = null;

	private int currentPosition = 0;

	private TextView playViewFile = null;
	private TextView playViewLrc = null;
	private TextView playViewSinger = null;
	private TextView visualizerText = null;
	private ImageView playViewAlbum = null;
	private VisualizerView viewVisualizerView = null;
	
	
	private LrcView playLrcView = null;

	private final static String ALLMUSIC = "allmusic";
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
	
	
	private Handler myHandler = new Handler();

	private Runnable myRunnable = new Runnable() {

		@Override
		public void run() {

			if (servicePlayer != null) {
				// TODO Auto-generated method stub

				try {
					int currentTime = servicePlayer.getCurrentTime();
					seekBar.setProgress(currentTime);
					seekBar.setMax(servicePlayer.getDuration());
					
					if(lrcInfo != null && !lrcInfo.isEmpty())
					{			
						int index = lrcInfo.find(currentTime);
						
						playLrcView.setIndex(index);
						playLrcView.invalidate();
						
						System.out.println(lrcInfo.getLrc(index));
						playViewLrc.setText(lrcInfo.getLrc(index));
						visualizerText.setText(lrcInfo.getLrc(index));
					}
					
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			myHandler.postDelayed(myRunnable, handlerTime);

		}
	};

	// private PlayService playService;

	/**
	 * 播放service
	 */
	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			servicePlayer = ServicePlayer.Stub.asInterface(service);
			
			final int maxCR = Visualizer.getMaxCaptureRate();

			try {
				mVisualizer = new Visualizer(servicePlayer.getAudioSessionId());
			
				mVisualizer.setCaptureSize(256);
				mVisualizer.setDataCaptureListener(new OnDataCaptureListener() {

					@Override
					public void onWaveFormDataCapture(Visualizer visualizer,
							byte[] waveform, int samplingRate) {

						PlayView.this.viewVisualizerView.updateVisualizer(waveform);
					}

					@Override
					public void onFftDataCapture(Visualizer visualizer, byte[] fft,
							int samplingRate) {
						PlayView.this.viewVisualizerView.updateVisualizer(fft);
					}
				}, maxCR/2, false, true);
				
				mVisualizer.setEnabled(true);
				
				if(servicePlayer.isPlaying())
				{
					playButton
					.setBackgroundResource(R.drawable.btn_play_normal);
				}
				
				
			} catch (UnsupportedOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.play_view);
		
		Intent argIntent = getIntent();
		
		mediaUtils = MediaUtils.getInstance(PlayView.this);
		
		int viewTag = argIntent.getIntExtra("viewTag", -1);
		
		
		
		
		if (viewTag == ALL) {
			list = mediaUtils.getMp3infoList();
			
			currentPosition = argIntent.getIntExtra("mp3id", -1);

		} else if (viewTag == SINGER) {
			String arg = argIntent.getStringExtra("arg");
			currentPosition = argIntent.getIntExtra("mp3id", -1);

			list = mediaUtils.getSingerList(arg);

		} else if (viewTag == ALBUM) {
			String arg = argIntent.getStringExtra("arg");
			currentPosition = argIntent.getIntExtra("mp3id", -1);
			
			list = mediaUtils.getAlbumList(arg);

		}else if(viewTag == PLAYLIST)
		{
			String arg = argIntent.getStringExtra("arg");
			currentPosition = argIntent.getIntExtra("mp3id", -1);
			
			DbManager dbManager = DbManager.getInstance(PlayView.this);
			
			list = dbManager.getListWithName(arg);
		}else
		{
			DbManager dbManager = DbManager.getInstance(PlayView.this);
			currentPosition = argIntent.getIntExtra("mp3id", -1);
			list = dbManager.getCollectList();

		}
		
		Mp3Info mp3info = null;
		
		if(list !=null && !list.isEmpty())
		{
			mp3info = mediaUtils.getMp3infoWithKey(list.get(currentPosition));
			if(mp3info !=null)
			{
				System.out.println("currentPosition = "+currentPosition+"\t"+"list size = "+list.size());
			}
		}
		else
		{
		}
		
		viewList = new ArrayList<View>();
		viewPage = (ViewPager) findViewById(R.id.viewPage);

		LayoutInflater inflater = getLayoutInflater();

		playViewVisualizer = inflater.inflate(R.layout.play_view_visualizer, null);
		albumView = inflater.inflate(R.layout.play_view_album, null);
		lrcView = inflater.inflate(R.layout.play_view_lrc, null);

		viewList.add(playViewVisualizer);
		viewList.add(albumView);
		viewList.add(lrcView);

		viewPage.setAdapter(new MyViewPagerAdapter(viewList));
		viewPage.setCurrentItem(1);

		seekBar = (SeekBar) findViewById(R.id.seekBar);

		volumeButton = (Button) findViewById(R.id.volumeButton);
		prevButton = (Button) findViewById(R.id.prevButton);
		playButton = (Button) findViewById(R.id.playButton);
		nextButton = (Button) findViewById(R.id.nextButton);
		modeButton = (Button) findViewById(R.id.modeButton);

		playViewAlbum = (ImageView) albumView
				.findViewById(R.id.play_view_album);
		playViewFile = (TextView) albumView.findViewById(R.id.play_view_file);
		playViewLrc = (TextView) albumView.findViewById(R.id.play_view_lrc);
		playViewSinger = (TextView) albumView.findViewById(R.id.play_view_singer);
		
		playLrcView =(LrcView) lrcView.findViewById(R.id.play_view_lrcView);
		
		viewVisualizerView = (VisualizerView) playViewVisualizer.findViewById(R.id.visualizerView);
		visualizerText = (TextView) playViewVisualizer.findViewById(R.id.visualizer_text);
		
		SharedPreferences mPerferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		
		mode = mPerferences.getInt("playmode", 0);
		
		if (mode == playNormal) {
			modeButton
					.setBackgroundResource(R.drawable.playmode_normal);

		} else if (mode == playCycle) {
			modeButton
					.setBackgroundResource(R.drawable.playmode_repeat_current);

		} else if (mode == allCycle) {
			modeButton
					.setBackgroundResource(R.drawable.playmode_repeat_all);


		} else if (mode == playRandom) {
			modeButton
					.setBackgroundResource(R.drawable.playmode_random);

		}

		if(mp3info !=null)
			playViewFile.setText(mp3info.getTitle());

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				if (servicePlayer != null) {
					//System.out.println(seekBar.getProgress());
					 try {
						 int currentTime = seekBar.getProgress();
						servicePlayer.setCurrent(currentTime);
						
						if(lrcInfo != null && !lrcInfo.isEmpty())
						{			
							int index = lrcInfo.find(currentTime);
							
							playLrcView.setIndex(index);
							playLrcView.invalidate();
						}
						
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub

			}
		});

		volumeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

		prevButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (servicePlayer != null) {
					try {
						servicePlayer.previous();
						playLrcView.setLrcListEmpty();
						playLrcView.invalidate();
						playViewLrc.setText(R.string.lrcfile);
						visualizerText.setText(R.string.lrcfile);
						lrcInfo = null;
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});

		playButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					if (servicePlayer == null) {
						Intent intent = new Intent();
						intent.putExtra("mp3id", currentPosition);
						intent.setClass(PlayView.this, MusicPlayer.class);
						bindService(intent, mServiceConnection,
								TRIM_MEMORY_BACKGROUND);
						startService(intent);

						playButton
								.setBackgroundResource(R.drawable.btn_play_normal);

					}

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
								
								myHandler.removeCallbacks(myRunnable);
							
							} else {
								playButton
										.setBackgroundResource(R.drawable.btn_play_normal);

								servicePlayer.play();
								// playButton.setImageResource(R.drawable.btn_play_normal);

								myHandler.postDelayed(myRunnable, handlerTime);
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
						playLrcView.setLrcListEmpty();
						playLrcView.invalidate();
						playViewLrc.setText(R.string.lrcfile);
						visualizerText.setText(R.string.lrcfile);
						lrcInfo = null;
				
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});

		modeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (mode == playNormal) {
					mode = playCycle;
					// playMode.setImageResource(R.drawable.playmode_repeat_current);
					modeButton
							.setBackgroundResource(R.drawable.playmode_repeat_current);
					Toast.makeText(PlayView.this, R.string.singleCycle,
							Toast.LENGTH_SHORT).show();
					;
					
					SharedPreferences mPerferences = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());

					SharedPreferences.Editor mEditor = mPerferences.edit();

					mEditor.putInt("playmode",playCycle);
					mEditor.commit();

				} else if (mode == playCycle) {
					mode = allCycle;
					modeButton
							.setBackgroundResource(R.drawable.playmode_repeat_all);
					Toast.makeText(PlayView.this, R.string.allCycle,
							Toast.LENGTH_SHORT).show();
					;
					
					SharedPreferences mPerferences = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());

					SharedPreferences.Editor mEditor = mPerferences.edit();

					mEditor.putInt("playmode",allCycle);
					mEditor.commit();

				} else if (mode == allCycle) {
					mode = playRandom;
					modeButton
							.setBackgroundResource(R.drawable.playmode_random);
					Toast.makeText(PlayView.this, R.string.random,
							Toast.LENGTH_SHORT).show();
					;
					
					SharedPreferences mPerferences = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());

					SharedPreferences.Editor mEditor = mPerferences.edit();

					mEditor.putInt("playmode",playRandom);
					mEditor.commit();

				} else if (mode == playRandom) {
					mode = playNormal;
					modeButton
							.setBackgroundResource(R.drawable.playmode_normal);
					Toast.makeText(PlayView.this, R.string.normal,
							Toast.LENGTH_SHORT).show();
					;
					
					SharedPreferences mPerferences = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());

					SharedPreferences.Editor mEditor = mPerferences.edit();

					mEditor.putInt("playmode",playNormal);
					mEditor.commit();
				}

				if (servicePlayer != null)
					try {
						servicePlayer.playMode(mode);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

			}
		});

		Intent intent = new Intent();
		intent.setClass(PlayView.this, MusicPlayer.class);
		bindService(intent, mServiceConnection, TRIM_MEMORY_BACKGROUND);
		startService(intent);

		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MYRECEIVER);
		filter.addAction(MUSICLRC);
		filter.addAction(MUSICLRCINFO);
		PlayView.this.registerReceiver(myReceiver, filter);

		myHandler.post(myRunnable);

		Resources res = getResources();

		BitmapDrawable bmpDraw = (BitmapDrawable) res
				.getDrawable(R.drawable.default_album);
		Bitmap bmp = MediaUtils.getAlbumInverted(bmpDraw.getBitmap());

		playViewAlbum.setImageBitmap(bmp);
		
		DbManager dbManager = DbManager.getInstance(PlayView.this);
		
		loadLrc();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		unregisterReceiver(myReceiver);
		unbindService(mServiceConnection);
		myHandler.removeCallbacks(myRunnable);
	}

	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
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
				
				currentPosition = position;
				
				if (list != null && !list.isEmpty()) {

					Mp3Info mp3info = mediaUtils.getMp3infoWithKey(list.get(position));
					if(mp3info !=null)
					{
						playViewFile.setText(mp3info.getTitle());
						playViewSinger.setText("");
						playViewLrc.setText(R.string.lrcfile);
						visualizerText.setText(R.string.lrcfile);
						
					}
				}

				seekBar.setMax(during);
				
				loadLrc();

			} else if (action.equals(MUSICLRC)) {
				Bundle bundle = intent.getExtras();
				lrcPath = null;
			    lrcPath = bundle.getString("lrcPath");
				
				Intent parserIntent = new Intent();
				parserIntent.putExtra("lrcPath", lrcPath);
				parserIntent.setClass(PlayView.this, LrcParserService.class);
				startService(parserIntent);
				
			}
			else if(action.equals(MUSICLRCINFO))
			{
				String lrc = null;
				
				lrcInfo = null;
				lrcInfo = (LrcInfo) intent.getSerializableExtra("lrcInfo");
				
				playViewSinger.setText(lrcInfo.getSinger());
				
				List<LrcItem> list = lrcInfo.getLrcList();
				
				playLrcView.setLrcList(list);
				playLrcView.invalidate();
				playViewLrc.setText(R.string.lrcfile);
				visualizerText.setText(R.string.lrcfile);
		
			}
		}

	}
	
	private void loadLrc()
	{
		if (list != null && !list.isEmpty()) {

			Mp3Info mp3info = mediaUtils.getMp3infoWithKey(list.get(currentPosition));

			if (mp3info != null) {

				DbManager dbManager = DbManager.getInstance(PlayView.this);

				lrcPath = dbManager.select(mp3info);

				if (lrcPath == null) {

					Intent dlIntent = new Intent();
					dlIntent.putExtra("mp3info", mp3info);
					dlIntent.setClass(PlayView.this, BaiduDownloadService.class);
					startService(dlIntent);
				} else {
					Intent parserIntent = new Intent();
					parserIntent.putExtra("lrcPath", lrcPath);
					parserIntent
							.setClass(PlayView.this, LrcParserService.class);
					startService(parserIntent);

				}
			}
		}
	}

}
