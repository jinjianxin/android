package com.asianux.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月9日 下午8:55:01 类说明
 */

public class LrcParser {

	LrcInfo info = null;
	//Map<Long, String> maps = null;
	private List<LrcItem> lrcList = null;
	private String currentContent = null;
	private long currentTime = 0;// 存放临时时间
	private long lastTime = 0; //存放上次时间 ，计算时间差

	public LrcParser() {
		// TODO Auto-generated constructor stub

		info = new LrcInfo();
		lrcList =new ArrayList<LrcItem>();
	}

	public LrcInfo parseLrcInfo(String lrcPath, Charset charset)
			throws IOException {
		System.out.println(lrcPath);

		File file = new File(lrcPath);
		if (file.exists()) {
			InputStream ins = new FileInputStream(file);
			parset(ins, charset);
		} else {
			return info;
		}
		return info;
	}

	private void parset(InputStream inputStream, Charset charset)
			throws IOException {

		InputStreamReader inr = new InputStreamReader(inputStream, charset);
		BufferedReader reader = new BufferedReader(inr);

		String line = null;
		while ((line = reader.readLine()) != null) {
			// System.out.println(line);
			parserLine(line);
		}
		info.setLrcList(lrcList);
	}

	private void parserLine(String line) {
		// TODO Auto-generated method stub
		
		//System.out.println(line);
		
		lastTime = currentTime;
		
		if (line.startsWith("[ti:")) {
			String title = line.substring(4, line.length() - 1);

			info.setTitle(title);
		} else if (line.startsWith("[ar:")) {
			String singer = line.substring(4, line.length() - 1);
			info.setSinger(singer);
		} else if (line.startsWith("[al:")) {
			String album = line.substring(4, line.length() - 1);
			info.setAlbum(album);
		} else if (line.startsWith("[url:")) {
			String url = line.substring(5, line.length() - 1);
			info.setLrcUrl(url);
		} else {

	    	String reg = "\\[(\\d{2}:\\d{2}\\.\\d{2})\\]";
			// 编译
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(line);

			while (matcher.find()) {
	
				String msg = matcher.group();

				int start = matcher.start();
				int end = matcher.end();

				int groupCount = matcher.groupCount();
				
				for (int i = 0; i <= groupCount; i++) {
					String timeStr = matcher.group(i);
					if (i == 1) {
						currentTime = getTimeLong(timeStr);
					}
				}
				
				String[] content = pattern.split(line);
				
				for (int i = 0; i < content.length; i++) {
					if (i == content.length - 1) {
			
						currentContent = content[i];
						break;
					}
				}
		
			}
			
		
			if(currentContent !=null)
			{
				LrcItem item = new LrcItem(lastTime,currentTime, currentContent);
				lrcList.add(item);
			}
			
			currentContent = null;
		}

	}

	private long getTimeLong(String time) {
		
		String[] s = time.split(":");
		int min = Integer.parseInt(s[0]);
		String[] ss = s[1].split("\\.");
		int sec = Integer.parseInt(ss[0]);
		int mill = Integer.parseInt(ss[1]);

		return min * 60 * 1000 + sec * 1000 + mill * 10;
	}

}