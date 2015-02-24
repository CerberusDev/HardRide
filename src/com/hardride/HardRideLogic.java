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
	GAME_PAUSED,
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
	
	private final float mBaseSpeed = 0.3f;
	private float mMoveInterpSpeed = 1.3f;
	private float mAccelChangeFactor = 2.8f;
	
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
			
			float radPitch = (float) -Math.toRadians(mVehicle.getPitch());
			
			float cos = (float) Math.cos(radPitch);
			float sin = (float) Math.sin(radPitch);
			
			float x = mVehicle.getX() + 4.0f;
			float z = mVehicle.getZ() + 5.0f;
	    	float modX1 = (float) (cos * (x - mVehicle.getX()) - sin * (z - mVehicle.getZ()) + mVehicle.getX());
	    	float modZ1 = (float) (sin * (x - mVehicle.getX()) + cos * (z - mVehicle.getZ()) + mVehicle.getZ());
			
	    	x = mVehicle.getX() + 4.0f;
			z = mVehicle.getZ() - 5.0f;
	    	float modX2 = (float) (cos * (x - mVehicle.getX()) - sin * (z - mVehicle.getZ()) + mVehicle.getX());
	    	float modZ2 = (float) (sin * (x - mVehicle.getX()) + cos * (z - mVehicle.getZ()) + mVehicle.getZ());
	    	
	    	x = mVehicle.getX() - 4.0f;
			z = mVehicle.getZ() + 5.0f;
	    	float modX3 = (float) (cos * (x - mVehicle.getX()) - sin * (z - mVehicle.getZ()) + mVehicle.getX());
	    	float modZ3 = (float) (sin * (x - mVehicle.getX()) + cos * (z - mVehicle.getZ()) + mVehicle.getZ());
	    	
	    	x = mVehicle.getX() - 4.0f;
			z = mVehicle.getZ() - 5.0f;
	    	float modX4 = (float) (cos * (x - mVehicle.getX()) - sin * (z - mVehicle.getZ()) + mVehicle.getX());
	    	float modZ4 = (float) (sin * (x - mVehicle.getX()) + cos * (z - mVehicle.getZ()) + mVehicle.getZ());
	    	
			for (Actor a : mRenderer.getActors()) {
				if (a.checkIntersect(modX1, modZ1))
					endGame();
				
				if (a.checkIntersect(modX2, modZ2))
					endGame();
				
				if (a.checkIntersect(modX3, modZ3))
					endGame();
				
				if (a.checkIntersect(modX4, modZ4))
					endGame();
			}
		}
		mLastUpdateTime = currTime;
	}
	
	public void endGame() {
		mGameState = GameState.GAME_OVER;
		mRenderer.spawnCollisionParticle(mVehicle.getX(), mVehicle.getY(), mVehicle.getZ(), 4.0f);
	}
	
	public void setRenderer(HardRideRenderer renderer) {
		mRenderer = renderer;
	}
	
	public void setVehicle(VehicleActor vehicle) {
		mVehicle = vehicle;
	}
	
	public void pauseGame() {
		if (mGameState == GameState.GAME) 
			mGameState = GameState.GAME_PAUSED;
	}
	
	public void unpauseGame() {
		if (mGameState == GameState.GAME_PAUSED) 
			mGameState = GameState.GAME;	
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
		default:
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
