package com.asianux.dialog;

import com.asianux.musicplayer.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月22日 下午9:54:12
 * 类说明
 */

public class ConfirmDialog extends Dialog{
	
	private Context context = null;
	private String arg = null;
	
	private Button sureButton = null;
	private Button cancelButton = null;
	
	 public interface ConfirmDialogEventListener {
	        public void confirmDialogEvent(int tag);
	    }

	 private ConfirmDialogEventListener confirmDialogEventListener = null;
	 
	public ConfirmDialog(Context context,String str) {
		super(context);
		
		this.context = context;
		this.arg = str;
		// TODO Auto-generated constructor stub
	}
	
	public ConfirmDialog(Context context,String str,ConfirmDialogEventListener listener) {
		super(context);
		
		this.context = context;
		this.arg = str;
		this.confirmDialogEventListener = listener;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_dialog);
		
		setTitle(R.string.rename_list);
		
		cancelButton = (Button) findViewById(R.id.rename_cancel);
		sureButton = (Button) findViewById(R.id.rename_sure);
		
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
				
				confirmDialogEventListener.confirmDialogEvent(0);
			
				dismiss();
				
			}
		});
	}

}
