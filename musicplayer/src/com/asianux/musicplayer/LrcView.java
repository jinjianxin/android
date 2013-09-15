package com.asianux.musicplayer;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.asianux.utils.LrcItem;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月12日 下午8:49:27 类说明
 */

public class LrcView extends TextView {

	private Paint mPaint = null;
	private Paint currentPaint = null;
	private float mX;
	private int mY;
	private float middleY;
	private List<LrcItem> lrcList = null;
	private int index = 0;
	private static final int DY = 50;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<LrcItem> getLrcList() {
		return lrcList;
	}

	public void setLrcList(List<LrcItem> lrcList) {
		this.lrcList = lrcList;
	}

	public void setLrcListEmpty()
	{
		this.lrcList = null;
	}
	
	public LrcView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		initView();
	}

	public LrcView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView();
	}

	public LrcView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView();
	}

	void initView() {
		setFocusable(true);

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(30);
		mPaint.setColor(Color.GREEN);
		mPaint.setTypeface(Typeface.SERIF);

		currentPaint = new Paint();
		currentPaint.setAntiAlias(true);
		currentPaint.setColor(Color.RED);
		currentPaint.setTextSize(30);
		currentPaint.setTypeface(Typeface.SANS_SERIF);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawColor(0xEFeffff);
		
		if(lrcList != null && !lrcList.isEmpty())
		{
			Paint p2 = currentPaint;
			p2.setTextAlign(Paint.Align.CENTER);

			mPaint.setTextAlign(Paint.Align.CENTER);

			canvas.drawText(lrcList.get(index).getLrc(), mX, middleY, p2);

			float tempY = middleY;

			for (int i = index - 1; i >= 0; i--) {
				tempY = tempY - DY;
				if (tempY < 0) {
					break;
				}
				canvas.drawText(lrcList.get(i).getLrc(), mX, tempY, mPaint);

			}

			tempY = middleY;

			for (int i = index + 1; i < lrcList.size(); i++) {

				tempY = tempY + DY;
				if (tempY > mY) {
					break;
				}
				canvas.drawText(lrcList.get(i).getLrc(), mX, tempY, mPaint);
				// canvas.translate(0, DY);
			}
        
		}
		else
		{
			Paint p2 = currentPaint;
			p2.setTextAlign(Paint.Align.CENTER);

			mPaint.setTextAlign(Paint.Align.CENTER);

			canvas.drawText(this.getResources().getString(R.string.lrcfile), mX, middleY, p2);

		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);

		mX = w * 0.5f; // remember the center of the screen
		mY = h;
		middleY = h * 0.5f;
	}

}
