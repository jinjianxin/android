package com.asianux.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


import com.asianux.adapter.DialogAdapter;
import com.asianux.musicplayer.R;
import com.asianux.utils.Mp3Info;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月21日 下午12:28:39
 * 类说明
 */

public class RemoveDialog extends Dialog{
	
	private Mp3Info mp3Info = null;
	private Context context = null;
	
	private TextView tipText = null;
	private TextView checkTipText = null;
	private CheckBox checkBox = null;
	private Button cancelButton = null;
	private Button sureButton = null;

	public RemoveDialog(Context context) {
		super(context);
		this.context = context;
		
		// TODO Auto-generated constructor stub
	}
	
	public RemoveDialog(Context context,Mp3Info mp3Info) {
		super(context);
		
		this.context = context;
		this.mp3Info = mp3Info;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remove_dialog);
		
		Resources resource  = getContext().getResources();
		
		setTitle(R.string.remove_music);
		
		tipText = (TextView) findViewById(R.id.remove_dialog_tip);
		checkTipText = (TextView) findViewById(R.id.remove_dialog_text);
		checkBox = (CheckBox) findViewById(R.id.remove_dialog_checkbox);
		cancelButton = (Button) findViewById(R.id.remove_dialog_cancel);
		sureButton = (Button) findViewById(R.id.remove_dialog_surce);
		
		String str = resource.getString(R.string.confirm_delete);
		str = String.format(str, mp3Info.getTitle());
		
		tipText.setText(str);

		cancelButton.setOnClickListener(new  View.OnClickListener() {
			
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
				
				if(checkBox.isChecked())
				{
					System.out.println("on checked \n");
					
				}else
				{
					System.out.println("no checked \n");
				}
				
			}
		});

		
	}

}
