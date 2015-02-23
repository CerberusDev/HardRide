/*
 * Hard Ride
 * 
 * Copyright (C) 2015
 *
 */

package com.hardride.actors.base;

import android.content.Context;
import com.hardride.models.base.Particle;
import com.hardride.shaders.ParticleShaderSet;

public class ParticleEmitter extends Actor {
	protected static Particle msModel;
	
	protected final float[] mModelViewMatrix = new float[16];
	
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
	
    public void drawParticle(float[] ProjectionMatrix, float[] ViewMatrix, ParticleShaderSet shader) {     
    	
    	shader.unfiormSetMat4(shader.U_MODEL_MATRIX, mModelMatrix);
        shader.unfiormSetMat4(shader.U_VIEW_MATRIX, ViewMatrix);
        shader.unfiormSetMat4(shader.U_PROJECTION_MATRIX, ProjectionMatrix);
        ((Particle)mModel).draw(shader);
    }

}
