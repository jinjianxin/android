package com.asianux.musicplayer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author jjx E-mail:jinjianxinjin@gmail.com
 * @version 创建时间：2013年9月26日 下午3:20:22
 * 类说明
 */

public class VisualizerView extends View
{

	private byte[] mBytes;
	private float[] mPoints;
	private Rect mRect = new Rect();

	private Paint mForePaint = new Paint();
	private int mSpectrumNum = 48;
	private boolean mFirst = true;

	public VisualizerView(Context context)
	{
		super(context);
		init();
	}

	public VisualizerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public VisualizerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	
	
	private void init()
	{
		mBytes = null;

		mForePaint.setStrokeWidth(8f);
		mForePaint.setAntiAlias(true);
		//mForePaint.setColor(Color.rgb(0, 128, 255));
		mForePaint.setColor(Color.rgb(255, 192, 203));
	}

	public void updateVisualizer(byte[] fft)
	{

		byte[] model = new byte[fft.length / 2 + 1];

		model[0] = (byte) Math.abs(fft[0]);
		for (int i = 2, j = 1; j < mSpectrumNum;)
		{
			model[j] = (byte) Math.hypot(fft[i], fft[i + 1]);
			i += 2;
			j++;
		}
		mBytes = model;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		if (mBytes == null)
		{
			return;
		}

		if (mPoints == null || mPoints.length < mBytes.length * 4)
		{
			mPoints = new float[mBytes.length * 4];
		}

		mRect.set(0, 0, getWidth(), getHeight());

		final int baseX = mRect.width()/mSpectrumNum;
		final int height = mRect.height();

		for (int i = 0; i < mSpectrumNum ; i++)
		{
			if (mBytes[i] < 0)
			{
				mBytes[i] = 127;
			}
			
			final int xi = baseX*i + baseX/2;
			
			mPoints[i * 4] = xi;
			mPoints[i * 4 + 1] = height;
			
			mPoints[i * 4 + 2] = xi;
			mPoints[i * 4 + 3] = height - mBytes[i];
		}

		canvas.drawLines(mPoints, mForePaint);
	}
}