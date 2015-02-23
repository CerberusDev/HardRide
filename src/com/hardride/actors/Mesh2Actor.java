/*
 * Hard Ride
 * 
 * Copyright (C) 2015
 *
 */

package com.hardride.actors;

import com.hardride.actors.base.Actor;
import com.hardride.models.Mesh2Model;
import com.hardride.models.base.Model;

import android.content.Context;

public class Mesh2Actor extends Actor {
	
	protected static Model msModel;
	
	public Mesh2Actor(Context context) {
		super(context);
		
		if (msModel == null) {
			msModel = new Mesh2Model(context);
		}
		
		setModel(msModel);
	}
	
	public Mesh2Actor(Context context, float x, float y, float z, float yaw, float pitch, float roll) {
		super(context, x, y, z, yaw, pitch, roll);
			
		if (msModel == null) {
			msModel = new Mesh2Model(context);
		}
			
		setModel(msModel);		
	}
}
