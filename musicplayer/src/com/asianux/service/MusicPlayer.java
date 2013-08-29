package com.asianux.service;

import java.io.IOException;

import com.asianux.utils.Mp3Info;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;

public class MusicPlayer extends Service {

	private MediaPlayer mediaPlayer = null;

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
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		final Mp3Info mp3info = (Mp3Info) intent
				.getSerializableExtra("mp3info");

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
		}
		else
		{
			mediaPlayer.stop();
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
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
