package com.asianux.utils;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月8日 上午9:57:15
 * 类说明
 */
public class GecimiInfo {
	
	private String sid = null;
	private String song = null;
	private String artist = null;
	private String aid = null;
	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}
	private String lrc = null;
	
	public GecimiInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public String toString()
	{
		return sid+"\t"+song+"\t"+artist+"\t"+aid+"\t"+lrc;
	}
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getSong() {
		return song;
	}
	public void setSong(String song) {
		this.song = song;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getLrc() {
		return lrc;
	}
	public void setLrc(String lrc) {
		this.lrc = lrc;
	}

}
