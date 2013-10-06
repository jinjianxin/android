package com.example.testviewgroup;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	
	private MyViewGroup myViewGroup = null;
	private boolean isOpen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		myViewGroup = (MyViewGroup) findViewById(R.id.mySlideMenu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub

		return super.onMenuOpened(featureId, menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		
/*		if(!isOpen)
		{
			isOpen = true;
			myViewGroup.openSlide();
		}
		else
		{
			isOpen = false;
			myViewGroup.closeSlide();
		}
		*/
		
		if(!isOpen)
		{
			isOpen=true;
			myViewGroup.openSlideBottom();
		}
		else
		{
			isOpen=false;
			myViewGroup.closeSlideBottom();
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
}
