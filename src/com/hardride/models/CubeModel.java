/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride.models;

import com.hardride.models.base.Model;

import android.content.Context;

public class CubeModel extends Model {
	
    public CubeModel(Context context) {
    	super("cube.raw", context);
    	
    	mCollisionRectSizeX = 10.0f;
    	mCollisionRectSizeZ = 10.0f;
    }
}
