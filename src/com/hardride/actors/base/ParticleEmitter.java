/*
 * Hard Ride
 * 
 * Copyright (C) 2015
 *
 */

package com.hardride.actors.base;

import android.content.Context;
import android.os.SystemClock;

import com.hardride.HardRideRenderer;
import com.hardride.models.base.Particle;
import com.hardride.shaders.ParticleShaderSet;

public class ParticleEmitter extends Actor {
	protected static Particle msModel;
	
	protected final float[] mModelViewMatrix = new float[16];
	
	protected float mLifeTime;	// in miliseconds
	protected float mSpawnTime;
	
	protected HardRideRenderer mRenderer;
	
	public ParticleEmitter(Context context, HardRideRenderer renderer, float lifeTime) {
		super(context);
		
		mLifeTime = lifeTime * 1000.0f;
		mSpawnTime = SystemClock.uptimeMillis();
		mRenderer = renderer;
		
		if (msModel == null) {
			msModel = new Particle();
		}
		
		setModel(msModel);
	}
	
	public ParticleEmitter(Context context, HardRideRenderer renderer, float x, float y, float z, float lifeTime) {
		super(context, x, y, z, 0.0f, 0.0f, 0.0f);
			
		mLifeTime = lifeTime * 1000.0f;
		mSpawnTime = SystemClock.uptimeMillis();
		mRenderer = renderer;
		
		if (msModel == null) {
			msModel = new Particle();
		}
			
		setModel(msModel);	
	}
	
    public void drawParticle(float[] ProjectionMatrix, float[] ViewMatrix, ParticleShaderSet shader, float currTime) {    
    	float lifeProgress = (currTime - mSpawnTime) / mLifeTime;
    	
    	if (lifeProgress > 1.0) {
    		mRenderer.clearCollisionParticle();
    	} else {    		
    		shader.unfiormSetMat4(shader.U_MODEL_MATRIX, mModelMatrix);
    		shader.unfiormSetMat4(shader.U_VIEW_MATRIX, ViewMatrix);
    		shader.unfiormSetMat4(shader.U_PROJECTION_MATRIX, ProjectionMatrix);
    		
    		shader.unfiormSetFloat(shader.U_LIFETIME, lifeProgress);
    		
    		((Particle)mModel).draw(shader);
    	}
    	
    }

}
