package com.asianux.musicplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年10月6日 下午10:19:22
 * 类说明
 */

public class MusicViewGroup extends ViewGroup {
	
	private Scroller myScroller = null;
	private boolean slideOpen = false;
	private boolean bottomOpen = false;
	
	private boolean debug = false;
	private boolean isLeft = false;
	private float pressX = 0;
	private float pressY = 0;
	private float releaseX = 0;
	private float releaseY = 0;
	
	private InterceptListener interceptListener = null;
	
	public interface InterceptListener
	{
		public void open();
		public void close();
		public void open_bottom();
		public void close_bottom();
	}
	

	public MusicViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);		
		myScroller =  new Scroller(getContext());
	}

	public MusicViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		myScroller =  new Scroller(getContext());
	}

	public MusicViewGroup(Context context) {
		super(context);
		myScroller =  new Scroller(getContext());
	}
	
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		makeMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	public void makeMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
	
		View menuView = getChildAt(0);
		
		menuView.measure(240, heightMeasureSpec);
		
		View  contentView = getChildAt(1);
		contentView.measure(widthMeasureSpec, heightMeasureSpec);
		
		View bootomView = getChildAt(2);
		bootomView.measure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub	
		
		if(debug)
		{
			System.out.println("l="+l+"\t"+"t="+t+"\t"+"r="+r+"\t"+"b="+b);
		}
		
		if (isLeft) {
			View menuView = getChildAt(0);
			menuView.layout(-240, 0, 0, b);

			View contentView = getChildAt(1);
			contentView.layout(l, t, r, b);
			
			View bootomView = getChildAt(2);
			bootomView.layout(l, t, r, b);
			
		} else {

			View menuView = getChildAt(0);
			menuView.layout(getWidth(), 0, getWidth() + 240, b);
			
			View contentView = getChildAt(1);
			contentView.layout(l, t, r, b);
			
			View bootomView = getChildAt(2);
			//bootomView.layout(0, getHeight(), 0, getHeight()+bootomView.getHeight());
			bootomView.layout(l, getHeight(), r, getWidth()+bootomView.getHeight());
			
			if(debug)
			{
				System.out.println(l+"\t"+t+"\t"+r+"\t"+b);
			}
			
		}

	}
	
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub

		if(myScroller.computeScrollOffset())
		{
			scrollTo(myScroller.getCurrX(), myScroller.getCurrY());
		}
	}
	
	@Override
	public void scrollTo(int x, int y) {
		// TODO Auto-generated method stub
		super.scrollTo(x, y);
		postInvalidate();
	}
	
	public void openSlide()
	{	
		slideOpen = true;
		
		if (!isLeft) {
			myScroller.startScroll(getScrollX(), 0, 240, 0, Math.abs(-240) * 2);
			invalidate();
		} else {
			
			if (debug) {
				System.out.println("open = " + getScrollX() + "\t"
						+ getScrollY());
			}
			myScroller
					.startScroll(getScrollX(), 0, -240, 0, Math.abs(-240) * 2);
			invalidate();
		}
		
	}
	
	public void closeSlide()
	{
		slideOpen = false;

		if (!isLeft) {
			myScroller
					.startScroll(getScrollX(), 0, -240, 0, Math.abs(-240) * 2);
			invalidate();
			
		} else {

			myScroller.startScroll(getScrollX(), 0, 240, 0, Math.abs(-240) * 2);
			invalidate();
		}
	}
	
	public void openSlideBottom()
	{
		bottomOpen = true;
		
		if(debug)
		{
			System.out.println("open position y = "+getHeight());
		}
		
		myScroller.startScroll(0, getScrollY(), 0, getHeight(), 480);
		invalidate();
	}
	
	public void closeSlideBottom()
	{
		bottomOpen = false;
		
		if(debug)
		{
			System.out.println("close position y = "+getHeight());
		}
		
		myScroller.startScroll(0, getScrollY(), 0, -getHeight(), 480);
		invalidate();
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN :
			pressX = ev.getX();
			pressY = ev.getY();
			
			break;
		case MotionEvent.ACTION_UP:
			releaseX = ev.getX();
			releaseY = ev.getY();
			
			float stepX = releaseX - pressX;
			float stepY = releaseY - pressY;
			
			System.out.println("rawY   2 "+ev.getRawY());
			
			if(stepX > 300.0)
			{
				if(slideOpen && !bottomOpen)
				{
					interceptListener.close();
				}
			}
			else if(stepX < -300.0)
			{
				if(!slideOpen && !bottomOpen)
				{
					interceptListener.open();
				}
			}else if(stepY >300.0)
			{
				if(bottomOpen && !slideOpen)
				{
					interceptListener.close_bottom();
				}
			}
			else if(stepY < -300.0)
			{
				if(!bottomOpen && !slideOpen)
				{
					interceptListener.open_bottom();
				}
			}
			break;

		default:
			break;
		}	
		return super.onInterceptTouchEvent(ev);
	}
	
	public void setListener(InterceptListener listener)
	{
		this.interceptListener = listener;
	}
}
