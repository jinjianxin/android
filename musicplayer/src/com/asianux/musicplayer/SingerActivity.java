package com.asianux.musicplayer;

import java.util.List;
import java.util.Map;

import com.asianux.adapter.AlbumApapter;
import com.asianux.adapter.SingerAdapter;
import com.asianux.utils.MediaUtils;
import com.asianux.utils.Mp3Info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月17日 下午9:59:48
 * 类说明
 */

public class SingerActivity extends Activity{
	
	private final static String SINGERMUSIC = "singer";
	
	private GridView ablumView = null;
	
	List<String> singerList = null;
	//Map<String , List<Mp3Info>> singerMap = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.singer_laypout);
		
		ablumView = (GridView) findViewById(R.id.singerGridView);
		
		MediaUtils mediaUtils = MediaUtils.getInstance(SingerActivity.this);
		
		ablumView.setAdapter(new SingerAdapter(SingerActivity.this));

		singerList = mediaUtils.getSingerList();
		//singerMap = mediaUtils.getALbumMap();
		
		ablumView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent();
				intent.setClass(SingerActivity.this, ListActivity.class);
				intent.putExtra("tag",SINGERMUSIC);
				intent.putExtra("arg", singerList.get(arg2));
				SingerActivity.this.startActivity(intent);
								
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
}
