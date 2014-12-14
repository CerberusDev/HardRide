/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride.actors;

import com.hardride.actors.base.Actor;
import com.hardride.models.CubeModel;

import android.content.Context;

public class CubeActor extends Actor {
	
	protected static CubeModel mCube;
	
	public CubeActor(Context context) {
		super(context);
		
		if (mCube == null) {
			mCube = new CubeModel(context);
		}
		
		setModel(mCube);
	}
}
