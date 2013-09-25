package com.asianux.dialog;

import com.asianux.adapter.DialogAdapter;
import com.asianux.dialog.RenameDialog.RenameDialogListener;
import com.asianux.musicplayer.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.test.RenamingDelegatingContext;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月22日 下午9:24:19
 * 类说明
 */

public class RenameListDialog extends Dialog{
	
	private Context context = null;
	private ListView listView = null;
	private String arg = null;
	
	 public interface RenameDialogEventListener {
	        public void RenameDialogEvent(int tag);
	    }
	
	 private RenameDialogEventListener renameDialogEventListener = null;
	 
	public RenameListDialog(Context context,String str,RenameDialogEventListener listener) {
		super(context);
		this.context =context;
		this.arg = str;
		this.renameDialogEventListener = listener;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rename_list_dialog);
		
		setTitle(arg);
		
		listView = (ListView) findViewById(R.id.rename_list_view);
		
		DialogAdapter dialogAdapter = new DialogAdapter(context,false);
		
		listView.setAdapter(dialogAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
					
				if(arg2 == 0)
				{			
					renameDialogEventListener.RenameDialogEvent(arg2);
					
					dismiss();
				}
				else
				{
					renameDialogEventListener.RenameDialogEvent(arg2);
					dismiss();
				}
					
			}
		});
	}



}
