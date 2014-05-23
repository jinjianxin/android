/**
 * 
 */
package com.lemi.gamecircletest;

import java.util.EnumSet;
import java.util.List;
import java.util.ListIterator;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.amazon.ags.api.AGResponseCallback;
import com.amazon.ags.api.AGResponseHandle;
import com.amazon.ags.api.AmazonGamesCallback;
import com.amazon.ags.api.AmazonGamesClient;
import com.amazon.ags.api.AmazonGamesFeature;
import com.amazon.ags.api.AmazonGamesStatus;
import com.amazon.ags.api.leaderboards.GetLeaderboardsResponse;
import com.amazon.ags.api.leaderboards.GetPlayerScoreResponse;
import com.amazon.ags.api.leaderboards.Leaderboard;
import com.amazon.ags.api.leaderboards.LeaderboardsClient;
import com.amazon.ags.api.leaderboards.SubmitScoreResponse;
import com.amazon.ags.constants.LeaderboardFilter;

/**
 * @author zhangqiang
 * 
 */
public class AmazonLeaderBoard {
	private static final String TAG = "lemi";
	private static final String LEADERBOARD_ID = "lilgirlrunningtopscores";
	private boolean isShowLeaderBoard;
	private Activity mActivity;
	// reference to the agsClient
	AmazonGamesClient agsClient;
	AmazonGamesCallback callback = new AmazonGamesCallback() {

		@Override
		public void onServiceReady(AmazonGamesClient amazonGamesClient) {
			Log.d(TAG, "ready to use GameCircle");
			isShowLeaderBoard = true;
			agsClient = amazonGamesClient;
		}

		@Override
		public void onServiceNotReady(AmazonGamesStatus status) {
			Log.d(TAG, "unable to use service status = " + status.toString());
		}
	};

	// list of features your game uses (in this application,leaderboards)
	EnumSet<AmazonGamesFeature> myGameFeatures = EnumSet
			.of(AmazonGamesFeature.Leaderboards);

	private static AmazonLeaderBoard leaderboard;

	public static AmazonLeaderBoard getInstance() {
		if (leaderboard == null) {
			leaderboard = new AmazonLeaderBoard();
		}
		return leaderboard;
	}

	private AmazonLeaderBoard() {
		// TODO Auto-generated constructor stub
	}

	public void onResume(Activity pActivity) {
		Log.d(TAG, "Amzon init");
		mActivity = pActivity;
		AmazonGamesClient.initialize(pActivity, callback, myGameFeatures);
	}

	public void onPause() {

		if (agsClient != null) {
			AmazonGamesClient.release();
		}
	}

	private LeaderboardsClient getLeaderBoardClient() {
		LeaderboardsClient lbClient = null;
		if (agsClient != null) {
			lbClient = agsClient.getLeaderboardsClient();
		}
		return lbClient;

	}

	public void showLeaderBoard() {

		Log.d(TAG, "show leaderboard Client = " + getLeaderBoardClient());

		if (getLeaderBoardClient() != null) {
			getLeaderBoardClient().showLeaderboardsOverlay();
		} else {
			Toast.makeText(AmazonLeaderBoard.this.mActivity,
					"Connecting to GameCircle.....", Toast.LENGTH_SHORT).show();
		}
	}

	public void uploadLeaderBoard(int pScore) {

		Log.d(TAG, "upload socre Client = " + getLeaderBoardClient());

		if (getLeaderBoardClient() != null) {
			AGResponseHandle<SubmitScoreResponse> handle = getLeaderBoardClient()
					.submitScore(LEADERBOARD_ID, pScore);
			handle.setCallback(new AGResponseCallback<SubmitScoreResponse>() {

				@Override
				public void onComplete(SubmitScoreResponse result) {
					if (result.isError()) {
						// upload score faild
						Log.d(TAG, "up load sccess");
					} else {

						Log.d(TAG, "upload faild");
					}
				}
			});
		} else {
			Toast.makeText(AmazonLeaderBoard.this.mActivity,"Connecting to GameCircle.....", Toast.LENGTH_SHORT).show();
			
		}
	}

}
