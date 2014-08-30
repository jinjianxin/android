package com.linux.view;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.linux.database.DbManager;
import com.linux.monking.R;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class StatsView {
	
	private TextView incomeTitle = null;
	private TextView incomeText = null;
	private TextView incomeCountText = null;
	private TextView incomeCountTitle = null;
	private TextView spendingText = null;
	private TextView spendingTitle = null;
	private TextView spendingCountText = null;
	private TextView spendingCountTitle = null;
	private TextView sumText = null;
	private TextView sumTitle = null;
	private String flag = null;
	private Resources resources = null;
	private DbManager dbManager = null;
	
	private Context context = null;
	
	public StatsView(Context context)
	{
		this.context = context;
		dbManager = DbManager.getInstance(context);
		resources  =context.getResources();
	}
	
	public View getView(LayoutInflater inflater,String flag)
	{
		this.flag = flag;
		
		View view = inflater.inflate(R.layout.stats_item_layout, null);
		
		incomeTitle =(TextView) view.findViewById(R.id.stats_item_income_title);
		incomeText = (TextView) view.findViewById(R.id.stats_item_income_text);
		
		incomeCountText = (TextView) view.findViewById(R.id.stats_item_income_count_text);
		incomeCountTitle = (TextView) view.findViewById(R.id.stats_item_income_count_title);
		
		spendingText = (TextView) view.findViewById(R.id.stats_item_spending_text);
		spendingTitle = (TextView) view.findViewById(R.id.stats_item_spending_title);
		
		spendingCountText = (TextView) view.findViewById(R.id.stats_item_spending_count_text);
		spendingCountTitle = (TextView) view.findViewById(R.id.stats_item_spending_count_title);
		
		sumText = (TextView) view.findViewById(R.id.stats_item_sum_text);
		sumTitle = (TextView) view.findViewById(R.id.stats_item_sum_title);
		
		if(flag.equals("month"))
		{
			incomeTitle.setText(R.string.stats_month_income);
			incomeCountTitle.setText(R.string.stats_month_income_count);
			spendingTitle.setText(R.string.stats_month_spending);
			spendingCountTitle.setText(R.string.stats_month_spending_count);
			sumTitle.setText(R.string.stats_month_profit);

			
		}else if(flag.equals("quarter"))
		{
			incomeTitle.setText(R.string.stats_quarter_income);
			incomeCountTitle.setText(R.string.stats_quarter_income_count);
			spendingTitle.setText(R.string.stats_quarter_spending);
			spendingCountTitle.setText(R.string.stats_quarter_spending_count);
			sumTitle.setText(R.string.stats_quarter_profit);

		}else if(flag.equals("year"))
		{
			incomeTitle.setText(R.string.stats_year_income);
			incomeCountTitle.setText(R.string.stats_year_income_count);
			spendingTitle.setText(R.string.stats_year_spending);
			spendingCountTitle.setText(R.string.stats_year_spending_count);
			sumTitle.setText(R.string.stats_year_profit);
	
		}
	
		if (flag.equals("month")) {

			getMonthSum();

		} else if (flag.equals("quarter")) {

			getquarterSum();

		} else if (flag.equals("year")) {

			getYearSum();

		}
		
		return view;
		
	}
	

	private void getMonthSum() {
		// TODO Auto-generated method stub

		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		Date curDate = new Date(System.currentTimeMillis());
		String month = formatter.format(curDate);

		Intent intent = dbManager.getMonthSum(month);

		if (intent != null) {

			String str = resources.getString(R.string.main_unit);
			
			setDefault(intent);

			str = intent.getStringExtra("profit");

			if (Float.parseFloat(str) > 0.0) {
				sumTitle.setText(R.string.stats_month_profit);
			} else {
				sumTitle.setText(R.string.stats_month_loss);
			}

			str = null;
			str = resources.getString(R.string.main_unit);
			str = String.format(str, intent.getStringExtra("profit"));
			sumText.setText(str);
			str = null;
		}else
		{
			setDefault();
		}

	}
	
	private void getquarterSum() {
		// TODO Auto-generated method stub
		SimpleDateFormat formatter = new SimpleDateFormat ("MM");       
		Date curDate=new Date(System.currentTimeMillis());       
		String month = formatter.format(curDate);
		
		Intent intent = dbManager.getQuarterSum(month);

		if (intent != null) 
		{
			String str = resources.getString(R.string.main_unit);
			
			setDefault(intent);
			
			str = null;

			str = intent.getStringExtra("profit");

			if (Float.parseFloat(str) > 0.0) {
				sumTitle.setText(R.string.stats_quarter_profit);
			} else {
				sumTitle.setText(R.string.stats_quarter_loss);
			}

			str = null;
			str = resources.getString(R.string.main_unit);
			str = String.format(str, intent.getStringExtra("profit"));
			sumText.setText(str);
			str = null;

		} else {
			setDefault();
		}
	}

	private void getYearSum() {
		
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy");       
		Date curDate=new Date(System.currentTimeMillis());       
		String year = formatter.format(curDate);
	
		Intent intent = dbManager.getYearSum(year);
		
		if(intent !=null)
		{
			String str = resources.getString(R.string.main_unit);
			
			setDefault(intent);
			
			str = null;

			str = intent.getStringExtra("profit");

			if (Float.parseFloat(str) > 0.0) {
				sumTitle.setText(R.string.stats_year_profit);
			} else {
				sumTitle.setText(R.string.stats_year_loss);
			}

			str = null;
			str = resources.getString(R.string.main_unit);
			str = String.format(str, intent.getStringExtra("profit"));
			sumText.setText(str);
		}
		else
		{
			setDefault();
		}

	}
	
	private void setDefault()
	{
		String str = resources.getString(R.string.main_unit);
		str = String.format(str,"0");
		incomeText.setText(str);
		spendingText.setText(str);
		sumText.setText(str);
		
		str = null;
		str = resources.getString(R.string.main_unit_count);
		str = String.format(str, "0");
		incomeCountText.setText(str);
		spendingCountText.setText(str);
	}
	
	private void setDefault(Intent intent)
	{
		String str = resources.getString(R.string.main_unit);

		str = String.format(str, intent.getStringExtra("incomeSum"));
		incomeText.setText(str);
		str = null;

		str = resources.getString(R.string.main_unit);
		str = String.format(str, intent.getStringExtra("spendingSum"));
		spendingText.setText(str);
		str = null;

		str = resources.getString(R.string.main_unit);
		str = String.format(str, "0");
		sumText.setText(str);
		str = null;

		str = resources.getString(R.string.main_unit_count);
		str = String.format(str, intent.getStringExtra("incomeCount"));
		incomeCountText.setText(str);
		str = null;

		str = resources.getString(R.string.main_unit_count);
		str = String.format(str, intent.getStringExtra("spendingCount"));
		spendingCountText.setText(str);
		str = null;
	}
	
}
