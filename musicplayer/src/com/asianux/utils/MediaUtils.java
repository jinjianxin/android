package com.asianux.utils;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.EncodingUtils;

import com.asianux.database.DbManager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.SignalStrength;

public class MediaUtils {
	
	
	private volatile static MediaUtils mediaUtils = null;
	private DbManager dbManager = null;
	private Context context = null;

	private static final Uri albumArtUri = Uri
			.parse("content://media/external/audio/albumart");
	
	private List<Mp3Info> collectList = new ArrayList<Mp3Info>();
	private List<String > albumList = new ArrayList<String>();
	private List<String > singerList = new ArrayList<String>();
	
	private List<String > keyList = new ArrayList<String>();
	private Map<String, List<String> > albumMap = new HashMap<String, List<String> >();
	private Map<String, List<String> > singerMap = new HashMap<String, List<String>>();
	private Map<String , Mp3Info> mp3infoMap = new HashMap<String, Mp3Info>();

	public static MediaUtils getInstance(Context context) {
			   
			   synchronized (MediaUtils.class) {
				if(mediaUtils == null)
				{
					mediaUtils = new MediaUtils(context);
				}
			}
		
		return mediaUtils;
	}
	
	
	public MediaUtils(Context context) {
		super();
		this.context = context;
		dbManager =DbManager.getInstance(context);
		
		collectList.clear();
		albumList.clear();
		singerList.clear();
		keyList.clear();
		albumMap.clear();
		singerMap.clear();
		mp3infoMap.clear();
		
		initData();
	}
	
	public void  initData() {
		
		List<Mp3Info> list = dbManager.getAllMusic();

		if (list != null) {

			System.out.println("**************************************1");
			
			int count = list.size();

			for (int i = 0; i < count; i++) {

				Mp3Info mp3Info = list.get(i);

				if (albumMap.containsKey(mp3Info.getAlbum())) {
					List<String> atmpList = albumMap.get(mp3Info.getAlbum());
					atmpList.add(mp3Info.getUrl());
					albumMap.put(mp3Info.getAlbum(), atmpList);
				} else if (mp3Info.getAlbum().equals("NULL")) {
					//System.out.println("----------------null");

					if (albumMap.containsKey("NULL")) {
						List<String> atmpList = albumMap.get("NULL");
						atmpList.add(mp3Info.getUrl());
						albumMap.put("NULL", atmpList);
					} else {
						List<String> atmpList = new ArrayList<String>();
						atmpList.add(mp3Info.getUrl());
						albumMap.put("NULL", atmpList);
						albumList.add("NULL");
					}
				} else {
					List<String> atmpList = new ArrayList<String>();
					atmpList.add(mp3Info.getUrl());
					albumMap.put(mp3Info.getAlbum(), atmpList);
					albumList.add(mp3Info.getAlbum());
				}

				if (singerMap.containsKey(mp3Info.getArtist())) {
					List<String> stmpList = singerMap.get(mp3Info.getArtist());
					stmpList.add(mp3Info.getUrl());
					singerMap.put(mp3Info.getArtist(), stmpList);
				} else if (mp3Info.getArtist().equals("NULL")) {
					if (singerMap.containsKey("NULL")) {
						List<String> atmpList = singerMap.get("NULL");
						atmpList.add(mp3Info.getUrl());
						singerMap.put("NULL", atmpList);
					} else {
						List<String> atmpList = new ArrayList<String>();
						atmpList.add(mp3Info.getArtist());
						singerMap.put("NULL", atmpList);
						singerList.add("NULL");
					}
				} else {
					List<String> stmpList = new ArrayList<String>();
					stmpList.add(mp3Info.getUrl());
					singerMap.put(mp3Info.getArtist(), stmpList);
					singerList.add(mp3Info.getArtist());
				}

				mp3infoMap.put(mp3Info.getUrl(), mp3Info);
				keyList.add(mp3Info.getUrl());

			}
		}
		
	}
		
	public Map<String , List<String> > getALbumMap()
	{
		return albumMap;
	}
	
	public Map<String , List<String>> getSingerMap()
	{
		return singerMap;
	}
	
	public List<String > getAlbumList()
	{
		return albumList;
	}
	
