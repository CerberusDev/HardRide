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
	private float mMoveSpeed[] = new float[]{0.0f, 1.0f}; 
	private final float mBaseSpeed = 5.0f;
	private float mAngle = 0.0f;
	
	public HardRideLogic() {
		mInput[InputType.NONE.ordinal()] = 2;
	}
	
	public void update() {
		float dT = 0.1f;
		
		mVehicle.setX(mVehicle.getX() + mMoveSpeed[0] * dT * mBaseSpeed);
		mVehicle.setZ(mVehicle.getZ() + mMoveSpeed[1] * dT * mBaseSpeed);
		
		mRenderer.updateViewMatrix(mVehicle.getX(), mVehicle.getZ(), mMoveSpeed[0], mMoveSpeed[1]);
		
		if (mInput[InputType.LEFT.ordinal()] > 0) {
			mAngle += 0.05f;
			mVehicle.setPitch((float) Math.toDegrees(mAngle));
			mMoveSpeed[0] = (float) Math.sin(mAngle);
			mMoveSpeed[1] = (float) Math.cos(mAngle);
		}
		if (mInput[InputType.RIGHT.ordinal()] > 0) {
			mAngle -= 0.05f;
			mVehicle.setPitch((float) Math.toDegrees(mAngle));
			mMoveSpeed[0] = (float) Math.sin(mAngle);
			mMoveSpeed[1] = (float) Math.cos(mAngle);
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
