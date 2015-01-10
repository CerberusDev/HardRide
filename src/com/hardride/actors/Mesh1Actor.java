/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride.actors;

import com.hardride.actors.base.Actor;
import com.hardride.models.Mesh1Model;
import com.hardride.models.base.Model;

import android.content.Context;

public class Mesh1Actor extends Actor {
	
	protected static Model msModel;
	
	public Mesh1Actor(Context context) {
		super(context);
		
		if (msModel == null) {
			msModel = new Mesh1Model(context);
		}
		
		setModel(msModel);
	}
	
	public Mesh1Actor(Context context, float x, float y, float z, float yaw, float pitch, float roll) {
		super(context, x, y, z, yaw, pitch, roll);
			
		if (msModel == null) {
			msModel = new Mesh1Model(context);
		}
			
		setModel(msModel);		
	}
}
