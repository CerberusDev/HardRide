/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride.models;

import com.hardride.models.base.Model;

import android.content.Context;

public class SmallCubeModel extends Model {
	
    public SmallCubeModel(Context context) {
    	super("small_cube.raw", context);
    	
    	mCollisionRectSizeX = 2.0f;
    	mCollisionRectSizeZ = 2.0f;
    }
}
