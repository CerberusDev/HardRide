/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride.models;

import com.hardride.models.base.Model;

import android.content.Context;

public class VehicleFlatModel extends Model {
	
    public VehicleFlatModel(Context context) 
    {
    	super("vehicleHi.obj", context);
    	    	
    	mCollisionRectSizeX = 8.0f;
    	mCollisionRectSizeZ = 10.0f;
    }
}