	public List<String > getAlbumList(String album)
	{
		if(albumMap.isEmpty())
		{
			return null;
		}
		else
		{
			return albumMap.get(album);
		}
	}
	
	public void removeAlbumList(Mp3Info mp3Info)
	{
		if(!albumMap.isEmpty())
		{
			//singerMap.remove(singer);
			
			List<String> list = albumMap.get(mp3Info.getAlbum());
			if(list !=null && !list.isEmpty())
			{
				int index = list.indexOf(mp3Info.getUrl());
				if(index !=-1)
				{
					list.remove(index);
				}
			}
			
		}
	}
	
	
	public List<String > getSingerList()
	{
		return singerList;
	}
	
	public List<String > getSingerList(String singer)
	{
		if(singerMap.isEmpty())
		{
			return null;
		}
		else
		{
			return singerMap.get(singer);
		}
	}
	
	public void removeSingerList(Mp3Info mp3Info)
	{
		if(!singerMap.isEmpty())
		{
			//singerMap.remove(singer);
			
			List<String> list = singerMap.get(mp3Info.getArtist());
			if(list !=null && !list.isEmpty())
			{
				int index = list.indexOf(mp3Info.getUrl());
				if(index !=-1)
				{
					list.remove(index);
				}
			}
			
		}
	}
	
	/**
	 * 
	 * getMp3infoList获得系统全部音乐
	 * (这里描述这个方法适用条件 – 可选)
	 * @return 
	 *List<String>
	 * @exception 
	 * @since  1.0.0
	 */
	public List<String> getMp3infoList()
	{
		return keyList;
	}
	
	public void removeMp3infoItem(String key)
	{
		if(keyList !=null && !keyList.isEmpty())
		{	
			keyList.remove(key);
		}
	}
	
	
	public Mp3Info getMp3infoWithKey(String key)
	{		
		if(mp3infoMap.isEmpty())
		{
			return null;
		}
		else
		{
			return mp3infoMap.get(key);
		}
		
	}
	
	public void addCollectMp3info(Mp3Info mp3Info)
	{
		collectList.add(mp3Info);
	}
	
	public void removeCollectMp3info(Mp3Info mp3Info)
	{
		int size = collectList.size();
		
		for (int i = 0; i < size; i++) {
			if(collectList.get(i).equals(mp3Info.getUrl()))
			{
				collectList.remove(i);
				break;
			}
		}
	}
	
	
	/**
	 * addCollect 收藏歌曲
	 * @param mp3Info 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public void addCollect(Mp3Info mp3Info)
	{
		
	}
	
	/**
	 * 
	 * removeCollect 删除已收藏歌曲
	 * (这里描述这个方法适用条件 – 可选)
	 * @param mp3Info 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	
	public void removeCollect(Mp3Info mp3Info)
	{
	
	}
	
	public static Bitmap getAlbumInverted(Bitmap originalImage)
	{
		    final int reflectionGap = 4;
		    int width = originalImage.getWidth(); 
		    int height = originalImage.getHeight();
		    Matrix matrix = new Matrix(); 
		    matrix.preScale(1, -1);
		    Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, 
		            height / 2, width, height / 2, matrix, false);
		    Bitmap bitmapWithReflection = Bitmap.createBitmap(width, 
		            (height + height / 2), Config.ARGB_8888);
		    Canvas canvas = new Canvas(bitmapWithReflection);
		    canvas.drawBitmap(originalImage, 0, 0, null);
		    Paint defaultPaint = new Paint(); 
		    canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
		    canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		    Paint paint = new Paint(); 
		    LinearGradient shader = new LinearGradient(0, 
		            originalImage.getHeight(), 0, bitmapWithReflection.getHeight() 
		                    + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.MIRROR);
		    
		    //LinearGradient shader = new LinearGradient(x0, y0, x1, y1, color0, color1, tile)
		    paint.setShader(shader);
		    paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		    canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() 
		            + reflectionGap, paint);
		    return bitmapWithReflection; 
	}

	public void clear() {
		// TODO Auto-generated method stub
		keyList.clear();
		albumMap.clear();
		singerMap.clear();
		mp3infoMap.clear();
	} 
}
