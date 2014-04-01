package com.asianux.dialog;

import java.util.ArrayList;
import java.util.List;

import com.asianux.adapter.DialogAdapter;
import com.asianux.database.DbManager;
import com.asianux.musicplayer.R;
import com.asianux.utils.Mp3Info;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月21日 下午1:26:47
 * 类说明
 */

public class PlaylistDialog extends Dialog {
	
	private ListView listView = null;
	private Context context = null;
	private DbManager dbManager = null;
	private Mp3Info mp3Info = null;
	private List<String > list = null;
	
	private Button cancelButton = null;
	private Button newListButton = null;
	

	public PlaylistDialog(Context context,Mp3Info mp3Info) {
		super(context);
		this.context = context;
		this.mp3Info = mp3Info;
		list = new ArrayList<String>();
		
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist_dialog);
		
		setTitle(R.string.add_to);
		
		listView = (ListView) findViewById(R.id.playlist_dialog_listview);
		cancelButton = (Button) findViewById(R.id.playlist_cancel);
		newListButton = (Button) findViewById(R.id.playlist_newlist);
		
		dbManager = DbManager.getInstance(this.context);
		
		list = dbManager.getPlayList();
		
		DialogAdapter adapter = new DialogAdapter(this.context,list);
		
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				dbManager.insertListDb(list.get(arg2), PlaylistDialog.this.mp3Info.getUrl());				
				dismiss();
			}
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				/*dbManager.removeListDb(list.get(arg2));
				dbManager.removePlayList(list.get(arg2));
				dismiss();
				*/
				return false;
			}
		});
		
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		
		newListButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				NewListDialog dialog = new NewListDialog(PlaylistDialog.this.context,PlaylistDialog.this.mp3Info);
				dialog.show();
				
				dismiss();
				
				
			}
		});
		
	}
}
