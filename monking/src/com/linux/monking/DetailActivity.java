package com.linux.monking;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.linux.adapter.MySpinnerAdapter;
import com.linux.adapter.ViewPagerAdapter;
import com.linux.database.DbManager;
import com.linux.dialog.CalendarDialog;
import com.linux.monking.StatsActivity.MyOnPageChangeListener;
import com.linux.view.CalendarView;
import com.linux.view.Cell;
import com.linux.view.DetailView;
import com.linux.view.StatsView;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.linux.view.CalendarView.OnCellTouchListener;

public class DetailActivity extends Activity implements CalendarView.OnCellTouchListener
{
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
	
	private TextView startTimeText = null;
	private TextView endTimeText = null;
	private Button searchButton = null;
	private RelativeLayout relativeLayout = null;
	private CalendarView calendarView = null;
	private Rect ecBounds;
	private PopupWindow popupWindow = null;
	private boolean switcher = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.detail_layout);
		
		dbManager = DbManager.getInstance(this);
		imageView= (ImageView) findViewById(R.id.detail_cursor);
		
		startTimeText = (TextView) findViewById(R.id.detail_start_text_view);
		endTimeText = (TextView) findViewById(R.id.detail_end_text_view);
		searchButton = (Button) findViewById(R.id.detail_search_button);
		relativeLayout = (RelativeLayout) findViewById(R.id.detail_relative_layout);
		
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / 3 - bmpW) / 2;
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);
		
		monthTextView = (TextView) findViewById(R.id.detail_month_text);		
		quarterTextView = (TextView) findViewById(R.id.detail_quarter_text);
		yearTextView = (TextView) findViewById(R.id.detail_year_text);

		monthTextView.setOnClickListener(new MyOnClickListener(0));
		quarterTextView.setOnClickListener(new MyOnClickListener(1));
		yearTextView.setOnClickListener(new MyOnClickListener(2));
		
		viewPager=(ViewPager) findViewById(R.id.detail_vPager);
		views=new ArrayList<View>();
		LayoutInflater inflater=getLayoutInflater();
			
		DetailView detailView = new DetailView(this);
		monthView = detailView.makeDetailView(inflater,"month");
		quarterView = detailView.makeDetailView(inflater,"quarter");
		yearView = detailView.makeDetailView(inflater, "year");
		
		views.add(monthView);
		views.add(quarterView);
		views.add(yearView);
		
		viewPager.setAdapter(new ViewPagerAdapter(views));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
		startTimeText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
	/*			
				CalendarDialog calendarDialog = new CalendarDialog(DetailActivity.this);
				calendarDialog.show();*/
				
				switcher = true;
				showCalendarView(arg0);

			}
		});
		
		endTimeText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switcher = false;
				showCalendarView(v);
				
			}
		});
		
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				launchResultSearch();
				
			}
		});
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());
		String str = formatter.format(curDate);

		startTimeText.setText(str);
		endTimeText.setText(str);
		
	}
	
	
    public class MyOnPageChangeListener implements OnPageChangeListener{

    	int one = offset * 2 + bmpW;
		int two = one * 2;
		public void onPageScrollStateChanged(int arg0) {
			
			
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
			
		}

		public void onPageSelected(int arg0) {			
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
    
    private void showCalendarView(View v)
    {
		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.calendar_dialog, null);

		calendarView = (CalendarView) layout.findViewById(R.id.calendar_dialog_calendar_view);
	
		Button prevButton = (Button) layout.findViewById(R.id.calendar_dialog_prev);
		Button nextButton = (Button) layout.findViewById(R.id.calendar_dialog_next);
		
	
		popupWindow = new PopupWindow(v);

		popupWindow.setWidth(relativeLayout.getWidth());

		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);

		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		popupWindow.setOutsideTouchable(true);

		popupWindow.setFocusable(true);

		popupWindow.setContentView(layout);

		popupWindow.showAsDropDown(v, -startTimeText.getWidth() - 2, 0);
		
		calendarView.setOnCellTouchListener(this);
		
		prevButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(calendarView !=null)
				{
					calendarView.previousMonth();
				}
			}
		});
		
		nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(calendarView !=null)
				{
					calendarView.nextMonth();
				}
				
			}
		});
		
    }

	@Override
	public void onTouch(Cell cell) {
		// TODO Auto-generated method stub
		Intent intent = DetailActivity.this.getIntent();
		String action = intent.getAction();
		
		if(cell.mPaint.getColor() == Color.GRAY) {
			calendarView.previousMonth(); 
		} else if(cell.mPaint.getColor() == Color.LTGRAY) {
			calendarView.nextMonth(); 

		} else {  //  本月的
			
			String str = String.format("%s-%s-%s", calendarView.getYear(),calendarView.getMonth(),cell.getDayOfMonth());
			
			if(switcher)
			{
				startTimeText.setText(str);
			}else
			{
				endTimeText.setText(str);
			}
			
			popupWindow.dismiss();
			popupWindow = null;
			calendarView = null;
			
		}
	}
   
	private void launchResultSearch()
	{	
		long startTime = getTimeToLong(startTimeText.getText().toString());
		long endTime = getTimeToLong(endTimeText.getText().toString());
		
		Intent intent = new Intent();
		intent.putExtra("startTime", startTime);
		intent.putExtra("endTime", endTime);
		intent.setClass(this, SearchActivity.class);
		startActivity(intent);
	}
	
	
	private long getTimeToLong(String time)
	{
		String[] date = time.split("\\-");
		
		Date cuttentDate = new Date(Integer.parseInt(date[0]),Integer.parseInt(date[1]) , Integer.parseInt(date[2]));
		
		return cuttentDate.getTime();
	}
	
	
	
}