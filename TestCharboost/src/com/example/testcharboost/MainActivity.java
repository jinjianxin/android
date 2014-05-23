package com.example.testcharboost;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chartboost.sdk.*;

public class MainActivity extends Activity {
	
	private Chartboost cb;
	private Button button = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		button = (Button) findViewById(R.id.button);
		
		this.cb = Chartboost.sharedChartboost();
		String appId = "537a289a89b0bb643d474a29";
		String appSignature = "e595c99cbcea5a9b9da0589759bc5ee7713ad83f";
		this.cb.onCreate(this, appId, appSignature, null);
		

		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				cb.showInterstitial();
			}
		});  
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		this.cb.onStart(this);

	    // Notify the beginning of a user session. Must not be dependent on user actions or any prior network requests.
	    this.cb.startSession();

	    
	    this.cb.cacheInterstitial();
	    
	  
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		this.cb.onStop(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		this.cb.onDestroy(this);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		if (this.cb.onBackPressed())
	        return;
	    else
	        super.onBackPressed();
	}
}
