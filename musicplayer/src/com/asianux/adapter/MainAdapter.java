package com.asianux.adapter;

import java.util.ArrayList;
import java.util.List;

import com.asianux.musicplayer.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月19日 上午9:34:07
 * 类说明
 */

public class MainAdapter extends BaseAdapter{

	private Context context = null;
	private List<View> viewList = null;
	
	public MainAdapter(Context context)
	{
		this.context = context;
		viewList = new ArrayList<View>();
	}
	
	public String getViewId(int arg)
	{
		if (arg > viewList.size() || arg < 0) {
			return null;
		} else {
			
			System.out.println("viewList = "+viewList.size());

			View view = viewList.get(arg);

			TextView textView = (TextView) view.findViewById(R.id.itemText);
			return textView.getText().toString();
		}
	}
	
	public void setViewText(int position,String title)
	{
		View view = viewList.get(position);

		TextView textView = (TextView) view.findViewById(R.id.itemText);
		textView.setText(title);
	}
	
	public View getView(int position)
	{
		if(position>viewList.size() || position<0)
		{
			return null;
		}
		else
		{
			return viewList.get(position);
		}
	}
	
	public void removeView(int position )
	{
		if(position >=4 && position<=viewList.size())
		{
			viewList.remove(position);
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
	
	public void makeViewDefault(String title)
	{
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.main_layout_item, null);
		TextView textView = (TextView) itemView.findViewById(R.id.itemText);
		textView.setText(title);
		
		viewList.add(itemView);
	}
	
	public void makeView(String title)
	{
		
	}

}
