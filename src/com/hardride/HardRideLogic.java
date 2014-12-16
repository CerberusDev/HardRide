/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride;

import com.hardride.actors.base.Actor;

public class HardRideLogic {

	@SuppressWarnings("unused")
	private static final String TAG = "HardRideLogic";
	
	private HardRideRenderer mRenderer;
	
	private int mInput[] = new int[InputType.size];
	
	private Actor mVehicle;
	private float mMoveSpeed[] = new float[2]; 
	
	
	public HardRideLogic() {
		mInput[InputType.NONE.ordinal()] = 2;
		
		mMoveSpeed[0] = 0.06f;
		mMoveSpeed[1] = 0.0f;
	}
	
	public void update() {
		mVehicle.setX(mVehicle.getX() + mMoveSpeed[0]);
		mVehicle.setZ(mVehicle.getZ() + mMoveSpeed[1]);
		
		if (mInput[InputType.LEFT.ordinal()] > 0) {
			mRenderer.rotateViewMatrix(1.0f);
		}
		if (mInput[InputType.RIGHT.ordinal()] > 0) {
			mRenderer.rotateViewMatrix(-1.0f);
		}
	}
	
	public void setRenderer(HardRideRenderer renderer) {
		mRenderer = renderer;
	}
	
	public void setVehicle(Actor vehicle) {
		mVehicle = vehicle;
	}
	
	public void incInputState(InputType input) {
		mInput[input.ordinal()] += 1;
	}
	
	public void decInputState(InputType input) {
		mInput[input.ordinal()] -= 1;
	}
}
