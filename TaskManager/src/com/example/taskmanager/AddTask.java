package com.example.taskmanager;



import java.io.InputStream;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.MySpinnerAdapter;
import com.example.database.DbManager;
import com.example.utils.Cell;
import com.example.utils.LogHelp;
import com.example.utils.TaskData;
import com.example.view.CalendarView;
import com.example.view.CalendarView.OnCellTouchListener;

public class AddTask extends Activity implements CalendarView.OnCellTouchListener{

	private TextView titleView = null;

	private PopupWindow popupWindow = null;
	private LinearLayout kindLayout = null;
	private TextView kindText = null;
	private Button kindButton = null;

	private EditText numberEditText = null;

	private TextView startDateText = null;
	private TextView endDateText = null;
	
	private Button sureButton = null;

	private Resources resources = null;
	private String flag = null;
	private List<String> kindList = null;
	private List<String> unitList = null;

	private DbManager dbManager = null;

	private CalendarView calendarView = null;
	private LinearLayout linearLayout = null;
	private boolean switcher = true;
	
	private ArrayAdapter<String> adapter = null;

	private MySpinnerAdapter mySpinnerAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		
		kindList = new ArrayList<String>();
		unitList = new ArrayList<String>();

		Intent intent = getIntent();

		flag = intent.getStringExtra("flag");

		setContentView(R.layout.addtask_layout);

		dbManager = DbManager.getInstance(getApplicationContext());
		
		titleView = (TextView) findViewById(R.id.income_title);

		kindLayout = (LinearLayout) findViewById(R.id.income_kind_spinner_layout);
		kindText = (TextView) findViewById(R.id.income_kind_spinner_text_view);
		kindButton = (Button) findViewById(R.id.income_kind_spinner_select_button);

		numberEditText = (EditText) findViewById(R.id.income_number_editText);


		startDateText = (TextView) findViewById(R.id.start_date_text);
		endDateText = (TextView) findViewById(R.id.end_date_text);
		
		
		sureButton = (Button) findViewById(R.id.income_sure_button);
		
		linearLayout = (LinearLayout) findViewById(R.id.income_linear_layout4);

		resources = getResources();

		if (flag.equals("income")) {
			titleView.setText(R.string.income_title);

			setDate();
			setSumDefault();
		} else if (flag.equals("spending")) {
			titleView.setText(R.string.spending_title);

			setDate();
			setSumDefault();

		} else if (flag.equals("details")) {
			
		}
		else if(flag.equals("task"))
		{
			TaskData data = (TaskData) getIntent().getSerializableExtra("taskData");
			if(data!=null)
			{
				setDefaultData(data);
			}
		}

		initIncome();
	
		initUnitIncome();


		kindButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showKindView(v);
			}
		});



		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!flag.equals("task")) {
					if (checkEmpty()) {
						
						
						String priority = kindText.getText().toString();
						String taskDes = numberEditText.getText().toString();
						String startTime = startDateText.getText().toString();
						String endTime = endDateText.getText().toString();
						
						LogHelp.logPutPut("startTime = "+startTime);
						LogHelp.logPutPut("endtime = "+endTime);
						
						
						LogHelp.logPutPut(priority);
						LogHelp.logPutPut(taskDes);
						LogHelp.logPutPut(startTime);
						LogHelp.logPutPut(endTime);
						
						long day = 1;
						
						try {
						
							java.util.Date date1 = new SimpleDateFormat("yyyy-mm-dd").parse(startTime);
							java.util.Date date2 = new SimpleDateFormat("yyyy-mm-dd").parse(endTime);
							
							day = (date1.getTime()-date2.getTime())/(24*60*60*1000)>0 ? (date1.getTime()-date2.getTime())/(24*60*60*1000): 
								   (date2.getTime()-date1.getTime())/(24*60*60*1000); 
							
						
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					
						if(day<=0.0f)
						{
							day+=1.0f;
						}
						
						TaskData taskData = new TaskData(taskDes, Integer.parseInt(priority), String.valueOf(day), startTime, endTime);
						dbManager.insertDataItem(taskData); 

						finish();
					} else {
						Toast.makeText(AddTask.this,
								R.string.input_error, Toast.LENGTH_SHORT)
								.show();
					}
					
				} 
				
				else {
					finish();
				}
			}
		});
		
		
		startDateText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switcher = true;
				showCalendarView(v);
			}
		});
		
		endDateText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switcher = false;
				showCalendarView(v);
			}
		});

	}
	
	private void setDefaultData(TaskData data)
	{
		
		titleView.setText(R.string.income_title_new);
		
		kindText.setText(String.valueOf(data.getmPriority()));
		kindButton.setEnabled(false);
		
		numberEditText.setText(data.getmTitle());
		numberEditText.setEnabled(false);
		
		startDateText.setText(data.getmStartTime());
		startDateText.setEnabled(false);
		
		endDateText.setText(data.getmStartTime());
		endDateText.setEnabled(false);
	
	}

	private boolean checkEmpty() {

		if ((kindText.getText().toString().length() > 0)
				&& (numberEditText.getText().toString().length() > 0)
				&& startDateText.getText().length() > 0
				&& endDateText.getText().length()>0) {
			
			return true;
	
			
		} else {
			return false;
		}

	}

	private String covertString() {
		return kindText.getText().toString() + "\t"
				+ numberEditText.getText().toString() + "\t"
				+ startDateText.getText().toString() + "\t"
				;
	}

	private void setDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());
		String str = formatter.format(curDate);

		startDateText.setText(str);
		endDateText.setText(str);
	}

	private String getDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy|MM|dd");
		Date curDate = new Date(System.currentTimeMillis());
		String str = formatter.format(curDate);

		return str;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void initIncome() {

		for (int i = 0; i < 5; i++) {
			kindList.add(String.valueOf(i));
		}
	}

	private void initUnitIncome() {
		for (int i = 0; i < 5; i++) {
			unitList.add(String.valueOf(i));
		}
	}


	private void setSumDefault() {
		String str = resources.getString(R.string.income_count_unit);
		str = String.format(str, "0.00");

	}

	private void launchKindDialog() {

	}

	private void launchUnitDialog() {

	}
	

	private void showKindView(View v) {
		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.spinner_layout, null);

		mySpinnerAdapter = new MySpinnerAdapter(this, kindList);

		ListView listView = (ListView) layout.findViewById(R.id.listView);

		listView.setAdapter(mySpinnerAdapter);

		popupWindow = new PopupWindow(v);

		popupWindow.setWidth(kindLayout.getWidth());

		LogHelp.logPutPut("width = "+kindLayout.getWidth());
		
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);

		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		popupWindow.setOutsideTouchable(true);

		popupWindow.setFocusable(true);

		popupWindow.setContentView(layout);

		popupWindow.showAsDropDown(v, -kindText.getWidth() - 2, 0);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				if (arg2 == kindList.size() - 1) {
					launchKindDialog();

					popupWindow.dismiss();
					popupWindow = null;

					mySpinnerAdapter = null;
				} else {
					kindText.setText(kindList.get(arg2));

					popupWindow.dismiss();
					popupWindow = null;
					mySpinnerAdapter = null;
				}
			}
		});
	}


	private void showCalendarView(View v)
    {
		
		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.calendar_dialog, null);

		calendarView = (CalendarView) layout.findViewById(R.id.calendar_dialog_calendar_view);
	
		Button prevButton = (Button) layout.findViewById(R.id.calendar_dialog_prev);
		Button nextButton = (Button) layout.findViewById(R.id.calendar_dialog_next);
		
	
		popupWindow = new PopupWindow(v);

		popupWindow.setWidth(linearLayout.getWidth());

		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		
		
		InputStream inputStream = getResources().openRawResource(R.drawable.popup_bkg);  
		
		popupWindow.setBackgroundDrawable(new BitmapDrawable(inputStream));

		popupWindow.setOutsideTouchable(true);

		popupWindow.setFocusable(true);

		popupWindow.setContentView(layout);

		popupWindow.showAsDropDown(v, -endDateText.getWidth(), -60);
		
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
		Intent intent = AddTask.this.getIntent();
		String action = intent.getAction();
		
		if(cell.mPaint.getColor() == Color.GRAY) {
			calendarView.previousMonth(); 
		} else if(cell.mPaint.getColor() == Color.LTGRAY) {
			calendarView.nextMonth(); 

		} else {  //  ±¾ÔÂµÄ
			
			String str = String.format("%s-%s-%s", calendarView.getYear(),calendarView.getMonth(),cell.getDayOfMonth());
			
	
			if(switcher)
			{
				startDateText.setText(str);
			}else
			{
				endDateText.setText(str);
			}
			
			popupWindow.dismiss();
			popupWindow = null;
			calendarView = null;
			
		}
	}
	
	
	
	
}