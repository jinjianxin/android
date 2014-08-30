package com.linux.monking;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.linux.adapter.MySpinnerAdapter;
import com.linux.database.DbManager;
import com.linux.dialog.AddDialog;
import com.linux.dialog.AddDialog.AddDialogListener;
import com.linux.utils.DataItem;
import com.linux.view.CalendarView;
import com.linux.view.Cell;

public class IncomeActivity extends Activity implements CalendarView.OnCellTouchListener{

	private TextView titleView = null;

	private PopupWindow popupWindow = null;
	private LinearLayout kindLayout = null;
	private TextView kindText = null;
	private Button kindButton = null;

	private EditText numberEditText = null;
	private LinearLayout unitLayout = null;
	private TextView unitText = null;
	private Button unitButton = null;

	private EditText dateEditText = null;

	private EditText unitEditText = null;
	private TextView counTextView = null;
	private Button sureButton = null;
	private DataItem dataItem = null;

	private Resources resources = null;
	private String flag = null;
	private List<String> kindList = null;
	private List<String> unitList = null;

	private DbManager dbManager = null;

	private CalendarView calendarView = null;
	private LinearLayout linearLayout = null;
	
	
	private ArrayAdapter<String> adapter = null;

	private MySpinnerAdapter mySpinnerAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		dbManager = DbManager.getInstance(IncomeActivity.this);

		kindList = new ArrayList<String>();
		unitList = new ArrayList<String>();

		Intent intent = getIntent();

		flag = intent.getStringExtra("flag");

		setContentView(R.layout.income_layout);

		titleView = (TextView) findViewById(R.id.income_title);

		kindLayout = (LinearLayout) findViewById(R.id.income_kind_spinner_layout);
		kindText = (TextView) findViewById(R.id.income_kind_spinner_text_view);
		kindButton = (Button) findViewById(R.id.income_kind_spinner_select_button);

		numberEditText = (EditText) findViewById(R.id.income_number_editText);

		unitLayout = (LinearLayout) findViewById(R.id.income_unit_spinner_layout);
		unitText = (TextView) findViewById(R.id.income_unit_spinner_text_view);
		unitButton = (Button) findViewById(R.id.income_unit_spinner_select_button);

		dateEditText = (EditText) findViewById(R.id.income_date_editText);
		
