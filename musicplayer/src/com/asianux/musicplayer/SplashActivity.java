package com.asianux.musicplayer;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

public class SplashActivity extends Activity {
	
	private Animation endAnimation;
	private Handler handler;
	private Runnable runnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//此函数必须放在setContentView之前否则会出错
		setContentView(R.layout.splashlayout);
		
		findViewById(R.id.splashlayout);

		
		endAnimation=AnimationUtils.loadAnimation(this, R.anim.splashanim);
		endAnimation.setFillAfter(true); 
		
		handler = new Handler();

		runnable = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub

				findViewById(R.id.splashlayout).startAnimation(endAnimation);
					
			}
		};
		
		endAnimation.setAnimationListener(new AnimationListener() {
			

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(SplashActivity.this, MainActivity.class);
				SplashActivity.this.startActivity(intent);
				SplashActivity.this.finish();
				
			}
		});
		
		
		handler.postDelayed(runnable, 2000);
	
	}

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

}
