/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride.actors;

import com.hardride.actors.base.Actor;
import com.hardride.models.VehicleModel;

import android.content.Context;

public class VehicleActor extends Actor {
	
	protected static VehicleModel mVehicleModel;
	
	public VehicleActor(Context context) {
		super(context);
		
		if (mVehicleModel == null) {
			mVehicleModel = new VehicleModel(context);
		}
		
		setModel(mVehicleModel);
	}
}
