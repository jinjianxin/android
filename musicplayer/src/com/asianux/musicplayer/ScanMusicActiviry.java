package com.asianux.musicplayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import com.asianux.database.DbManager;
import com.asianux.utils.MediaUtils;
import com.asianux.utils.Mp3Info;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年10月8日 下午9:16:06
 * 类说明
 */

public class ScanMusicActiviry extends Activity {
	
	private ViewPager viewPager = null;
	private LayoutInflater myInflater = null;
	private Button scanButton = null;
	private Handler myHandler = null;
	private TextView scanText = null;
	private TextView musicCount = null;
	private List<Mp3Info> mp3infoList = null;
	private Resources resources = null;
	private MyAsyncTask task = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.scan_music_layput);
		
		mp3infoList = new ArrayList<Mp3Info>();
		
		myInflater = getLayoutInflater();
		myHandler = new Handler();
		
		resources = getResources();
		
		viewPager = (ViewPager) findViewById(R.id.scanviewPage);
		List<View> list = new ArrayList<View>();
		
		list.add(createViewPager(R.drawable.navigation001));
		list.add(createViewPager(R.drawable.navigation002));
		list.add(createViewPager(R.drawable.navigation003));
		list.add(createViewPager(R.drawable.navigation004));
		list.add(createViewPager(R.drawable.navigation005));
		list.add(createViewPager(R.drawable.navigation006));
		
		viewPager.setAdapter(new MyViewPagerAdapter(list));
		
		scanText = (TextView) findViewById(R.id.scanText);
		
		musicCount = (TextView) findViewById(R.id.music_tip);
		
		String str = resources.getString(R.string.music_count);
		str = String.format(str,0);
		musicCount.setText(str);
	
		scanButton = (Button) findViewById(R.id.scanButton);
		scanButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				scanText.setGravity(Gravity.LEFT);
				mp3infoList.clear();
				
				task = new MyAsyncTask();
				task.execute(0);

			}
		});
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if(task !=null)
		{
			task.cancel(true);
		}
		
	}
	
	
	public class MyViewPagerAdapter extends PagerAdapter
	{
		private List<View> viewList = null;
		
		public MyViewPagerAdapter(List<View> list)
		{
			this.viewList = list;			
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Integer.MAX_VALUE;
			//return viewList.size();
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			//super.destroyItem(container, position, object);
			container.removeView(viewList.get(position%viewList.size()));
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
		//	return super.instantiateItem(container, position);
			container.addView(viewList.get(position%viewList.size()),0);
			
			return viewList.get(position%viewList.size());
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		
	}
	
	public View createViewPager(int id)
	{
		//playViewVisualizer = inflater.inflate(R.layout.play_view_visualizer, null);
		View view = myInflater.inflate(R.layout.scan_music_layout_item, null);
		//ImageView imageView = (ImageView) view.findViewById(R.id.musci_scan_view_image);
		Button button= (Button) view.findViewById(R.id.musci_scan_view_image);
		button.setBackgroundResource(id);
		return view;
	}
	
	public class MyAsyncTask extends AsyncTask<Integer,String, List<Mp3Info>>
	{

		public MyAsyncTask()
		{
			
		}
		
		@Override
		protected List<Mp3Info> doInBackground(Integer... params) {
			// TODO Auto-generated method stub
		
		
		/*	GetFiles(Environment
					.getExternalStorageDirectory());*/

			getFiles();

			
				
			return mp3infoList;
		}
		
		public void getFiles()
		{
			Cursor cursor = ScanMusicActiviry.this.getContentResolver().query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
					null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToNext();

				long id = cursor.getLong(cursor
						.getColumnIndex(MediaStore.Audio.Media._ID));

				String title = cursor.getString((cursor
						.getColumnIndex(MediaStore.Audio.Media.TITLE)));

				String artist = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.ARTIST));

				long size = cursor.getLong(cursor
						.getColumnIndex(MediaStore.Audio.Media.SIZE));

				String url = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.DATA));

				int isMusic = cursor.getInt(cursor
						.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
				
				long albumId = cursor.getInt(cursor
						.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
				
				String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
				
				if(isMusic !=0)
				{
					Mp3Info mp3Info = new Mp3Info();
					mp3Info.setUrl(url);
					mp3Info.setTitle(title);
					mp3Info.setArtist(artist);
					mp3Info.setAlbum(album);
					
					publishProgress(url);
					mp3infoList.add(mp3Info);
					
				}
				
			}
			
		}
		
	/*	public void GetFiles(File filePath)
		{	
			File[] files = filePath.listFiles();

			int length = files.length;
			
			if (files != null)
			{
				for (int i = 0; i < length; i++)
				{
							
					if (files[i].isDirectory())
					{
						GetFiles(files[i]);
					}
					else
					{				
						if (files[i].getName().toLowerCase()
								.endsWith("." + "mp3") || files[i].getName().toLowerCase()
								.endsWith("." + "wav") )
						{
							System.out.println("****************path = "+files[i].getAbsolutePath());
							publishProgress(files[i].getAbsoluteFile().toString());
							
							getTag(files[i]);
						}
					}
				}
			}
		}
	
		public void getTag(File file)
		{
			
			MP3File mp3File;
			try {
				
				Mp3Info mp3Info = new Mp3Info();
				mp3Info.setUrl(file.getAbsolutePath());
				
				mp3File = (MP3File) AudioFileIO.read(file);
				Tag tag = mp3File.getTag();
				
				if (tag !=null &&!tag.isEmpty()) {

					String title = tag.getFirst(FieldKey.TITLE);

					if (!title.isEmpty()) {
						mp3Info.setTitle(title);
					}
					
					String artist = tag.getFirst(FieldKey.ARTIST);
					
					if(!artist.isEmpty())
					{
						mp3Info.setArtist(artist);
					}
					
					String album = tag.getFirst(FieldKey.ALBUM);
					
					if(!album.isEmpty())
					{
						mp3Info.setAlbum(album);
					}	
				}
				
				mp3infoList.add(mp3Info);
				
			} catch (CannotReadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TagException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ReadOnlyFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidAudioFrameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		*/
		protected void onPostExecute(java.util.List<Mp3Info> result) {
			//scanText.setGravity(Gravity.CENTER_HORIZONTAL);
			scanText.setText("");
			
			DbManager dbManager = DbManager.getInstance(ScanMusicActiviry.this);
			dbManager.cleanMusicDb();
			dbManager.insert(result);

			MediaUtils mediaUtils = MediaUtils.getInstance(ScanMusicActiviry.this);
			mediaUtils.clear();
			mediaUtils.initData();
			
			super.onPostExecute(result);
		};
		
		
		protected void onProgressUpdate(String[] values) {	
			String str = resources.getString(R.string.music_count);
			str = String.format(str, mp3infoList.size());
			musicCount.setText(str);
			
			scanText.setText(values[0]);
		};
		
	}

}