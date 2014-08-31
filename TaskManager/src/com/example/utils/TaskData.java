package com.example.utils;

import java.io.Serializable;

public class TaskData implements Serializable {
	
	private String mTitle;
	private int mPriority;
	private String mDate;
	private String mStartTime ;
	private String mEndTime;
	
	public TaskData()
	{
		
	}

	public TaskData(String title,int priority,String date,String stratTime,String endTime)
	{
		this.mTitle = title;
		this.mPriority = priority;
		this.mDate = date;
		this.mStartTime = stratTime;
		this.mEndTime = endTime;
	}


	/**
	 * @return mTitle
	 */
	
	public String getmTitle() {
		return mTitle;
	}


	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}


	/**
	 * @return mPriority
	 */
	
	public int getmPriority() {
		return mPriority;
	}


	public void setmPriority(int mPriority) {
		this.mPriority = mPriority;
	}


	/**
	 * @return mDate
	 */
	
	public String getmDate() {
		return mDate;
	}


	public void setmDate(String mDate) {
		this.mDate = mDate;
	}


	/**
	 * @return mStartTime
	 */
	
	public String getmStartTime() {
		return mStartTime;
	}


	public void setmStartTime(String mStartTime) {
		this.mStartTime = mStartTime;
	}


	/**
	 * @return mEndTime
	 */
	
	public String getmEndTime() {
		return mEndTime;
	}


	public void setmEndTime(String mEndTime) {
		this.mEndTime = mEndTime;
	}

	
	

}
