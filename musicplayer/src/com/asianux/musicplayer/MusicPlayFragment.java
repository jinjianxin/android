package com.asianux.musicplayer;

import java.util.ArrayList;
import java.util.List;

import com.asianux.musicplayer.PlayView.MyViewPagerAdapter;
import com.asianux.service.MusicPlayer;
import com.asianux.utils.LrcInfo;
import com.asianux.utils.LrcItem;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年10月11日 下午9:55:22
 * 类说明
 */

public class MusicPlayFragment extends Fragment{
	
	private List<View> viewList = null;
	private ViewPager viewPage = null;
	private SeekBar seekBar = null;
	
	private static final int playNormal = 0;
	private static final int playCycle = 1;
	private static final int allCycle = 2;
	private static final int playRandom = 3;
	
	private Button volumeButton = null;
	private Button prevButton = null;
	private Button playButton = null;
	private Button nextButton = null;
	private Button modeButton = null;
	

	private TextView playViewFile = null;
	private TextView playViewLrc = null;
	private TextView playViewSinger = null;
	private TextView visualizerText = null;
	private ImageView playViewAlbum = null;
	private VisualizerView viewVisualizerView = null;
	
	private LrcView playLrcView = null;
	
	private LrcInfo lrcInfo = null;
	
	private int mode = 0;
	
	private boolean isPlay = false;
	
	/*
	 * 三个标签页
	 */
	private View albumView = null;
	private View lrcView = null;
	private View playViewVisualizer = null;
	
	
	public interface MusicPlayFragmentListener
	{
		public void next();
		public void prev();
		public void play();
		public void pause();
		public void setSeekBarProgress(int currentTime);
	}
	
	private MusicPlayFragmentListener musicPlayFragmentListener = null;
	
	public MusicPlayFragment(MusicPlayFragmentListener listener)
	{
		this.musicPlayFragmentListener =listener;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.play_view, container, false);
		//View view = inflater.inflate(R.layout.activity_main, container, false);
	
		init(view,inflater);
		
		return view;
	}

	
	public void init(View view, LayoutInflater inflater) {

		viewList = new ArrayList<View>();
		viewPage = (ViewPager) view.findViewById(R.id.viewPage);
		seekBar = (SeekBar) view.findViewById(R.id.seekBar);

		volumeButton = (Button) view.findViewById(R.id.volumeButton);
		prevButton = (Button) view.findViewById(R.id.prevButton);
		playButton = (Button) view.findViewById(R.id.playButton);
		nextButton = (Button) view.findViewById(R.id.nextButton);
		modeButton = (Button) view.findViewById(R.id.modeButton);

		playViewVisualizer = inflater.inflate(R.layout.play_view_visualizer,
				null);
		albumView = inflater.inflate(R.layout.play_view_album, null);
		lrcView = inflater.inflate(R.layout.play_view_lrc, null);

		viewList.add(playViewVisualizer);
		viewList.add(albumView);
		viewList.add(lrcView);

		viewPage.setAdapter(new MyViewPagerAdapter(viewList));
		viewPage.setCurrentItem(1);

		volumeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

		playViewAlbum = (ImageView) albumView
				.findViewById(R.id.play_view_album);
		playViewFile = (TextView) albumView.findViewById(R.id.play_view_file);
		playViewLrc = (TextView) albumView.findViewById(R.id.play_view_lrc);
		playViewSinger = (TextView) albumView
				.findViewById(R.id.play_view_singer);

		playLrcView = (LrcView) lrcView.findViewById(R.id.play_view_lrcView);

		viewVisualizerView = (VisualizerView) playViewVisualizer
				.findViewById(R.id.visualizerView);
		visualizerText = (TextView) playViewVisualizer
				.findViewById(R.id.visualizer_text);

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

				int currentTime = seekBar.getProgress();
				musicPlayFragmentListener.setSeekBarProgress(currentTime);

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub

			}
		});

		prevButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				musicPlayFragmentListener.prev();

				playLrcView.setLrcListEmpty();
				playLrcView.invalidate();
				playViewLrc.setText(R.string.lrcfile);
				visualizerText.setText(R.string.lrcfile);
				lrcInfo = null;
			}

		});

		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!isPlay) {
					isPlay = true;
					musicPlayFragmentListener.play();

					playButton.setBackgroundResource(R.drawable.pause_normal);
				} else {
					isPlay = false;
					musicPlayFragmentListener.pause();

					playButton
							.setBackgroundResource(R.drawable.btn_play_normal);
				}

			}
		});

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				musicPlayFragmentListener.next();
				playLrcView.setLrcListEmpty();
				playLrcView.invalidate();
				playViewLrc.setText(R.string.lrcfile);
				visualizerText.setText(R.string.lrcfile);
				lrcInfo = null;
			}
		});

		modeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (mode == playNormal) {
					mode = playCycle;
					// playMode.setImageResource(R.drawable.playmode_repeat_current);
					modeButton
							.setBackgroundResource(R.drawable.playmode_repeat_current);

				} else if (mode == playCycle) {
					mode = allCycle;
					modeButton
							.setBackgroundResource(R.drawable.playmode_repeat_all);

				} else if (mode == allCycle) {
					mode = playRandom;
					modeButton
							.setBackgroundResource(R.drawable.playmode_random);

				} else if (mode == playRandom) {
					mode = playNormal;
					modeButton
							.setBackgroundResource(R.drawable.playmode_normal);
				}
			}
		});
	}
	
	public void setMusicPlayInfo(LrcInfo lrcInfo)
	{
		
		System.out.println("5%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		
		playViewSinger.setText(lrcInfo.getSinger());
		
		List<LrcItem> list = lrcInfo.getLrcList();
		
		playLrcView.setLrcList(list);
		playLrcView.invalidate();
		playViewLrc.setText(R.string.lrcfile);
		visualizerText.setText(R.string.lrcfile);	
	}
	
	/**
	 * setLrcIndex 设置当心要显示歌词的位置
	 * (这里描述这个方法适用条件 – 可选)
	 * @param index 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	
	public void setLrcIndex(int index)
	{
		playLrcView.setIndex(index);
		playLrcView.invalidate();
	}
	
	public void setLrcText(String text)
	{
		playViewLrc.setText(text);
		visualizerText.setText(text);
	}
	
	
	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
	
	public void setSeekProgress(int progress)
	{
		seekBar.setProgress(progress);
	}
	
	public void setMaxSeekBar(int max)
	{
		seekBar.setMax(max);
	}
	
	public void updateVisualizer(byte[] fft)
	{
		viewVisualizerView.updateVisualizer(fft);
	}
	
	public void setPlayStatus(boolean status)
	{
		if(status)
		{
			playButton.setBackgroundResource(R.drawable.pause_pressed);
		}
		else
		{
			playButton.setBackgroundResource(R.drawable.pause_normal);
		}
	}
	
}
