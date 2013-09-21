package com.asianux.dialog;

import com.asianux.database.DbManager;
import com.asianux.musicplayer.R;
import com.asianux.utils.Mp3Info;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月21日 下午8:31:02
 * 类说明
 */

public class NewListDialog extends Dialog{
	
	private Context context = null;
	private Mp3Info mp3Info = null;
	
	private EditText editText = null;
	private Button cancelButton = null;
	private Button sureButton = null;
	private Resources resource = null;
	private String name = null;

	public NewListDialog(Context context,Mp3Info mp3Info) {
		super(context);
		this.context = context;
		this.mp3Info = mp3Info;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_list_dialog);
		
		editText = (EditText) findViewById(R.id.newlist_edit_text);
		
		cancelButton = (Button) findViewById(R.id.newlist_cancel);
		sureButton = (Button) findViewById(R.id.newlist_sure);
		
		resource  = getContext().getResources();
		DbManager dbManager = DbManager.getInstance(NewListDialog.this.context);
		
		String str = resource.getString(R.string.new_default_list);
	
		for(int i = 1;i<500;i++)
		{
			name = str+i;
	
			if(dbManager.findPlayList(name))
			{
				break;
			}			
			
			name = null;
		}
		
		editText.setHint(name);
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
				
			}
		});
		
		sureButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String tmpString = editText.getText().toString();
				if(tmpString == null || tmpString.isEmpty())
				{	
					tmpString = name;
				}
				
				DbManager dbManager = DbManager.getInstance(NewListDialog.this.context);
				
				dbManager.insertPlaylistDb(tmpString);
				dbManager.addPlayList(tmpString);
				dbManager.insertListDb(tmpString, NewListDialog.this.mp3Info.getUrl());
				
				dismiss();
				
			}
		});
		
	}

}
