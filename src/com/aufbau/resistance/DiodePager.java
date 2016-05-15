package com.aufbau.resistance;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

public class DiodePager extends ViewPager {

	private boolean enabled;
	private boolean halted;
	private float mStartDragX;

	public DiodePager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.enabled = true;
		this.halted = false;
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (this.enabled) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				float x = event.getX();
				if (mStartDragX < x)
				{
					//Left scroll 
					Toast.makeText(this.getContext(), "left swipe detected", Toast.LENGTH_SHORT).show();
					return true;
				}
				else {
					//Right scroll 
					break;
				}
			}
		}
		

		return (halted) ? false : super.onTouchEvent(event);

	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		
		if (this.enabled)  // view pager disable scrolling 
		{
			float x = event.getX();
			switch (event.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				mStartDragX = x;
				//Toast.makeText(this.getContext(), "Gesture beings at "+Float.toString(mStartDragX), Toast.LENGTH_SHORT).show();
				return false;
			
			}
		}
		

		return super.onInterceptTouchEvent(event);
	}

	// To enable/disable swipe unidirectionality
	public void setDiode(boolean enabled) {
		this.enabled = enabled;
	}
	
	// To set catch point
	public void setHalt(boolean halted) {
		this.halted = halted;
	}
}
