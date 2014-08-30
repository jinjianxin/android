package com.linux.setting;

import com.linux.monking.R;
import com.linux.monking.R.id;
import com.linux.monking.R.layout;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DateSelectActivity extends Activity implements OnCheckedChangeListener{
	
	private CheckBox monthCheckBox = null;
	private CheckBox quarterCheckBox = null;
	private CheckBox yearCheckBox = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.date_select_layout);
		
		monthCheckBox = (CheckBox) findViewById(R.id.date_select_month_checkbox);
		quarterCheckBox = (CheckBox) findViewById(R.id.date_select_quarter_checkbox);
		yearCheckBox = (CheckBox) findViewById(R.id.date_select_year_checkbox);
		
		
		monthCheckBox.setChecked(true);
		
		monthCheckBox.setOnCheckedChangeListener(this);
		quarterCheckBox.setOnCheckedChangeListener(this);
		yearCheckBox.setOnCheckedChangeListener(this);
		
	}


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub

		if(isChecked)
		{
			if(buttonView == monthCheckBox)
			{
				monthCheckBox.setChecked(true);
				quarterCheckBox.setChecked(false);
				yearCheckBox.setChecked(false);
				
				SharedPreferences mPerferences = PreferenceManager
				        .getDefaultSharedPreferences(getApplicationContext());

				SharedPreferences.Editor mEditor = mPerferences.edit();

				mEditor.putString("show", "month");
				mEditor.commit();
				
			}else if(buttonView == quarterCheckBox)
			{
				quarterCheckBox.setChecked(true);
				monthCheckBox.setChecked(false);
				yearCheckBox.setChecked(false);
				
				SharedPreferences mPerferences = PreferenceManager
				        .getDefaultSharedPreferences(getApplicationContext());

				SharedPreferences.Editor mEditor = mPerferences.edit();

				mEditor.putString("show", "quarter");
				mEditor.commit();
			}
			else if(buttonView == yearCheckBox)
			{
				yearCheckBox.setChecked(true);
				quarterCheckBox.setChecked(false);
				monthCheckBox.setChecked(false);
				
				SharedPreferences mPerferences = PreferenceManager
				        .getDefaultSharedPreferences(getApplicationContext());

				SharedPreferences.Editor mEditor = mPerferences.edit();

				mEditor.putString("show", "year");
				mEditor.commit();
				
			}
		}	
	}
}
