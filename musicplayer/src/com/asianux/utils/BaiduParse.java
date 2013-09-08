package com.asianux.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月8日 下午4:47:08
 * 类说明
 */

public class BaiduParse {
	
	public BaiduParse()
	{
		super();
	}
	
	public static List<BaiduInfo> getBaidunfo(String xmlData) throws XmlPullParserException, IOException
	{
		
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser parser = factory.newPullParser();
		
		InputStream is = new ByteArrayInputStream(xmlData.getBytes());
		parser.setInput(is, "UTF-8");
	
		//ResultSet<BaiduInfo> resultSet = new ResultSet<BaiduInfo>();

		List<BaiduInfo> songInfos = new ArrayList<BaiduInfo>();
		BaiduInfo songInfo = null;

		int eventType = parser.getEventType();
		String tag = null;
		
		int i = 0;
		
		while (eventType != XmlPullParser.END_DOCUMENT)
		{
			tag = parser.getName();
			switch (eventType)
			{
				case XmlPullParser.START_TAG:
				{
					if ("result".equalsIgnoreCase(tag))
					{
						//songInfo = new BaiduInfo();
					}
					else if ("count".equalsIgnoreCase(tag))
					{
						//resultSet.setTotal(Integer.valueOf(parser.nextText()));
					}
					else if("url".equalsIgnoreCase(tag))
					{
						songInfo = new BaiduInfo();
					}
					else if ("encode".equalsIgnoreCase(tag))
					{
						String text = parser.nextText();
					/*	System.out.println(i+"\t text = "+text+"\n");
						i++;*/
						
						songInfo.setEncode(text);
					}
					else if ("decode".equalsIgnoreCase(tag))
					{
						songInfo.setDecode(parser.nextText());
					}
					else if ("type".equalsIgnoreCase(tag))
					{
						String text = parser.nextText();
						//songInfo.setType(text);
						songInfo.setType(text);
				
					}
					else if ("lrcid".equalsIgnoreCase(tag))
					{
						String text = parser.nextText();
						songInfo.setLrcid(text);		
					}
					else if ("flag".equalsIgnoreCase(tag))
					{
						String text = parser.nextText();
						songInfo.setFlag(text);
					}
					break;
				}
				case XmlPullParser.END_TAG:
				{
					
				//	System.out.println("tag = "+tag+"\n");
					if("url".equalsIgnoreCase(tag))
					{
						songInfos.add(songInfo);
					}
					else if ("durl".equalsIgnoreCase(tag) && songInfo != null)
					{
					//	songInfos.add(songInfo);
						//resultSet.setData(songInfos);
					}
					else if ("result".equalsIgnoreCase(tag) && songInfo != null)
					{
						
					}
					
					break;
				}
			}
			
			eventType = parser.next();
		}	
		
		System.out.println("------size = "+songInfos.size());
		
		return songInfos;
	}
}
