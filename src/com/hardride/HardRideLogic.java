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
	private float mMoveAngle = 0.0f;
	private float mAccelAngle = 0.0f;
	
	private final float mBaseSpeed = 25.0f;
	private float mMoveInterpSpeed = 2.4f;
	private float mAccelChangeFactor = 3.0f;
	
	private float mLastUpdateTime;
	
	public HardRideLogic() {
		mInput[InputType.NONE.ordinal()] = 2;
		mLastUpdateTime = SystemClock.uptimeMillis();
	}
	
	public void update() {
		float currTime = SystemClock.uptimeMillis();
		float dT = (currTime - mLastUpdateTime) / 1000.0f;
		
		if (mInput[InputType.LEFT.ordinal()] > 0) {
			mAccelAngle += mAccelChangeFactor * dT;
		}
		if (mInput[InputType.RIGHT.ordinal()] > 0) {
			mAccelAngle -= mAccelChangeFactor * dT;
		}
		
		float diff = mAccelAngle - mMoveAngle;
		mMoveAngle += diff * dT * mMoveInterpSpeed;
		
		mVehicle.setPitch((float) Math.toDegrees(mAccelAngle));
		mMoveDir[0] = (float) Math.sin(mMoveAngle);
		mMoveDir[1] = (float) Math.cos(mMoveAngle);
		
		mVehicle.setX(mVehicle.getX() + mMoveDir[0] * dT * mBaseSpeed);
		mVehicle.setZ(mVehicle.getZ() + mMoveDir[1] * dT * mBaseSpeed);
		
		mRenderer.updateViewMatrix(mVehicle.getX(), mVehicle.getZ(), mMoveDir[0], mMoveDir[1]);
		
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
