
/**  
* @Title: TaskAdapter.java
* @Package com.example.adapter
* @Description: TODO(用一句话描述该文件做什么)
* @author jjx
* @date 2014年8月29日 下午10:10:03
* @version V1.0 
*/

 
package com.example.adapter;

import java.util.List;

import com.example.taskmanager.R;
import com.example.utils.LogHelp;
import com.example.utils.TaskData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * @author jjx
 *
 */

public class TaskAdapter extends BaseAdapter {

	private Context context;
	private List<TaskData> dataList = null;
	
	public TaskAdapter(Context context,List<TaskData> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.dataList = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	
	/**
	* @param arg0
	* @return
	* @see android.widget.Adapter#getItem(int)
	*/
	
	
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	* @param arg0
	* @return
	* @see android.widget.Adapter#getItemId(int)
	*/
	
	
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}


	
	
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		
		

		TaskData taskData = dataList.get(arg0);
		
		if(arg1 ==null)
		{
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			View view = inflater.inflate(R.layout.item_layout, null);
			
			
			
			TextView priorityView = (TextView) view.findViewById(R.id.task_priority_content);
			TextView desView =  (TextView) view.findViewById(R.id.task_des_content);
			TextView dateView = (TextView) view.findViewById(R.id.task_date_content);
			
			if(taskData!=null)
			{
				priorityView.setText(String.valueOf(taskData.getmPriority()));
				desView.setText(taskData.getmTitle());
				dateView.setText(taskData.getmStartTime());
			}
			
			return view;
			
		}
		else
		{
			
			TextView priorityView = (TextView) arg1.findViewById(R.id.task_priority_content);
			TextView desView =  (TextView) arg1.findViewById(R.id.task_des_content);
			TextView dateView = (TextView) arg1.findViewById(R.id.task_date_content);
			
			if(taskData!=null)
			{
				priorityView.setText(String.valueOf(taskData.getmPriority()));
				desView.setText(taskData.getmTitle());
				dateView.setText(taskData.getmStartTime());
			}
		}
		
		return arg1;
		
	}

}
