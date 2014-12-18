/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride.actors;

import com.hardride.actors.base.Actor;
import com.hardride.models.PlatformModel;
import android.content.Context;

public class GroundActor extends Actor {
	
	protected static PlatformModel mPlatformModel;
	
	public GroundActor(Context context) {
		super(context);
		
		if (mPlatformModel == null) {
			mPlatformModel = new PlatformModel(context);
		}
		
		setModel(mPlatformModel);
	}
}
