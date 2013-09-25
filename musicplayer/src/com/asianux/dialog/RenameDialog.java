package com.asianux.dialog;

import com.asianux.musicplayer.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月23日 下午9:28:59
 * 类说明
 */

public class RenameDialog extends Dialog {

	private EditText editText = null;
	private Button cancelButton = null;
	private Button sureButton = null;
	
	public interface RenameDialogListener 
	{
		public void RenameDialogEvent(String name);
	}
	
	private RenameDialogListener renameDialogListener = null;
	private String str = null;
	
	public RenameDialog(Context context,String str,RenameDialogListener listener) {
		super(context);
		this.str = str;
		this.renameDialogListener = listener;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rename_dialog);
		
		setTitle(R.string.rename_list);
		
		editText = (EditText) findViewById(R.id.rename_edit_text);
		cancelButton = (Button) findViewById(R.id.rename_cancel);
		sureButton =(Button) findViewById(R.id.rename_sure);
		
		editText.setText(str);
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				dismiss();
				
			}
		});
		
		sureButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String tmpString = editText.getText().toString();
				if(tmpString == null || tmpString.isEmpty())
				{	
					tmpString = RenameDialog.this.str;
				}
				
				RenameDialog.this.renameDialogListener.RenameDialogEvent(tmpString);
				
				dismiss();
			}
		});
	}
	
}
