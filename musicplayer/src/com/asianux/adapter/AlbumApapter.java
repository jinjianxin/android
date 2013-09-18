package com.asianux.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.asianux.musicplayer.R;
import com.asianux.utils.MediaUtils;
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
 * @version 创建时间：2013年9月16日 下午9:46:36 类说明
 */

public class AlbumApapter extends BaseAdapter {

	private Context context = null;
	private List<View> viewList = null;

	public AlbumApapter(Context context) {

		viewList = new ArrayList<View>();

		this.context = context;

		MediaUtils mediaUtils = MediaUtils.getInstance(context);

		List<String > list = mediaUtils.getAlbumList();
		
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

		View itemView = inflater.inflate(R.layout.album_view_item, null);

		TextView textView = (TextView) itemView.findViewById(R.id.albumText);

		textView.setText(title);

		return itemView;

	}

}
