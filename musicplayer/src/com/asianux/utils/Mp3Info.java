package com.asianux.utils;

import java.io.Serializable;

public class Mp3Info implements Serializable {

	private long id;
	private String title = null;
	private String artist = null;
	private long size;
	private String url = null;
	private String lrc = null;
	private String album = null;
	private long albumId;


	public String getLrc() {
		
		if(lrc == null)
			return null;
		else
			return lrc;
	}

	public void setLrc(String lrc) {
		this.lrc = lrc;
	}

	public long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(long albumId) {
		this.albumId = albumId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Mp3Info [id=" + id +"\title"+title+"\turl"+url+"]";
	}

	public Mp3Info() {
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}


}

