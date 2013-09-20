package com.asianux.adapter;

import java.util.ArrayList;
import java.util.List;

import com.asianux.musicplayer.R;
import com.asianux.utils.Mp3Info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月20日 上午10:03:52
 * 类说明
 */

public class DialogAdapter extends BaseAdapter {
	
	private Context context = null;
	private List<View > viewList = null;
	
	public DialogAdapter(Context context) {
		// TODO Auto-generated constructor stub
		
		this.context = context;
		viewList = new ArrayList<View>();
		
		viewList.add(makeCiewItem(R.string.remove,R.drawable.dialog_menu_remove));
		viewList.add(makeCiewItem(R.string.add,R.drawable.dialog_menu_add_to));
		viewList.add(makeCiewItem(R.string.ring,R.drawable.dialog_menu_set_as_ringtone));
		viewList.add(makeCiewItem(R.string.music_info,R.drawable.dialog_menu_media_info));

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
		// TODO Auto-generated method stub
		return viewList.get(arg0);
	}

	private View makeCiewItem(int title,int id)
	{
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.add_dialog_item, null);
		
		ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
		imageView.setImageResource(id);
		
		TextView textView = (TextView) itemView.findViewById(R.id.textView);
		
		textView.setText(title);
		
		return itemView;
	}
	
}
