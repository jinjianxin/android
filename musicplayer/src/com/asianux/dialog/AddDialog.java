package com.asianux.dialog;

import com.asianux.adapter.DialogAdapter;
import com.asianux.musicplayer.R;
import com.asianux.utils.Mp3Info;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
 
/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月20日 上午9:14:44
 * 类说明
 */

public class AddDialog extends Dialog {
	
	private Context context = null;
	private ListView listView = null;
	private Mp3Info mp3Info = null;
	
	private static int FLAG_REMOVE = 0;
	private static int FLAG_ADD = 1;
	private static int FLAG_RING = 2;
	private static int FLAG_INFO =  3;
	
	public interface AddDialogListener
	{
		public void removedialog(Mp3Info mp3Info);
	}
	
	private AddDialogListener addDialogListener = null;

	public AddDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public AddDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	/*
	public AddDialog(Context context,Mp3Info mp3Info) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mp3Info = mp3Info;
	}
	*/
	
	public AddDialog(Context context,Mp3Info mp3Info,AddDialogListener listener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mp3Info = mp3Info;
		this.addDialogListener = listener;
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.add_dialog);
		
		super.onCreate(savedInstanceState);
		
		setTitle(mp3Info.getTitle());
		
		listView = (ListView) findViewById(R.id.listView);
		
		DialogAdapter dialogAdapter = new DialogAdapter(context);
		
		listView.setAdapter(dialogAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			
				if(arg2 == FLAG_REMOVE)
				{
					/*RemoveDialog dialog = new RemoveDialog(AddDialog.this.context,AddDialog.this.mp3Info);
					dialog.show();*/
					
					AddDialog.this.addDialogListener.removedialog(mp3Info);
					
					dismiss();
					
				}else if(arg2 == FLAG_ADD)
				{
					PlaylistDialog dialog = new PlaylistDialog(AddDialog.this.context,AddDialog.this.mp3Info);
					dialog.show();
					
					dismiss();
					
				}else if(arg2 == FLAG_RING)
				{
					RingDialog dialog = new RingDialog(AddDialog.this.context,AddDialog.this.mp3Info);
					dialog.show();
					
					dismiss();
					
					
				}else if(arg2 == FLAG_INFO)
				{
					MusicInfoDialog dialog = new MusicInfoDialog(AddDialog.this.context,AddDialog.this.mp3Info.getUrl());
					dialog.show();
					dismiss();
				}
				
				
			}
		});
	
		
	}

}
