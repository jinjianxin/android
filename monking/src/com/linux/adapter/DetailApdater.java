package com.linux.adapter;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;

import com.linux.monking.R;
import com.linux.utils.DataItem;

import android.database.ContentObservable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.content.*;

public class DetailApdater  extends BaseAdapter{

	private Context context = null;
	private List<DataItem>  list = null;
	private List<View > viewList = null;
	
	public DetailApdater(Context context,List<DataItem> dataList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = dataList;
		
		viewList = new ArrayList<View>();
		
		if(list !=null && !list.isEmpty())
		{
			
			for (DataItem dataItem : dataList) {
				
				viewList.add(MakeView(dataItem));
			}
			
		}
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		if(list !=null && !list.isEmpty())
		{
			return list.size();
		}
		else
		{
			return 0;
		}
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return viewList.get(arg0);
		
	}

	private View MakeView(DataItem dataItem)
	{
		 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		 View view = inflater.inflate(R.layout.detail_adapter_layout, null);
		 
		 TextView dateText = (TextView) view.findViewById(R.id.detail_adapter_date);
		 
		 String date = formateDate(dataItem.getYear(),dataItem.getMonth(),dataItem.getDay());
		 dateText.setText(date);
		 
		 TextView typeText = (TextView) view.findViewById(R.id.detail_adapter_type);
		 
		 if(dataItem.getFlag().equals("0"))
		 {
			 typeText.setText(R.string.main_income);
			 
		 }else
		 {
			 typeText.setText(R.string.main_spending);
		 }
		 
		 TextView countText = (TextView) view.findViewById(R.id.detail_adapter_count);
		 
		 countText.setText(dataItem.getSum());
		 
		 return view;
	}
	
	private String formateDate(int year,int month,int day)
	{
		return String.format("%d-%d-%d", year,month,day);
	}
	
}
