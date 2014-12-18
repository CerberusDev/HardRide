/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride;

import android.os.SystemClock;
import com.hardride.actors.VehicleActor;

public class HardRideLogic {

	@SuppressWarnings("unused")
	private static final String TAG = "HardRideLogic";
	
	private HardRideRenderer mRenderer;
	
	private int mInput[] = new int[InputType.size];
	
	private VehicleActor mVehicle;
	private float mMoveDir[] = new float[]{0.0f, 1.0f}; 
	private final float mBaseSpeed = 20.0f;
	private float mAngle = 0.0f;
	
	private float mLastUpdateTime;
	
	public HardRideLogic() {
		mInput[InputType.NONE.ordinal()] = 2;
		mLastUpdateTime = SystemClock.uptimeMillis();
	}
	
	public void update() {
		float currTime = SystemClock.uptimeMillis();
		float dT = (currTime - mLastUpdateTime) / 1000.0f;
		
		mVehicle.setX(mVehicle.getX() + mMoveDir[0] * dT * mBaseSpeed);
		mVehicle.setZ(mVehicle.getZ() + mMoveDir[1] * dT * mBaseSpeed);
		
		mRenderer.updateViewMatrix(mVehicle.getX(), mVehicle.getZ(), mMoveDir[0], mMoveDir[1]);
		
		if (mInput[InputType.LEFT.ordinal()] > 0) {
			mAngle += 0.05f;
			mVehicle.setPitch((float) Math.toDegrees(mAngle));
			mMoveDir[0] = (float) Math.sin(mAngle);
			mMoveDir[1] = (float) Math.cos(mAngle);
		}
		if (mInput[InputType.RIGHT.ordinal()] > 0) {
			mAngle -= 0.05f;
			mVehicle.setPitch((float) Math.toDegrees(mAngle));
			mMoveDir[0] = (float) Math.sin(mAngle);
			mMoveDir[1] = (float) Math.cos(mAngle);
		}
		
		mLastUpdateTime = currTime;
	}
	
	public void setRenderer(HardRideRenderer renderer) {
		mRenderer = renderer;
	}
	
	public void setVehicle(VehicleActor vehicle) {
		mVehicle = vehicle;
	}
	
	public void incInputState(InputType input) {
		mInput[input.ordinal()] += 1;
	}
	
	public void decInputState(InputType input) {
		mInput[input.ordinal()] -= 1;
	}
}
