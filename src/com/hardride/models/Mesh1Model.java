/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride.models;

import com.hardride.models.base.Model;

import android.content.Context;

public class Mesh1Model extends Model {
	
    public Mesh1Model(Context context) {
    	super("mesh1.raw", context);
    	
    	mCollisionRectSizeX = 2.0f;
    	mCollisionRectSizeZ = 20.0f;
    }
}
