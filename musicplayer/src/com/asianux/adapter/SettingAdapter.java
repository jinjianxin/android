package com.asianux.adapter;

import java.util.ArrayList;
import java.util.List;

import com.asianux.musicplayer.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年10月6日 下午11:17:48
 * 类说明
 */

public class SettingAdapter extends BaseAdapter {
	
	
	private List<View> viewList = null;
	private Context context = null;
	
	public SettingAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		viewList = new ArrayList<View>();

		viewList.add(makeView(R.drawable.menu_add_song,R.string.menu_scan));
		viewList.add(makeView(R.drawable.menu_list_reapeat,R.string.menu_mode));
		viewList.add(makeView(R.drawable.menu_audio_effect,R.string.menu_effect));
		viewList.add(makeView(R.drawable.menu_change_skin,R.string.menu_skin));
		viewList.add(makeView(R.drawable.menu_sequence_play,R.string.menu_sleep));
		viewList.add(makeView(R.drawable.menu_download,R.string.menu_download));
		viewList.add(makeView(R.drawable.menu_setting,R.string.menu_setting));
		
		
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return viewList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		
		return viewList.get(arg0);
	}
	
	public View makeView(int icon,int text)
	{
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view = inflater.inflate(R.layout.my_view_item, null);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.item_icon);
		imageView.setScaleType(ScaleType.CENTER);
		imageView.setImageResource(icon);
		
		TextView textView = (TextView) view.findViewById(R.id.item_text);
		textView.setText(text);
		
		return view;
	}

}
