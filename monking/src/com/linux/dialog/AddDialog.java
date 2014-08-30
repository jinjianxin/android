package com.linux.dialog;

import com.linux.monking.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddDialog  extends Dialog{
	
	private EditText editText = null;
	private Button sureButton = null;
	private Button cancelButton = null;
	
	private Context context = null;
	
	public interface AddDialogListener
	{
		void sure(String kind);
	}
	
	private AddDialogListener addDialogListener = null;

	public AddDialog(Context context,AddDialogListener listener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.addDialogListener = listener;
	}

	public AddDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context = context;
		
	}
	
/*	public void setTitle(int id)
	{
		AddDialog.this.setTitle("tyest");
	}
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_dialog);
		
		editText = (EditText) findViewById(R.id.add_edit_text);
		sureButton = (Button) findViewById(R.id.add_sure_button);
		cancelButton = (Button) findViewById(R.id.add_cancel_button);

		sureButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(editText.getText().toString().length()>0)
				{
					addDialogListener.sure(editText.getText().toString());
					
					dismiss();
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