		unitEditText = (EditText) findViewById(R.id.income_unitprice_editText);
		counTextView = (TextView) findViewById(R.id.income_count_edittext);
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
			dataItem = (DataItem) intent.getSerializableExtra("dataItem");
		}

		initKindSpinner(dataItem);

		initUnitSpinner(dataItem);


		kindButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showKindView(v);
			}
		});

		unitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				showUnitView(v);
			}
		});

		if (flag.equals("details")) {
			setSumDefault(dataItem);
		}
		
		numberEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				if (unitEditText.getText().toString().length() > 0
						&& numberEditText.getText().toString().length() > 0) {
					setSum();
				} else {
					setSumDefault();
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		unitEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				if (unitEditText.getText().toString().length() > 0
						&& numberEditText.getText().toString().length() > 0) {
					setSum();
				} else {
					setSumDefault();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!flag.equals("details")) {
					if (checkEmpty()) {

						float numer = Float.parseFloat(numberEditText.getText()
								.toString());
						float unit = Float.parseFloat(unitEditText.getText()
								.toString());

						float sum = numer * unit;

						DataItem dataItem = new DataItem();

						String[] date = getDate().split("\\|");

						for (int i = 0; i < date.length; i++) {
							System.out.println(date[i]);
						}

						dataItem.setKind(kindText.getText().toString());

						dataItem.setNumber(numberEditText.getText().toString());
						dataItem.setUnit(unitText.getText().toString());
						dataItem.setPrice(unitEditText.getText().toString());
						dataItem.setSum(String.valueOf(sum));

						dataItem.setYear(Integer.parseInt(date[0]));
						dataItem.setMonth(Integer.parseInt(date[1]));
						dataItem.setDay(Integer.parseInt(date[2]));
					
						Date currentDate = new Date(dataItem.getYear(),dataItem.getMonth(),dataItem.getDay());
						dataItem.setTime(currentDate.getTime());
						
						if (flag.equals("income")) {
							dataItem.setFlag("0");
						} else if (flag.equals("spending")) {
							dataItem.setFlag("1");
						}

						dbManager.insertDataItem(dataItem);

						finish();
					} else {
						Toast.makeText(IncomeActivity.this,
								R.string.input_error, Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					finish();
				}
			}
		});
		
		
		dateEditText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//showCalendarView(v);
			}
		});

	}

	private boolean checkEmpty() {

		if ((kindText.getText().toString().length() > 0)
				&& (numberEditText.getText().toString().length() > 0)
				&& unitEditText.getText().toString().length() > 0
				&& dateEditText.getText().length() > 0
				&& counTextView.getText().length() > 0
				&& unitEditText.getText().length() > 0) {
			return true;
		} else {
			return false;
		}

	}

	private String covertString() {
		return kindText.getText().toString() + "\t"
				+ numberEditText.getText().toString() + "\t"
				+ unitEditText.getText().toString() + "\t"
				+ dateEditText.getText().toString() + "\t"
				+ counTextView.getText().toString();

	}

	private void setDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());
		String str = formatter.format(curDate);

		dateEditText.setText(str);
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

	private void initKindSpinner(DataItem dataItem) {
		if (flag.equals("income")) {
			initIncome();
		} else if (flag.equals("spending")) {
			initSpending();
		} else if (flag.equals("details")) {
			if (dataItem.getFlag().equals("0")) {
				initIncome();
				titleView.setText(R.string.income_title);
			} else {
				initSpending();
				titleView.setText(R.string.spending_title);
			}
		}
	}

	private void initIncome() {
		kindList.clear();
		List<String> list = dbManager.getAllKind(0);
		kindList = list;

		if (kindList != null && !kindList.isEmpty()) {
			kindText.setText(kindList.get(0));
		} else {
			kindText.setText("");
		}

		kindList.add("新增");
	}

	private void initSpending() {
		kindList.clear();
		List<String> list = dbManager.getAllKind(1);
		kindList = list;

		if (kindList != null && !kindList.isEmpty()) {
			kindText.setText(kindList.get(0));
		} else {
			kindText.setText("");
		}

		kindList.add("新增");

	}

	private void initUnitSpinner(DataItem dataItem) {

		if (flag.equals("income")) {
			initUnitIncome();
		} else if (flag.equals("spending")) {
			initUnitSpending();
		} else if (flag.equals("details")) {
			if (dataItem.getFlag().equals("0")) {
				initUnitIncome();
			} else {
				initUnitSpending();
			}
		}
	}

	private void initUnitIncome() {

		unitList.clear();
		List<String> list = dbManager.getAllUnit(0);
		unitList = list;

		if (unitList != null && !unitList.isEmpty()) {
			unitText.setText(unitList.get(0));
		} else {
			unitText.setText("");
		}

		unitList.add("新增");
	}

	private void initUnitSpending() {
		unitList.clear();
		List<String> list = dbManager.getAllUnit(1);
		unitList = list;

		if (unitList != null && !unitList.isEmpty()) {
			unitText.setText(unitList.get(0));
		} else {
			unitText.setText("");
		}

		unitList.add("新增");
	}

	private void setSumDefault() {
		String str = resources.getString(R.string.income_count_unit);
		str = String.format(str, "0.00");
		counTextView.setText(str);

	}

	private void setSumDefault(DataItem dataItem) {
		
		kindText.setText(dataItem.getKind());
		kindButton.setClickable(false);

		numberEditText.setEnabled(false);
		numberEditText.setText(dataItem.getNumber());

		unitText.setText(dataItem.getUnit());
		unitButton.setClickable(false);

		unitEditText.setEnabled(false);
		unitEditText.setText(dataItem.getPrice());

		String str = resources.getString(R.string.income_count_unit);
		str = String.format(str, Float.parseFloat(dataItem.getSum()));
		counTextView.setText(str);

		dateEditText.setEnabled(false);
		dateEditText.setText(String.format("%d-%d-%d", dataItem.getYear(),
				dataItem.getMonth(), dataItem.getDay()));

	}

	private void setSum() {
		float number = Float.parseFloat(numberEditText.getText().toString());
		float unit = Float.parseFloat(unitEditText.getText().toString());

		float sum = number * unit;
		DecimalFormat fnum = new DecimalFormat("##0.00");
		String dd = fnum.format(sum);
		System.out.println(dd);

		String str = resources.getString(R.string.income_count_unit);
		str = String.format(str, sum);
		counTextView.setText(str);
	}

	private void launchKindDialog() {
		AddDialog addDialog = new AddDialog(this, new AddDialogListener() {

			@Override
			public void sure(String kind) {
				// TODO Auto-generated method stub

				kindList.add(kindList.size() - 1, kind);

				if (flag.equals("income")) {
					dbManager.addNewKind(kind, 0);
				} else if (flag.equals("spending")) {
					dbManager.addNewKind(kind, 1);
				}

				kindText.setText(kind);

			}
		});

		addDialog.setTitle(R.string.add_dialog_title);

		addDialog.show();
	}

	private void launchUnitDialog() {
		System.out.println("********************************************\n");

		AddDialog addDialog = new AddDialog(this, new AddDialogListener() {

			@Override
			public void sure(String kind) {
				// TODO Auto-generated method stub

				unitList.add(unitList.size() - 1, kind);

				if (flag.equals("income")) {

					dbManager.addNewUnit(kind, 0);
				} else if (flag.equals("spending")) {
					dbManager.addNewUnit(kind, 1);
				}

				unitText.setText(kind);

			}

		});

		addDialog.setTitle(R.string.add_unit_title);

		addDialog.show();
	}

	private void showKindView(View v) {
		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.spinner_layout, null);

		mySpinnerAdapter = new MySpinnerAdapter(this, kindList);

		ListView listView = (ListView) layout.findViewById(R.id.listView);

		listView.setAdapter(mySpinnerAdapter);

		popupWindow = new PopupWindow(v);

		popupWindow.setWidth(kindLayout.getWidth());

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

	private void showUnitView(View v) {
		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.spinner_layout, null);

		mySpinnerAdapter = new MySpinnerAdapter(this, unitList);

		ListView listView = (ListView) layout.findViewById(R.id.listView);

		listView.setAdapter(mySpinnerAdapter);

		popupWindow = new PopupWindow(v);

		popupWindow.setWidth(unitLayout.getWidth());

		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);

		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		popupWindow.setOutsideTouchable(true);

		popupWindow.setFocusable(true);

		popupWindow.setContentView(layout);

		popupWindow.showAsDropDown(v, -unitText.getWidth() - 2, 0);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				if (arg2 == unitList.size() - 1) {
					launchUnitDialog();

					popupWindow.dismiss();
					popupWindow = null;

					mySpinnerAdapter = null;
				} else {
					unitText.setText(unitList.get(arg2));

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

		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		popupWindow.setOutsideTouchable(true);

		popupWindow.setFocusable(true);

		popupWindow.setContentView(layout);

		popupWindow.showAsDropDown(v, -dateEditText.getWidth(), 0);
		
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
		Intent intent = IncomeActivity.this.getIntent();
		String action = intent.getAction();
		
		if(cell.mPaint.getColor() == Color.GRAY) {
			calendarView.previousMonth(); 
		} else if(cell.mPaint.getColor() == Color.LTGRAY) {
			calendarView.nextMonth(); 

		} else {  //  本月的
			
			String str = String.format("%s-%s-%s", calendarView.getYear(),calendarView.getMonth(),cell.getDayOfMonth());
			
	
			
			popupWindow.dismiss();
			popupWindow = null;
			calendarView = null;
			
		}
	}
	
}
