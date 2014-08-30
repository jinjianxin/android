package com.linux.dialog;

import com.linux.monking.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class CalendarDialog extends Dialog {

	public CalendarDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.calendar_dialog);
	}	
}
