package com.asianux.musicplayer;

import com.asianux.utils.Mp3Info;

import android.app.Activity;
import android.os.Bundle;

public class PlayActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlayout);
		
		Mp3Info mp3info = (Mp3Info) this.getIntent().getSerializableExtra("mp3info");
		
		System.out.println(mp3info.toString());
	}

}
