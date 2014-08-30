package com.linux.monking;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.linux.database.DbManager;
import com.linux.utils.FileUtils;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private EditText editText = null;
	private TextView incomeText = null;
	private TextView spendingText = null;
	private TextView profitText = null;
	private Button searchButton = null;
	private Button incomeButton = null;
	private Button spendingButton = null;
	private Button countButton = null;
	private Button detailButton = null;
	private Button settingButton = null;
	private DbManager dbManager = null;
	private Resources resources = null;
	private TextView profitTextTitle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_layout);
		
		FileUtils fileUtils = FileUtils.getInstance();
	
		dbManager = DbManager.getInstance(MainActivity.this);

		editText = (EditText) findViewById(R.id.main_edit_text);
		incomeText = (TextView) findViewById(R.id.main_income_text);
		spendingText = (TextView) findViewById(R.id.main_spending_text);
		profitText = (TextView) findViewById(R.id.main_profit_text);
		profitTextTitle = (TextView) findViewById(R.id.main_profit_title);
		
		searchButton = (Button) findViewById(R.id.main_search_button);
		
		incomeButton = (Button) findViewById(R.id.main_income_button);
		spendingButton = (Button) findViewById(R.id.main_spending_button);
		
		countButton = (Button) findViewById(R.id.main_count_button);
		detailButton = (Button) findViewById(R.id.main_detail_button);
		settingButton = (Button) findViewById(R.id.main_setting_button);
		
		resources = getResources();
		
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		incomeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent();
				intent.putExtra("flag", "income");
				intent.setClass(MainActivity.this, IncomeActivity.class);
				startActivity(intent);
			}
		});
		
		spendingButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent();
				intent.putExtra("flag", "spending");
				intent.setClass(MainActivity.this, IncomeActivity.class);
				startActivity(intent);
				
			}
		});
		
		countButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, StatsActivity.class);
				startActivity(intent);
			}
		});
		
		detailButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, DetailActivity.class);
				startActivity(intent);
				
			}
		});
		
		settingButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, SettingActivity.class);
				startActivity(intent);
				
			}
		});
		
	//	showData();
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
		showData();
		
	}
	
	private void setSumText(float incomeSum,float spendingSum)
	{
		String str = resources.getString(R.string.main_unit);
		str = String.format(str,String.valueOf(incomeSum));
		incomeText.setText(str);
		
		str = null;
		
		str = resources.getString(R.string.main_unit);
		str = String.format(str,String.valueOf(spendingSum));
		spendingText.setText(str);
		
		if(incomeSum-spendingSum >=0.0)
		{
			profitTextTitle.setText(R.string.main_profit);
		}
		else
		{
			profitTextTitle.setText(R.string.main_loss);
		}
			
		str = null;
		
		str = resources.getString(R.string.main_unit);
		str = String.format(str,String.valueOf(Math.abs(incomeSum-spendingSum)));
		profitText.setText(str);
	}
	
	private void setMonthSum()
	{	
		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		Date curDate = new Date(System.currentTimeMillis());
		String month = formatter.format(curDate);
		
		Intent intent = dbManager.getMonthSum(month);
		
		if(intent !=null)
		{
			String incomeSum = intent.getStringExtra("incomeSum");
			String spendingSum = intent.getStringExtra("spendingSum");
		
			setSumText(Float.parseFloat(incomeSum), Float.parseFloat(spendingSum));
		}
		else
		{
			setSumText(0, 0);
		}
	}
	
	private void setQuarterSum()
	{		
		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		Date curDate = new Date(System.currentTimeMillis());
		String month = formatter.format(curDate);
		
		Intent intent = dbManager.getMonthSum(month);
		
		if(intent !=null)
		{
			String incomeSum = intent.getStringExtra("incomeSum");
			String spendingSum = intent.getStringExtra("spendingSum");
		
			setSumText(Float.parseFloat(incomeSum), Float.parseFloat(spendingSum));
		}
		else
		{
			setSumText(0, 0);
		}
	}
	
	private void setYearSum()
	{	
		System.out.println("------------year");
		
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy");       
		Date curDate=new Date(System.currentTimeMillis());       
		String year = formatter.format(curDate);
	
		Intent intent = dbManager.getMonthSum(year);
		
		if(intent !=null)
		{
			String incomeSum = intent.getStringExtra("incomeSum");
			String spendingSum = intent.getStringExtra("spendingSum");
		
			setSumText(Float.parseFloat(incomeSum), Float.parseFloat(spendingSum));
		}
		else
		{
			setSumText(0, 0);
		}
	}
	
	private void showData()
	{
		SharedPreferences mPerferences = PreferenceManager
		        .getDefaultSharedPreferences(getApplicationContext());

		SharedPreferences.Editor mEditor = mPerferences.edit();

		String flag = mPerferences.getString("show", null);
		
		if(flag == null)
		{
			mEditor.putString("show", "month");
			mEditor.commit();
			
			setMonthSum();
		}
		else if(flag.equals("month"))
		{
			setMonthSum();
		}
		else if(flag.equals("quarter"))
		{
			setQuarterSum();
		}else if(flag.equals("year"))
		{
			setYearSum();
		}
	}
}
