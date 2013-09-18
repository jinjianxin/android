package com.asianux.adapter;

import java.util.ArrayList;
import java.util.List;

import com.asianux.musicplayer.R;
import com.asianux.utils.MediaUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月17日 下午10:00:53
 * 类说明
 */

public class SingerAdapter extends BaseAdapter {
	private Context context = null;
	private List<View> viewList = null;
	
	public SingerAdapter(Context context) {
		// TODO Auto-generated constructor stub
		

		viewList = new ArrayList<View>();

		this.context = context;

		MediaUtils mediaUtils = MediaUtils.getInstance(context);

		List<String > list = mediaUtils.getSingerList();
		
		System.out.println("*****\t"+list.size());
		
		for (String string : list) {
			View view = MakeItemView(string);

			viewList.add(view);
		}
		
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

	public View MakeItemView(String title) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.singer_view_item, null);

		TextView textView = (TextView) itemView.findViewById(R.id.singerText);

		System.out.println(title);
		
		textView.setText(title);

		return itemView;

	}

	
}
