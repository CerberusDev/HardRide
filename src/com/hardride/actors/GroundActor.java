/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride.actors;

import com.hardride.actors.base.Actor;
import com.hardride.models.PlatformModel;
import com.hardride.models.base.Model;

import android.content.Context;

public class GroundActor extends Actor {
	
	protected static Model msModel;
	
	public GroundActor(Context context) {
		super(context);
		
		if (msModel == null) {
			msModel = new PlatformModel(context);
		}
		
		setModel(msModel);
	}
	
	public GroundActor(Context context, float x, float y, float z, float yaw, float pitch, float roll) {
		super(context, x, y, z, yaw, pitch, roll);
			
		if (msModel == null) {
			msModel = new PlatformModel(context);
		}
			
		setModel(msModel);		
	}
}
