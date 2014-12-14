/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride.actors.base;

import com.hardride.models.CubeModel;

import android.content.Context;

public class DebugCube extends Actor {
	
	protected static CubeModel mCube;
	
	public DebugCube(Context context, float[] color) {
		super(context, color);
		
		if (mCube == null) {
			mCube = new CubeModel(context);
		}
		
		setModel(mCube);
	}
}
