package com.example.readfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.example.utils.FileUtils;

import android.R.array;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	
	//private HashMap<String, String> hasMap = new HashMap<String, String>();
	
	private String []keyArray = {"name","map","devSubtype","GAME_UP","GAME_DOWN",
			"GAME_LEFT","GAME_RIGHT","GAME_Y","GAME_X","GAME_A","GAME_B",
			"GAME_SELECT","GAME_START","GAME_L2","GAME_L1","GAME_R2","GAME_R1"};
	private String []mapArray = {"joy","0","0","3","4","5","6","7",
			"8","9","10","11","12","13","14","15","16"};
	
	
	ArrayList<String> keyList = new ArrayList<String>();
	ArrayList<String> mapList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
	/*	hasMap.put("name", "joy");
		hasMap.put("map", "1");
		hasMap.put("devSubtype", "0");
		hasMap.put("GAME_UP", "0");
		hasMap.put("GAME_RIGHT", "1");
		hasMap.put("GAME_DOWN", "2");
		hasMap.put("GAME_LEFT", "3");
		hasMap.put("GAME_SELECT", "4");
		hasMap.put("GAME_START", "5");
		hasMap.put("GAME_B", "6");
		hasMap.put("GAME_A", "7");
		hasMap.put("GAME_Y", "8");
		hasMap.put("GAME_X", "9");
		hasMap.put("GAME_L1", "10");
		hasMap.put("GAME_L2", "11");
		hasMap.put("GAME_R1", "12");
		hasMap.put("GAME_R2", "13");*/
		

		
/*		for (String string : keyArray) {
			keyList.add(string);
		}
		
		
		for (String string : keyList) {
			Log.d("shentan", string);
		}*/
		
		for (int i = 0; i < keyArray.length; i++) {
			keyList.add(keyArray[i]);
			mapList.add(mapArray[i]);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		FileUtils fileUtils = FileUtils.getInstance();
		fileUtils.saveConfig(keyList,mapList);
		
	}
	
}
