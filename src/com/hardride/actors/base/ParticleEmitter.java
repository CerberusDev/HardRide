/*
 * Hard Ride
 * 
 * Copyright (C) 2015
 *
 */

package com.hardride.actors.base;

import android.content.Context;
import android.opengl.Matrix;

import com.hardride.models.base.Particle;
import com.hardride.models.base.Renderable;
import com.hardride.shaders.base.BaseObjectShaderSet;

public class ParticleEmitter extends Actor {
	protected static Renderable msModel;
	
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
	
    public void drawParticle(float[] ProjectionMatrix, float[] ViewMatrix, BaseObjectShaderSet shader) {     
    	Matrix.multiplyMM(mModelViewMatrix, 0, ViewMatrix, 0, mModelMatrix, 0);
    	
    	mModelViewMatrix[0] = -1.0f;	mModelViewMatrix[1] = 0.0f;		mModelViewMatrix[2] = 0.0f;
    	mModelViewMatrix[4] = 0.0f;		mModelViewMatrix[5] = 1.0f;		mModelViewMatrix[6] = 0.0f;
    	mModelViewMatrix[8] = 0.0f;		mModelViewMatrix[9] = 0.0f;		mModelViewMatrix[10] = -1.0f;
    	
    	Matrix.multiplyMM(mMVPMatrix, 0, ProjectionMatrix, 0, mModelViewMatrix, 0);
    	
    	shader.unfiormSetMat4(shader.U_MODEL_MATRIX, mModelMatrix);
        shader.unfiormSetMat4(shader.U_MVPMATRIX, mMVPMatrix);
        
        mModel.draw(shader);
    }

}
