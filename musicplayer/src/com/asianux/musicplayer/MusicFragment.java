package com.asianux.musicplayer;

import java.util.List;

import com.asianux.adapter.MainAdapter;
import com.asianux.database.DbManager;
import com.asianux.dialog.ConfirmDialog;
import com.asianux.dialog.RenameDialog;
import com.asianux.dialog.RenameListDialog;
import com.asianux.dialog.RenameListDialog.RenameDialogEventListener;
import com.asianux.service.MusicPlayer;
import com.asianux.service.ServicePlayer;
import com.asianux.dialog.ConfirmDialog.ConfirmDialogEventListener;
import com.asianux.dialog.RenameDialog.RenameDialogListener;


import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年10月9日 下午8:41:38
 * 类说明
 */

public class MusicFragment extends Fragment{
	
	private ImageButton allMusic;
	private ImageButton albumMusic;
	private ImageButton singerMusic;
	private ImageButton playMusic;
	private ImageButton storgeMusic;
	private ImageButton collectMusic;
	private Resources resource = null;
	private GridView gridView = null;
	private ImageView albumView = null;
	private TextView nameText = null;
	private TextView singerText = null;
	private TextView timeText = null;
	
	private Button playButton = null;
	private Button nextButton = null;
	
	private LinearLayout linearLayout = null;
	
	private final static String ALLMUSIC = "allmusic";
	private final static String COLLECTMUSIC = "collectmusic";
	private final static String CUSTOMLIST = "custom";
	
	private DbManager dbManager = null;
	private MainAdapter mainAdapter = null;
	private Context context = null;
	private boolean isPlay = false; 	//是否播放标志
	private View view = null;
	
	public interface MusicFragmentLinster
	{
		public void play();
		public void pause();
		public void next();
		public void to_playui();
	}
	
	private MusicFragmentLinster musicFragmentLinster = null;
	
	public MusicFragment(MusicFragmentLinster linster)
	{
		musicFragmentLinster = linster;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.mainlayout, container, false);
		context = inflater.getContext();
		
		initView(view);

		albumView = (ImageView) view.findViewById(R.id.album_image);
		
		nameText = (TextView) view.findViewById(R.id.music_name);
		singerText = (TextView) view.findViewById(R.id.singer_name);
		timeText = (TextView) view.findViewById(R.id.music_time);
		
		playButton = (Button) view.findViewById(R.id.play_button);
		nextButton = (Button) view.findViewById(R.id.next_button);
		
		linearLayout = (LinearLayout) view.findViewById(R.id.music_toolbar);
		
		playButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isPlay)
				{
					isPlay = true;
					musicFragmentLinster.play();
					playButton.setBackgroundResource(R.drawable.icon_pause_normal);
					
				}
				else
				{
					musicFragmentLinster.pause();
					isPlay = false;
					
					playButton.setBackgroundResource(R.drawable.icon_play_normal);
				}
				
			}
		});
		
		nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				musicFragmentLinster.next();
			}
		});
		
		linearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				musicFragmentLinster.to_playui();
			}
		});
		
		return view;
	}
	
	/**
	 * setPlayStatus设置播放按钮图标
	 * (这里描述这个方法适用条件 – 可选) 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public void setPlayStatus(boolean status)
	{
		if(status)
		{
			playButton.setBackgroundResource(R.drawable.icon_pause_normal);
		}
		else
		{
			playButton.setBackgroundResource(R.drawable.icon_pause_normal);
		}
	}
	
	private void initView(View view)
	{
		dbManager = DbManager.getInstance(context);

		gridView = (GridView) view.findViewById(R.id.mainGridview);

		mainAdapter = new MainAdapter(context);
		
		resource = getResources();

		mainAdapter.makeViewDefault(resource.getString(R.string.allMusic));
		mainAdapter.makeViewDefault(resource.getString(R.string.album));
		mainAdapter.makeViewDefault(resource.getString(R.string.singer));
		mainAdapter.makeViewDefault(resource.getString(R.string.collect));

		addPlayList();

		gridView.setAdapter(mainAdapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				lanuchActivity(arg2);
			}
		});

		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				lanuchDialog(arg2);
				return false;
			}
		});
	}
	
	public void initView()
	{
		remoevPlayList();
		addPlayList();
	}
	
	private void remoevPlayList()
	{
		for(int i = 4;i<mainAdapter.getCount();i++)
		{
			mainAdapter.removeView(i);
		}
		
		mainAdapter.notifyDataSetChanged();
	}
	
	private void addPlayList()
	{
		List<String> list = dbManager.getPlayList();

		if (list != null && !list.isEmpty()) {
			for (String str : list) {
				if (!str.isEmpty()) {
					mainAdapter.makeViewDefault(str);
				}
			}
		}
	}
	
	private void lanuchActivity(int arg2) {
		// TODO Auto-generated method stub
		
		String str = mainAdapter.getViewId(arg2);

		if(str.equals(resource.getString(R.string.allMusic)))
		{
			System.out.println(str);
			Intent intent = new Intent();
			intent.setClass(context, ListActivity.class);
			intent.putExtra("tag",ALLMUSIC);
			startActivity(intent);
			
		}
		else if(str.equals(resource.getString(R.string.album)))
		{
			System.out.println(str);
			
			Intent intent = new Intent();
			intent.setClass(context, AlbumActivity.class);
			startActivity(intent);

		}
		else if(str.equals(resource.getString(R.string.singer)))
		{
			System.out.println(str);
			
			Intent intent = new Intent();
			intent.setClass(context, SingerActivity.class);
			startActivity(intent);
					
		}
		else if(str.equals(resource.getString(R.string.collect)))
		{
			System.out.println(str);
			
			Intent intent = new Intent();
			intent.setClass(context, ListActivity.class);
			intent.putExtra("tag",COLLECTMUSIC);
			startActivity(intent);

		}
		else
		{
			System.out.println(str);
			
			Intent intent = new Intent();
			intent.setClass(context, ListActivity.class);
			intent.putExtra("tag", CUSTOMLIST);
			intent.putExtra("listname", str);
			startActivity(intent);
		}
		
	}
	
	private void lanuchDialog(final int position) {
		
		if(position >=4)
		{
			System.out.println("lanuchDialog = " +position);
			String str = mainAdapter.getViewId(position);

			RenameListDialog dialog = new RenameListDialog(context, str, new RenameDialogEventListener() {

				@Override
				public void RenameDialogEvent(int tag) {
					// TODO Auto-generated method stub
					
					if(tag == 0)
					{
						lanuchConfirmDialog(position);
					}
					else
					{
						lanuchRenameDialog(position);
					}
				}

			});
			
			dialog.show();
		}

	}
	
	private void lanuchConfirmDialog(final int position) {
	
		System.out.println("lanuchConfirmDialog = " +position);
		
		String str = mainAdapter.getViewId(position);
		
		ConfirmDialog dialog = new ConfirmDialog(context, str, new ConfirmDialogEventListener() {
			
			@Override
			public void confirmDialogEvent(int tag) {
				// TODO Auto-generated method stub
				
				View view = mainAdapter.getView(position);
				
				if(view !=null)
				{
					//MainActivity.this.gridView.removeView(view);
					DbManager dbManager = DbManager.getInstance(context);
					
					dbManager.removeListDb(mainAdapter.getViewId(position));
					dbManager.removePlayList(mainAdapter.getViewId(position));
					
					mainAdapter.removeView(position);
					mainAdapter.notifyDataSetChanged();
					
				}
			}
		});
		
		dialog.show();
		
	}
	
	private void lanuchRenameDialog(final int position)
	{
		String str = mainAdapter.getViewId(position);
		
		RenameDialog dialog = new RenameDialog(context, str, new RenameDialogListener() {
		
			@Override
			public void RenameDialogEvent(String name) {
				// TODO Auto-generated method stub

				if(!name.equals(mainAdapter.getViewId(position)))
				{
					dbManager.renamePlayList(mainAdapter.getViewId(position), name);
					
					mainAdapter.setViewText(position, name);
				}
				
			}
		});
		
		dialog.show();

	}
	
	public void setMp3Info(List<String > list)
	{
		if(list.size() == 2)
		{
			nameText.setText(list.get(0));
			singerText.setText(list.get(1));
		}
	}
	
	public void setTime(String str)
	{
		timeText.setText(str);
	}
	
}
