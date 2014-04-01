package com.asianux.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jaudiotagger.tag.FieldKey;

import com.asianux.database.DbManager;
import com.asianux.musicplayer.ListActivity;
import com.asianux.musicplayer.R;
import com.asianux.utils.MediaUtils;
import com.asianux.utils.Mp3Info;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

	//private View[] itemView;
	private List<View> itemView = null;
	private Context context;
	private MediaUtils mediaUtil = null;
	private DbManager dbManager = null;
	private List<String> keyList = null;

	public ListAdapter(Context context, List<String > list) {
		// TODO Auto-generated constructor stub

		this.context = context;
		mediaUtil = MediaUtils.getInstance(context);
		dbManager = DbManager.getInstance(context);
		
		itemView = new ArrayList<View>();
		
		keyList = list;
		
		System.out.println("------------size = "+list.size());

		if (!list.isEmpty()) {
			
			for ( String str : list) {
				
				View view = MakeItemView(str);
				if(view !=null)
				{
					itemView.add(view);
				}

			}
		}
	}
	
	public ListAdapter(Context context, List<String> list,int flag) {
		// TODO Auto-generated constructor stub

		this.context = context;
		mediaUtil = MediaUtils.getInstance(context);
		dbManager = DbManager.getInstance(context);
		
		itemView = new ArrayList<View>();
		
		keyList = list;

		if (!list.isEmpty()) {
			
			for ( String str : list) {
				
				View view = MakeItemViewCollect(str);
				if(view !=null)
				{
					itemView.add(view);
				}

			}
		}
		
		
	}
	
	/**
	 * remoeveItem删除子item
	 * (这里描述这个方法适用条件 – 可选)
	 * @param key 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public void remoeveItem(String key)
	{
		
		if(keyList !=null && !keyList.isEmpty())
		{
			int index = keyList.indexOf(key);
			
			System.out.println(" key size = "+keyList.size()+"\t"+"viewList size = "+itemView.size());
			
			if(index !=-1)
			{
				keyList.remove(index);
				itemView.remove(index);
			}
			
			System.out.println(" key size = "+keyList.size()+"\t"+"viewList size = "+itemView.size());
		}
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		//return itemView.length;
		
		return itemView.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// if (convertView == null)
		// return itemView[position];

		return itemView.get(position);

		// return convertView;
	}

	/**
	 * @param mp3Info
	 * @return
	 */
	public View MakeItemView(String key) {
		
		Mp3Info mp3Info = mediaUtil.getMp3infoWithKey(key);
		
		if(mp3Info !=null)
		{

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View itemView = inflater.inflate(R.layout.list_item_view, null);

			ImageView imageView = (ImageView) itemView
					.findViewById(R.id.albumImage);
			imageView.setImageResource(R.drawable.defaultalbum);

			TextView fileName = (TextView) itemView.findViewById(R.id.fileName);
			
			if(mp3Info.getTitle().equals("NULL"))
			{
				File file = new File(mp3Info.getUrl());
				
			//	System.out.println(file.getName());
				fileName.setText(file.getName());
			}
			else
			{
				fileName.setText(mp3Info.getTitle());
			}

			TextView signerName = (TextView) itemView
					.findViewById(R.id.signerName);
			signerName.setText(mp3Info.getArtist());

			TextView duringTime = (TextView) itemView
					.findViewById(R.id.duringTime);
			// duringTime.setText(MediaUtils.formatTime(mp3Info.get));

			ImageButton imageButton = (ImageButton) itemView
					.findViewById(R.id.collectButton);

			if (dbManager.isCollect(mp3Info.getUrl())) {
				imageButton.setBackgroundResource(R.drawable.favoliten);

				imageButton.setOnClickListener(new MyImageButtonListener(
						mp3Info, true));
			} else {
				imageButton.setBackgroundResource(R.drawable.unfavoliten);

				imageButton.setOnClickListener(new MyImageButtonListener(
						mp3Info, false));
			}

			return itemView;
		}
		else
		{
			return null;
		}

	}
	
	public View MakeItemViewCollect(String key) {
		
		Mp3Info mp3Info = mediaUtil.getMp3infoWithKey(key);

		if (mp3Info != null) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View itemView = inflater.inflate(R.layout.list_item_view, null);

			ImageView imageView = (ImageView) itemView
					.findViewById(R.id.albumImage);
			imageView.setImageResource(R.drawable.defaultalbum);

			TextView fileName = (TextView) itemView.findViewById(R.id.fileName);
			fileName.setText(mp3Info.getTitle());

			TextView signerName = (TextView) itemView
					.findViewById(R.id.signerName);
			signerName.setText(mp3Info.getArtist());

			TextView duringTime = (TextView) itemView
					.findViewById(R.id.duringTime);
			// duringTime.setText(MediaUtils.formatTime(mp3Info.get));

			ImageButton imageButton = (ImageButton) itemView
					.findViewById(R.id.collectButton);

			imageButton.setBackgroundResource(R.drawable.favoliten);

			imageButton.setOnClickListener(new MyImageButtonListener(mp3Info,
					true));

			return itemView;
		} else {

			return null;
		}

	}
	
	
	public class MyImageButtonListener implements OnClickListener
	{
		private Mp3Info mp3Info = null;
		private boolean isCollect = false;
		public MyImageButtonListener( Mp3Info mp3Info,boolean status)
		{
			this.mp3Info = mp3Info;
			this.isCollect = status;
			
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(isCollect)
			{
				v.setBackgroundResource(R.drawable.unfavoliten);
				isCollect = false;
				
				DbManager dbManager = DbManager.getInstance(ListAdapter.this.context);
				dbManager.remoevCollect(mp3Info);
				dbManager.remoevCollectList(mp3Info.getUrl());
			}
			else
			{
				v.setBackgroundResource(R.drawable.favoliten);
				DbManager dbManager = DbManager.getInstance(ListAdapter.this.context);
				
				dbManager.insertCollect(mp3Info);
				dbManager.addCollectList(mp3Info.getUrl());
				
				isCollect = true;
			}
		}
		
	}

}
