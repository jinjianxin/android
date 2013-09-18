package com.asianux.musicplayer;

import java.util.List;
import java.util.Map;

import com.asianux.adapter.AlbumApapter;
import com.asianux.utils.MediaUtils;
import com.asianux.utils.Mp3Info;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月15日 下午10:01:16
 * 类说明
 */

public class AlbumActivity  extends Activity{
	
	private GridView ablumView = null;
	
	List<String> albumList = null;
	Map<String , List<Mp3Info>> albumMap = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.album_layout);
		
		ablumView = (GridView) findViewById(R.id.gridView);
		
		MediaUtils mediaUtils = MediaUtils.getInstance(AlbumActivity.this);
		
		ablumView.setAdapter(new AlbumApapter(AlbumActivity.this));

		albumList = mediaUtils.getAlbumList();
		albumMap = mediaUtils.getALbumMap();

		
		System.out.println("albumList="+albumList.size());
		
		ablumView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				List<Mp3Info> tmpMap = albumMap.get(albumList.get(arg2));
				
				System.out.println("tmpMap = "+tmpMap.size());
				
			}
		});
	
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
