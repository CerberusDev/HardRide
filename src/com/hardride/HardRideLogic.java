/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride;

import android.os.SystemClock;
import com.hardride.actors.VehicleActor;
import com.hardride.actors.base.Actor;

enum GameState {
	INITIAL_SCREEN, 
	GAME, 
	GAME_OVER;
	
	public static final int size = InputType.values().length;
}

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
	
	private GameState mGameState = GameState.INITIAL_SCREEN;
	
	private boolean mbUniqueTap = false;
	
	public HardRideLogic() {
		mInput[InputType.NONE.ordinal()] = 2;
		mLastUpdateTime = SystemClock.uptimeMillis();
	}
	
	public void update() {
		float currTime = SystemClock.uptimeMillis();
		float dT = (currTime - mLastUpdateTime) / 1000.0f;
		
		if (mGameState == GameState.GAME) {
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
			
			for (Actor a : mRenderer.getActors()) {
				if (a.checkIntersect(mVehicle.getX(), mVehicle.getZ()))
					mGameState = GameState.GAME_OVER;
			}
		}
		mLastUpdateTime = currTime;
	}
	
	public void setRenderer(HardRideRenderer renderer) {
		mRenderer = renderer;
	}
	
	public void setVehicle(VehicleActor vehicle) {
		mVehicle = vehicle;
	}
	
	public void tapPress() {
		if (mGameState == GameState.GAME_OVER)
			mbUniqueTap = true;
	}
	
	public void tapRelease() {
		switch (mGameState) {
		case INITIAL_SCREEN:
			mGameState = GameState.GAME;
			break;
		case GAME:
			break;
		case GAME_OVER:		
			if (mbUniqueTap) {
				mVehicle.setX(-70.0f);
				mVehicle.setZ(-70.0f);
				mVehicle.setPitch(0.0f);
				
				mAccelAngle = 0.0f;
				mMoveAngle = 0.0f;
				
				mRenderer.updateViewMatrix(mVehicle.getX(), mVehicle.getZ(), 0.0f, 1.0f);
				
				mGameState = GameState.INITIAL_SCREEN;
				mbUniqueTap = false;
			}
			break;
		}
	}
	
	public void incInputState(InputType input) {
		mInput[input.ordinal()] += 1;
	}
	
	public void decInputState(InputType input) {	
		mInput[input.ordinal()] -= 1;
	}
}
