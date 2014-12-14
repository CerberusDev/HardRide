/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride;

import android.util.Log;

public class HardRideLogic {

	private static final String TAG = "HardRideLogic";
	
	private HardRideRenderer mRenderer;
	
	private boolean mInput[] = new boolean[2];
	
	public void setRenderer(HardRideRenderer renderer) {
		mRenderer = renderer;
	}
	
	public void setInput(boolean bNewInputState, int index) {
		mInput[index] = bNewInputState;
	}
	
	public void update() {
		//Log.i(TAG, "Input state: " + mInput[0] + "  " + mInput[1]);
		
		if (mInput[0]) {
			mRenderer.rotateViewMatrix(-1.0f);
		}
		if (mInput[1]) {
			mRenderer.rotateViewMatrix(1.0f);
		}
	}
}
