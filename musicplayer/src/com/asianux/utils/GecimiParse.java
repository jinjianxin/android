package com.asianux.utils;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月8日 上午9:58:08
 * 类说明
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.graphics.Region.Op;

public class GecimiParse {
	
	public GecimiParse()
	{
		super();
	}
	
	
	public static GecimiInfo getGecimiInfo(String data) throws JSONException
	{		
		JSONTokener jsonTokener = new JSONTokener(data);
		JSONObject person = (JSONObject) jsonTokener.nextValue();
		
		 int count = person.optInt("count");
		 // 响应码
		 int code = person.optInt("code");
		 // 歌词结果集
		 JSONArray jsonArray = person.optJSONArray("result");
		 
		 System.out.println("count=\t"+count+"\t"+"code=\t"+code);
		 
		 if(jsonArray == null || jsonArray.length()==0)
			 return null ;
		 
		 int length = jsonArray.length();
		 for (int i = 0; i < 1; i++)
		 {
			 JSONObject obj = jsonArray.getJSONObject(i);
			 
			GecimiInfo info =new GecimiInfo();
			
			info.setSid(obj.optString("sid"));
			info.setSong(obj.optString("song"));
			info.setArtist(obj.optString("artist"));
			info.setAid(obj.optString("aid"));
			info.setLrc(obj.optString("lrc"));
	
			return info;
			 
		 }
		
		return null;
		
	}
	

}
