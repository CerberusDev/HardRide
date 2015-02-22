/*
 * Hard Ride
 * 
 * Copyright (C) 2015
 *
 */

package com.hardride.actors.base;

import android.content.Context;

import com.hardride.models.base.Particle;
import com.hardride.models.base.Renderable;

public class ParticleEmitter extends Actor {
	protected static Renderable msModel;
	
	public ParticleEmitter(Context context) {
		super(context);
		
		if (msModel == null) {
			msModel = new Particle();
		}
		
		setModel(msModel);
	}
	
	public ParticleEmitter(Context context, float x, float y, float z, float yaw, float pitch, float roll) {
		super(context, x, y, z, yaw, pitch, roll);
			
		if (msModel == null) {
			msModel = new Particle();
		}
			
		setModel(msModel);		
	}
}
