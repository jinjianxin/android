package com.asianux.musicplayer;

import java.util.ArrayList;
import java.util.List;

import com.asianux.service.MusicPlayer;
import com.asianux.service.ServicePlayer;
import com.asianux.utils.MediaUtils;
import com.asianux.utils.Mp3Info;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
	private static final int playNormal = 0;
	private static final int playCycle = 1;
	private static final int allCycle = 2;
	private static final int playRandom =3;
	
	private int mode = 0;	
	
	private List<View > viewList = null;
	private List<Mp3Info> list = null;
	private ViewPager viewPage = null;
	private SeekBar seekBar = null;

	private Button volumeButton = null;
	private Button prevButton = null;
	private Button playButton = null;
	private Button nextButton = null;
	private Button modeButton = null;
	private ServicePlayer servicePlayer = null;
//	private DownloadService downloadService = null;
	private View albumView = null;
	private View lrcView = null;	
	private MyReceiver myReceiver;
	
	private int currentPosition = 0;
	
	private TextView playViewFile = null;
	private TextView playViewLrc = null;
	private ImageView playViewAlbum = null;
	
	private Handler myHandler = new Handler();

	private Runnable myRunnable = new Runnable() {

		@Override
		public void run() {

			if (servicePlayer != null) {
				// TODO Auto-generated method stub

				int seconds = 0;

				try {
					seconds = servicePlayer.getCurrentTime() / 1000;
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				int minutes = seconds / 60;
				seconds = seconds % 60;

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
		}
	};
	
	/**
	 * 下载歌词service
	 */
	private ServiceConnection dServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			
		//	downloadService = ((DownloadService.MyBinder)service).getInstance();
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.play_view);
		
		currentPosition = (Integer) this.getIntent().getSerializableExtra("mp3id");
		
		MediaUtils mediaUtils = MediaUtils.getInstance();
		list = mediaUtils.getMp3Info(PlayView.this);
		
		Mp3Info mp3info = list.get(currentPosition);
		
		viewList =  new ArrayList<View>();
		viewPage = (ViewPager) findViewById(R.id.viewPage);
		
		LayoutInflater inflater=getLayoutInflater();
		
		albumView = inflater.inflate(R.layout.play_view_album, null);
		lrcView = inflater.inflate(R.layout.play_view_lrc, null);
		
		viewList.add(albumView);
		viewList.add(lrcView);
		          
		viewPage.setAdapter(new MyViewPagerAdapter(viewList));
		viewPage.setCurrentItem(0);
		
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		
		volumeButton = (Button) findViewById(R.id.volumeButton);
		prevButton = (Button) findViewById(R.id.prevButton);
		playButton =(Button) findViewById(R.id.playButton);
		nextButton =(Button) findViewById(R.id.nextButton);
		modeButton = (Button) findViewById(R.id.modeButton);
		
		playViewAlbum = (ImageView) albumView.findViewById(R.id.play_view_album);
		playViewFile = (TextView) albumView.findViewById(R.id.play_view_file);
		playViewLrc = (TextView) albumView.findViewById(R.id.play_view_lrc);
		
		playViewFile.setText(mp3info.getTitle());
		
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				if(servicePlayer !=null)
				{
					try {
						servicePlayer.setCurrent(seekBar.getProgress());
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
				if(servicePlayer !=null)
				{
					try {
						servicePlayer.previous();
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

						playButton.setBackgroundResource(R.drawable.btn_play_normal);

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

								// playButton.setImageResource(R.drawable.pause_normal);

								myHandler.removeCallbacks(myRunnable);
							} else {
								playButton
										.setBackgroundResource(R.drawable.btn_play_normal);

								servicePlayer.play();
								// playButton.setImageResource(R.drawable.btn_play_normal);

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
				if(servicePlayer !=null)
				{
					try {
						servicePlayer.next();
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
				
				if(mode == playNormal)
				{
					mode = playCycle;
					//playMode.setImageResource(R.drawable.playmode_repeat_current);
					modeButton.setBackgroundResource(R.drawable.playmode_repeat_current);
					Toast.makeText(PlayView.this, R.string.singleCycle, Toast.LENGTH_SHORT).show();;
					
				}else if(mode == playCycle)
				{
					mode = allCycle;
					modeButton.setBackgroundResource(R.drawable.playmode_repeat_all);
					Toast.makeText(PlayView.this, R.string.allCycle, Toast.LENGTH_SHORT).show();;
					
				}else if(mode ==allCycle)
				{
					mode =playRandom;
					modeButton.setBackgroundResource(R.drawable.playmode_random);
					Toast.makeText(PlayView.this, R.string.random, Toast.LENGTH_SHORT).show();;
					
				}else if(mode ==playRandom)
				{
					mode =playNormal;
					modeButton.setBackgroundResource(R.drawable.playmode_normal);
					Toast.makeText(PlayView.this, R.string.normal, Toast.LENGTH_SHORT).show();;
				}
				
				if(servicePlayer !=null)
					try {
						servicePlayer.playMode(mode);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

			}
		});
		
		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MYRECEIVER);
		PlayView.this.registerReceiver(myReceiver, filter);

		myHandler.post(myRunnable);
		
		Resources res=getResources();

		BitmapDrawable bmpDraw=(BitmapDrawable)res.getDrawable(R.drawable.default_album);
		Bitmap bmp = MediaUtils.getAlbumInverted(bmpDraw.getBitmap());
		
		playViewAlbum.setImageBitmap(bmp);
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	public class MyViewPagerAdapter extends PagerAdapter{
	    private List<View> mListViews;

	    public MyViewPagerAdapter(List<View> mListViews) {
	        this.mListViews = mListViews;
	    }

	    @Override
	    public void destroyItem(ViewGroup container, int position, Object object)   {
	        container.removeView(mListViews.get(position));
	    }


	    @Override
	    public Object instantiateItem(ViewGroup container, int position) {
	         container.addView(mListViews.get(position), 0);
	         return mListViews.get(position);
	    }

	    @Override
	    public int getCount() {
	        return  mListViews.size();
	    }

	    @Override
	    public boolean isViewFromObject(View arg0, Object arg1) {
	        return arg0==arg1;
	    }
	}

	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle bundle = intent.getExtras();
			int position = bundle.getInt("postion");
			int during = bundle.getInt("during");

			seekBar.setMax(during);
		}

	}


}
