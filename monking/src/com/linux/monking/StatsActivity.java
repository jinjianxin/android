package com.linux.monking;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.linux.adapter.ViewPagerAdapter;
import com.linux.database.DbManager;
import com.linux.view.StatsView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class StatsActivity extends Activity {
	
	private ViewPager viewPager;
	private ImageView imageView;
	private TextView monthTextView = null;
	private TextView quarterTextView = null;
	private TextView yearTextView = null; 
	private List<View> views;
	private int offset = 0;
	private int currIndex = 0;
	private int bmpW;
	private View monthView = null;
	private View quarterView = null;
	private View yearView = null;
	private DbManager dbManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.stats_layout);
		
		dbManager = DbManager.getInstance(this);
		
		imageView= (ImageView) findViewById(R.id.ststs_cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / 3 - bmpW) / 2;
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);
		
		monthTextView = (TextView) findViewById(R.id.stats_month_text);		
		quarterTextView = (TextView) findViewById(R.id.stats_quarter_text);
		yearTextView = (TextView) findViewById(R.id.stats_year_text);

		monthTextView.setOnClickListener(new MyOnClickListener(0));
		quarterTextView.setOnClickListener(new MyOnClickListener(1));
		yearTextView.setOnClickListener(new MyOnClickListener(2));
		
		viewPager=(ViewPager) findViewById(R.id.vPager);
		views=new ArrayList<View>();
		LayoutInflater inflater=getLayoutInflater();
		
		StatsView statsView = new StatsView(this);
		monthView = statsView.getView(inflater,"month");
	
		quarterView =statsView.getView(inflater, "quarter");
		yearView = inflater.inflate(R.layout.stats_item_layout, null);
		yearView = statsView.getView(inflater, "year");
		
		views.add(monthView);
		views.add(quarterView);
		views.add(yearView);
				
		viewPager.setAdapter(new ViewPagerAdapter(views));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    public class MyOnPageChangeListener implements OnPageChangeListener{

    	int one = offset * 2 + bmpW;
		int two = one * 2;
		public void onPageScrollStateChanged(int arg0) {
			
			
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
			
		}

		public void onPageSelected(int arg0) {
			
			System.out.println("*************************");
			
			Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			imageView.startAnimation(animation);
		}
    	
    }
    
    private class MyOnClickListener implements OnClickListener{
        private int index=0;
        public MyOnClickListener(int i){
        	index=i;
        }
		public void onClick(View v) {
			viewPager.setCurrentItem(index);			
		}
		
	}
/*    
    private void initMonthItem()
    {
    	TextView incomeTitle = null;
    	TextView incomeText = null;
    	TextView incomeCountText = null;
    	TextView incomeCountTitle = null;
    	TextView spendingText = null;
    	TextView spendingTitle = null;
    	TextView spendingCountText = null;
    	TextView spendingCountTitle = null;
    	TextView sumText = null;
    	TextView sumTitle = null;
    	
		incomeTitle =(TextView) monthView.findViewById(R.id.stats_item_income_title);
		incomeText = (TextView) monthView.findViewById(R.id.stats_item_income_text);
		
		incomeCountText = (TextView) monthView.findViewById(R.id.stats_item_income_count_text);
		incomeCountTitle = (TextView) monthView.findViewById(R.id.stats_item_income_count_title);
		
		spendingText = (TextView) monthView.findViewById(R.id.stats_item_spending_text);
		spendingTitle = (TextView) monthView.findViewById(R.id.stats_item_spending_title);
		
		spendingCountText = (TextView) monthView.findViewById(R.id.stats_item_spending_count_text);
		spendingCountTitle = (TextView) monthView.findViewById(R.id.stats_item_spending_count_title);
		
		sumText = (TextView) monthView.findViewById(R.id.stats_item_sum_text);
		sumTitle = (TextView) monthView.findViewById(R.id.stats_item_sum_title);
		
		incomeTitle.setText(R.string.stats_month_income);
		incomeCountTitle.setText(R.string.stats_month_income_count);
		spendingTitle.setText(R.string.stats_month_spending);
		spendingCountTitle.setText(R.string.stats_month_spending_count);
		sumTitle.setText(R.string.stats_month_profit);
		
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy");       
		Date curDate=new Date(System.currentTimeMillis());       
		String year = formatter.format(curDate);
	
		Intent intent = dbManager.getYearSum(year);
		
    }*/
    
}

