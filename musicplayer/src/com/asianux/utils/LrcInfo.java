package com.asianux.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月9日 下午8:54:43 类说明
 */

public class LrcInfo implements Serializable {

	private String title = null;
	private String singer = null;
	private String album = null;
	private String lrcUrl = null;
	private List<LrcItem > lrcList = null;
	private int position = 0;

	public String getLrcUrl() {
		return lrcUrl;
	}

	public void setLrcUrl(String lrcUrl) {
		this.lrcUrl = lrcUrl;
	}

	public String toString() {
		return title + "\n" + singer + "\n" + album + "\n" + lrcUrl;
	}

	public LrcInfo() {
		// TODO Auto-generated constructor stub
		
		lrcList = new ArrayList<LrcItem>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}
	
	public List<LrcItem> getLrcList() {
		return lrcList;
	}

	public void setLrcList(List<LrcItem> lrcList) {
		this.lrcList = lrcList;
	}


	public boolean isEmpty() {
		if (title != null && singer != null && (!lrcList.isEmpty()))
			return false;
		else
			return true;
	}

	public int find(int currentTime) {
		// TODO Auto-generated method stub
		
		LrcItem lrcItem = null;

		for (int i = 0; i < lrcList.size(); i++) 
		{
			lrcItem = lrcList.get(i);
			
			if(lrcItem.isTime(currentTime))
			{
				position = i;
				break;
			}
		}
		
		if(position !=0)
			position= position-1;
		
		return position;
	}

}
