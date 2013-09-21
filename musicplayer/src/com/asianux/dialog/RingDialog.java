package com.asianux.dialog;

import com.asianux.adapter.DialogAdapter;
import com.asianux.musicplayer.R;
import com.asianux.utils.Mp3Info;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月21日 下午1:45:03
 * 类说明
 */

public class RingDialog extends Dialog {

	private ListView listView = null;
	private Button cancelButton = null;
	private Context context = null;
	private Mp3Info mp3Info = null;
	
	public RingDialog(Context context,Mp3Info mp3Info) {
		super(context);
		this.context = context;
		this.mp3Info = mp3Info;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ring_dialog);
		
		Resources resources = getContext().getResources();
		
		setTitle(R.string.ring_title);
		
		listView = (ListView) findViewById(R.id.ring_dialog_listview);
		cancelButton = (Button) findViewById(R.id.ringdialog_cancel);
		
		DialogAdapter dialogAdapter = new DialogAdapter(this.context, 0);
		
		listView.setAdapter(dialogAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				if(arg2 == 0)
				{
					System.out.println("Message Ring");
				}
				else if(arg2 == 1)
				{
					System.out.println("Phone Ring");
					
				}else if(arg2 == 2)
				{
					System.out.println("All Ring");
				}
				
			}
		});
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				dismiss();
				
			}
		});
		
		
	}

}
