package com.asianux.utils;

import java.io.Serializable;

public class Mp3Info implements Serializable {

	private long id;
	private String title;
	private String album;
	private long albumId;
	private String artist;
	private long duration;
	private long size;
	private String url;
	private String lrcTitle;
	private String lrcSize;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Mp3Info [id=" + id +"\title"+title+"\talbum"+album
				+"\tartist"+artist+"\turl"+url+"]";
	}
	
	public long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(long albumId) {
		this.albumId = albumId;
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

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
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

	public String getLrcTitle() {
		return lrcTitle;
	}

	public void setLrcTitle(String lrcTitle) {
		this.lrcTitle = lrcTitle;
	}

	public String getLrcSize() {
		return lrcSize;
	}

	public void setLrcSize(String lrcSize) {
		this.lrcSize = lrcSize;
	}


	
	
}
