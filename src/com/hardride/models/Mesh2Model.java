/*
 * Hard Ride
 * 
 * Copyright (C) 2015
 *
 */

package com.hardride.models;

import com.hardride.models.base.Model;

import android.content.Context;

public class Mesh2Model extends Model {
	
    public Mesh2Model(Context context) {
    	super("mesh2.raw", context);
    	
    	mCollisionRectSizeX = 2.0f;
    	mCollisionRectSizeZ = 2.0f;
    }
}
