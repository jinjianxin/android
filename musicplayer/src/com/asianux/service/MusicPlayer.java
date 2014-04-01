package com.asianux.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.asianux.database.DbManager;
import com.asianux.musicplayer.ListActivity;
import com.asianux.utils.LrcInfo;
import com.asianux.utils.LrcParser;
import com.asianux.utils.MediaUtils;
import com.asianux.utils.Mp3Info;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;

public class MusicPlayer extends Service {

	private MediaPlayer mediaPlayer = null;
	private MediaUtils mediaUtils = null;
	private DbManager dbManager = null;
	private int currentPosition = 0;
	private List<String> list = null;
	private final String MYRECEIVER = "Music.Player.MUSIC_SERVICE_BROADCAST";
	private int limit = 0;

	/**
	 * 播放模式
	 */
	private static final int playNormal = 0;
	private static final int playCycle = 1;
	private static final int allCycle = 2;
	private static final int playRandom = 3;
	
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
	
	private int viewTag = 0;

	private int currentMode = 0;
	private String arg = null;

	private ServicePlayer.Stub stub = new ServicePlayer.Stub() {

		@Override
		public void stop() throws RemoteException {
			// TODO Auto-generated method stub
			if (mediaPlayer != null) {
				mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer = null;
			}

		}

		@Override
		public void setCurrent(int cur) throws RemoteException {
			// TODO Auto-generated method stub

			if (mediaPlayer != null) {
				mediaPlayer.seekTo(cur);
			}

		}

		@Override
		public void play() throws RemoteException {
			// TODO Auto-generated method stub
			
			if (mediaPlayer != null) {
				mediaPlayer.start();
			}
			else
			{
				defaultPlay();
			}

		}

		@Override
		public void pause() throws RemoteException {
			// TODO Auto-generated method stub
			if (mediaPlayer != null) {
				mediaPlayer.pause();
			}

		}

		@Override
		public boolean isPlaying() throws RemoteException {
			// TODO Auto-generated method stub
			if (mediaPlayer != null) {
				if (mediaPlayer.isPlaying()) {
					return true;
				} else {
					return false;
				}

			}
			return false;
		}

		@Override
		public int getDuration() throws RemoteException {
			// TODO Auto-generated method stub

			if (mediaPlayer != null) {
				return mediaPlayer.getDuration();
			}

			return 0;
		}

		@Override
		public int getCurrentTime() throws RemoteException {
			// TODO Auto-generated method stub
			if (mediaPlayer != null) {
				return mediaPlayer.getCurrentPosition();
			}
			return 0;
		}

		@Override
		public void previous() throws RemoteException {
			// TODO Auto-generated method stub

			if (currentMode == playNormal) {
				if (currentPosition > 0) {
					currentPosition -= 1;
				} else {
					currentPosition = 0;
				}

			} else if (currentMode == playRandom) {
				Random random = new Random(System.currentTimeMillis());

				currentPosition = random.nextInt(limit);

			} else if (currentMode == allCycle) {
				if (currentPosition > 0) {
					currentPosition -= 1;
				} else {
					currentPosition = list.size();
				}
			}

			//Mp3Info mp3info = list.get(currentPosition);
			
			Mp3Info mp3info = mediaUtils.getMp3infoWithKey(list.get(currentPosition));

			if (mp3info != null) {

				mediaPlayer.reset();

				try {
					mediaPlayer.setDataSource(MusicPlayer.this,
							Uri.parse(mp3info.getUrl()));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					mediaPlayer.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mediaPlayer.start();

				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
						try {
							stub.next();
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

				Intent intent = new Intent();
				intent.putExtra("postion", currentPosition);
				intent.putExtra("during", mediaPlayer.getDuration());
				intent.setAction(MYRECEIVER);// action与接收器相同
				sendBroadcast(intent);

				SharedPreferences mPerferences = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());

				SharedPreferences.Editor mEditor = mPerferences.edit();

				mEditor.putInt("position", currentPosition);
				mEditor.commit();
			}

		}

		@Override
		public void next() throws RemoteException {
			// TODO Auto-generated method stub

			if (list != null) {
				if (!list.isEmpty()) {

					if (currentMode == playNormal) {
						if (currentPosition + 1 < list.size()) {
							currentPosition += 1;
						} else {
							if (mediaPlayer != null) {
								mediaPlayer.stop();
								mediaPlayer.release();
								mediaPlayer = null;
							}
						}

					} else if (currentMode == playRandom) {

						Random random = new Random(System.currentTimeMillis());

						currentPosition = random.nextInt(limit);

					} else if (currentMode == allCycle) {
						if (currentPosition + 1 < list.size()) {
							currentPosition += 1;
						} else {
							currentPosition = 0;
						}
					}

					if (mediaPlayer != null) {

						// Mp3Info mp3info = list.get(currentPosition);

						Mp3Info mp3info = mediaUtils.getMp3infoWithKey(list
								.get(currentPosition));

						if (mp3info != null) {

							mediaPlayer.reset();

							try {
								mediaPlayer.setDataSource(MusicPlayer.this,
										Uri.parse(mp3info.getUrl()));
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalStateException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							try {
								mediaPlayer.prepare();
							} catch (IllegalStateException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							mediaPlayer.start();

							mediaPlayer
									.setOnCompletionListener(new OnCompletionListener() {

										@Override
										public void onCompletion(MediaPlayer mp) {
											// TODO Auto-generated method stub
											try {
												stub.next();
											} catch (RemoteException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
										}
									});

							Intent intent = new Intent();
							intent.putExtra("postion", currentPosition);
							intent.putExtra("during", mediaPlayer.getDuration());
							intent.setAction(MYRECEIVER);// action与接收器相同
							sendBroadcast(intent);

							SharedPreferences mPerferences = PreferenceManager
									.getDefaultSharedPreferences(getApplicationContext());

							SharedPreferences.Editor mEditor = mPerferences
									.edit();

							mEditor.putInt("position", currentPosition);
							mEditor.commit();
						}
					}
				}
			}
		}

		@Override
		public int getPosition() throws RemoteException {
			// TODO Auto-generated method stub
			return currentPosition;
		}

		@Override
		public void playMode(int mode) throws RemoteException {
			// TODO Auto-generated method stub

			currentMode = mode;

			System.out.println("mode=" + mode + "\n");

		}

		@Override
		public String getPlayingUrl() throws RemoteException {
			// TODO Auto-generated method stub
			
			if (list != null && !list.isEmpty()) {
				return list.get(currentPosition);
			}
			else
			{
				return null;
			}
		}

		@Override
		public String getPlayingInfo() throws RemoteException {
			// TODO Auto-generated method stub
			
			
			System.out.println("viewTag = "+viewTag+"\t"+arg);
			
			if(viewTag !=-1 )
			{	
				String tmp = new String();	
				tmp+=currentPosition+"|"+viewTag+"|"+arg;
				return  tmp;
			}
			else
			{
				return null;
			}
		}

		@Override
		public int getAudioSessionId() throws RemoteException {
			// TODO Auto-generated method stub
			return mediaPlayer.getAudioSessionId();
		}

		@Override
		public List<String> getMp3info() throws RemoteException {
			// TODO Auto-generated method stub
			if(list !=null && !list.isEmpty())
			{
				List<String> argList = new ArrayList<String>();

				Mp3Info mp3info = mediaUtils.getMp3infoWithKey(list
						.get(currentPosition));

				argList.add(mp3info.getTitle());
				argList.add(mp3info.getArtist());

				return argList;
			} else {
				return null;
			}
		}
	};

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return stub;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		mediaUtils = MediaUtils.getInstance(MusicPlayer.this);
		dbManager = DbManager.getInstance(MusicPlayer.this);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
			
		arg = null;
		
		if(intent == null)
			return ;
		
		int tmp  = intent.getIntExtra("viewTag", -1);
		int id = 0;

		if(tmp == -1)
		{
			return ;
		}
		
		viewTag =tmp;
		
		if (viewTag == ALL) {
			list = mediaUtils.getMp3infoList();
			limit = list.size();
			id = intent.getIntExtra("mp3id", -1);

		} else if (viewTag == SINGER) {
			arg = intent.getStringExtra("arg");
			id = intent.getIntExtra("mp3id", -1);

			list = mediaUtils.getSingerList(arg);

			if (list != null && !list.isEmpty()) {
				limit = list.size();
			}

		} else if (viewTag == ALBUM) {
			arg = intent.getStringExtra("arg");
			id = intent.getIntExtra("mp3id", -1);

			list = mediaUtils.getAlbumList(arg);

			if (list != null && !list.isEmpty()) {
				limit = list.size();
			}
		} else if(viewTag == COLLECT )
		{
			list = dbManager.getCollectList();
			id = intent.getIntExtra("mp3id", -1);
			
			if (list != null && !list.isEmpty()) {
				limit = list.size();
			}
		}else if(viewTag == PLAYLIST)
		{
			arg = intent.getStringExtra("arg");
			id = intent.getIntExtra("mp3id", -1);
			
			list = dbManager.getListWithName(arg);
			
			if (list != null && !list.isEmpty()) {
				limit = list.size();
			}
		}

		if (id != -1) {

			currentPosition = id;

			if (list != null && !list.isEmpty()) {

				Mp3Info mp3info = mediaUtils.getMp3infoWithKey(list
						.get(currentPosition));

				if (mp3info != null) {

					if (mediaPlayer == null) {
						mediaPlayer = new MediaPlayer();
						try {
							mediaPlayer.setDataSource(MusicPlayer.this,
									Uri.parse(mp3info.getUrl()));
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						try {
							mediaPlayer.prepare();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						mediaPlayer.start();

						mediaPlayer
								.setOnCompletionListener(new OnCompletionListener() {

									@Override
									public void onCompletion(MediaPlayer mp) {
										// TODO Auto-generated method stub

										try {
											stub.next();
										} catch (RemoteException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
								});

						Intent in = new Intent();
						in.putExtra("postion", currentPosition);
						in.putExtra("during", mediaPlayer.getDuration());
						in.setAction(MYRECEIVER);// action与接收器相同
						sendBroadcast(in);

						SharedPreferences mPerferences = PreferenceManager
								.getDefaultSharedPreferences(getApplicationContext());

						SharedPreferences.Editor mEditor = mPerferences.edit();

						mEditor.putInt("position", currentPosition);
						mEditor.commit();

					} else {
						mediaPlayer.reset();

						try {
							mediaPlayer.setDataSource(MusicPlayer.this,
									Uri.parse(mp3info.getUrl()));
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						try {
							mediaPlayer.prepare();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mediaPlayer.start();

						mediaPlayer
								.setOnCompletionListener(new OnCompletionListener() {

									@Override
									public void onCompletion(MediaPlayer mp) {
										// TODO Auto-generated method stub
										try {
											stub.next();
										} catch (RemoteException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});

						Intent in = new Intent();
						in.putExtra("postion", currentPosition);
						in.putExtra("during", mediaPlayer.getDuration());
						in.setAction(MYRECEIVER);// action与接收器相同
						sendBroadcast(in);

						SharedPreferences mPerferences = PreferenceManager
								.getDefaultSharedPreferences(getApplicationContext());

						SharedPreferences.Editor mEditor = mPerferences.edit();

						mEditor.putInt("position", currentPosition);
						mEditor.commit();
					}

				}
			}
		}
	}
	

	/**
	 * defaultPlay 点击首页面播放按钮是调用此方法，只有在mediaPlayer未初始化的情况下
	 * (这里描述这个方法适用条件 – 可选) 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	private void defaultPlay() {
		// TODO Auto-generated method stub
		
		viewTag =0;
		currentPosition =0;
		
		list = mediaUtils.getMp3infoList();
		
		if (list != null && !list.isEmpty()) {

			limit = list.size();

			Mp3Info mp3info = mediaUtils.getMp3infoWithKey(list
					.get(currentPosition));

			if (mp3info != null) {

				if (mediaPlayer == null) {
					mediaPlayer = new MediaPlayer();

					try {
						mediaPlayer.setDataSource(MusicPlayer.this,
								Uri.parse(mp3info.getUrl()));
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					try {
						mediaPlayer.prepare();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					mediaPlayer.start();

					mediaPlayer
							.setOnCompletionListener(new OnCompletionListener() {

								@Override
								public void onCompletion(MediaPlayer mp) {
									// TODO Auto-generated method stub
									try {
										stub.next();
									} catch (RemoteException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});

					Intent in = new Intent();
					in.putExtra("postion", currentPosition);
					in.putExtra("during", mediaPlayer.getDuration());
					// in.putExtra("mp3info", mp3info);
					in.setAction(MYRECEIVER);// action与接收器相同
					sendBroadcast(in);

					SharedPreferences mPerferences = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());

					SharedPreferences.Editor mEditor = mPerferences.edit();

					mEditor.putInt("position", currentPosition);
					mEditor.commit();
				}
			}

		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		try {
			stub.stop();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
