package com.asianux.utils;

import java.io.Serializable;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月12日 下午9:22:06
 * 类说明
 */

public class LrcItem implements Serializable{
	

	private String lrc = null;
	private long startTime = 0;
	private long endTime = 0;
	
	public LrcItem(long startTime,long endTime ,String lrc) {
		// TODO Auto-generated constructor stub
		
	//	System.out.println("startTime = "+startTime+"\t"+"endTime = "+endTime);
		
		this.startTime=startTime;
		this.endTime=endTime;
		this.lrc = lrc;
	}
	
	public String toString()
	{
		return startTime+"\t"+endTime+"\t"+lrc;
	}

	public String getLrc() {
		return lrc;
	}

	public void setLrc(String lrc) {
		this.lrc = lrc;
	}
	
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public boolean isTime(int time) {
		if (time >= startTime && time <= endTime)
			return true;
		else
			return false;
	}
}
