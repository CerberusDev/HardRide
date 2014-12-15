/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride;

public class HardRideLogic {

	@SuppressWarnings("unused")
	private static final String TAG = "HardRideLogic";
	
	private HardRideRenderer mRenderer;
	
	private int mInput[] = new int[InputType.size];
	
	public HardRideLogic() {
		mInput[InputType.NONE.ordinal()] = 2;
	}
	
	public void setRenderer(HardRideRenderer renderer) {
		mRenderer = renderer;
	}
	
	public void incInputState(InputType input) {
		mInput[input.ordinal()] += 1;
	}
	
	public void decInputState(InputType input) {
		mInput[input.ordinal()] -= 1;
	}
	
	public void update() {
		if (mInput[InputType.LEFT.ordinal()] > 0) {
			mRenderer.rotateViewMatrix(1.0f);
		}
		if (mInput[InputType.RIGHT.ordinal()] > 0) {
			mRenderer.rotateViewMatrix(-1.0f);
		}
	}
}
