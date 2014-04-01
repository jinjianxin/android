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
	
	/**
	 * 
	 * 创建一个新的实例 DialogAdapter. 弹出可选择的选项对话框
	 *
	 * @param context
	 */
	public DialogAdapter(Context context) {
		// TODO Auto-generated constructor stub
		
		this.context = context;
		viewList = new ArrayList<View>();
		
		viewList.add(makeViewItem(R.string.remove,R.drawable.dialog_menu_remove));
		viewList.add(makeViewItem(R.string.add,R.drawable.dialog_menu_add_to));
		viewList.add(makeViewItem(R.string.ring,R.drawable.dialog_menu_set_as_ringtone));
		viewList.add(makeViewItem(R.string.music_info,R.drawable.dialog_menu_media_info));

	}
	
	/**
	 * 
	 * 创建一个新的实例 重命名自定义列表构造函数
	 *
	 * @param context
	 * @param flag
	 */
	public DialogAdapter(Context context,boolean flag) {
		// TODO Auto-generated constructor stub
		
		this.context = context;
		viewList = new ArrayList<View>();
		
		viewList.add(makeViewItem(R.string.rename_delete,R.drawable.dialog_menu_remove));
		viewList.add(makeViewItem(R.string.rename_list,R.drawable.dialog_menu_rename));

	}
	
	
	/**
	 * 
	 * 创建一个新的实例 DialogAdapter. 显示自定义列表对话框
	 *
	 * @param context
	 * @param list
	 */
	public DialogAdapter(Context context,List<String> list) {
		// TODO Auto-generated constructor stub
		
		this.context = context;
		viewList = new ArrayList<View>();
		
		if (list != null && !list.isEmpty()) {

			for (String str : list) {
				viewList.add(makeViewItem(str));
			}
		}
		
	}
	
	/**
	 * 
	 * 创建一个新的实例 DialogAdapter. 设置铃声对话框
	 *
	 * @param context
	 * @param flag
	 */
	public DialogAdapter(Context context,int flag) {
		// TODO Auto-generated constructor stub
		
		this.context = context;
		viewList = new ArrayList<View>();
		
		viewList.add(makeViewItemRing(R.string.message_ring));
		viewList.add(makeViewItemRing(R.string.phone_ring));
		viewList.add(makeViewItemRing(R.string.all_ring));
		
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

	private View makeViewItem(int title,int id)
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
	
	
	private View makeViewItem(String title)
	{
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.playlist_dialog_item, null);
		
		TextView textView = (TextView) itemView.findViewById(R.id.playlist_item_text);
		
		textView.setText(title);
		
		return itemView;
	}
	
	private View makeViewItemRing(int id)
	{
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.playlist_dialog_item, null);
		
		TextView textView = (TextView) itemView.findViewById(R.id.playlist_item_text);
		
		textView.setText(id);
		
		return itemView;
	}
	
}
