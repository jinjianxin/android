package com.lemi.gamecircletest;

import com.lemi.gamecircle.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;

public class MainActivity extends Activity {
	private String TAG = "lemi";

	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// MpUtils.enablePaymentBroadcast(this,
		// Manifest.permission.PAYMENT_BROADCAST_PERMISSION);

		button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				AmazonLeaderBoard.getInstance().showLeaderBoard();
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onResume");
		AmazonLeaderBoard.getInstance().onResume(this);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		AmazonLeaderBoard.getInstance().onPause();
	}
}
