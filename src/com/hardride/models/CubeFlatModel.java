/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride.models;

import com.hardride.models.base.Model;

import android.content.Context;

public class CubeFlatModel extends Model {
	
    public CubeFlatModel(Context context) {
    	super("cube_flat.raw", context);
    	
    	mCollisionRectSizeX = 10.0f;
    	mCollisionRectSizeZ = 10.0f;
    }
}
