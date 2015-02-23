/*
 * Hard Ride
 * 
 * Copyright (C) 2015
 *
 */

package com.hardride.models.base;

import java.util.Random;

import android.opengl.GLES20;

import com.hardride.actors.base.Actor;
import com.hardride.shaders.ParticleShaderSet;

public class Particle extends Renderable {

	public void draw(ParticleShaderSet shader) {
        final int stride = (FLOATS_PER_POSITION + FLOATS_PER_NORMAL) * Actor.BYTES_PER_FLOAT;
        
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVBO[0]);
        GLES20.glVertexAttribPointer(shader.A_POSITION, FLOATS_PER_POSITION, GLES20.GL_FLOAT, false, stride, 0);
        GLES20.glVertexAttribPointer(shader.A_END_TRANSLATION, FLOATS_PER_NORMAL, GLES20.GL_FLOAT, false, stride, 
        		FLOATS_PER_POSITION * Actor.BYTES_PER_FLOAT);
        
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, mIBO[0]);
        
        // Draw the model
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndicesAmount, GLES20.GL_UNSIGNED_SHORT, 0);
    }
	
	public Particle() {
		int particlesAmount = 40;
		int verticesAmount = 4 * particlesAmount;
		mIndicesAmount = 6 * particlesAmount;
		
		mVerticesRawData = new float[(FLOATS_PER_POSITION + FLOATS_PER_NORMAL) * verticesAmount];
		mIndicesRawData = new short[mIndicesAmount];
		
		Random rand = new Random();
		
		int idxV = 0;
		int idxI = 0;
		
		for (int p = 0; p < particlesAmount; ++p) {
			float dirX = rand.nextFloat() * 20.0f - 10.0f;
			float dirY = rand.nextFloat() * 20.0f - 10.0f;
			float dirZ = rand.nextFloat() * 20.0f - 10.0f;
			
			for (int i = 0; i < 4; ++i) {
				mVerticesRawData[idxV++] = (i > 1) ? 2.0f : -2.0f;
				mVerticesRawData[idxV++] = (i == 0 || i == 3) ? 2.0f : -2.0f;
				mVerticesRawData[idxV++] = 0.0f;
				mVerticesRawData[idxV++] = dirX;
				mVerticesRawData[idxV++] = dirY;
				mVerticesRawData[idxV++] = dirZ;
			}
	
			mIndicesRawData[idxI++] = (short) (0 + p * 4);
			mIndicesRawData[idxI++] = (short) (2 + p * 4);
			mIndicesRawData[idxI++] = (short) (1 + p * 4);
			mIndicesRawData[idxI++] = (short) (0 + p * 4);
			mIndicesRawData[idxI++] = (short) (3 + p * 4);
			mIndicesRawData[idxI++] = (short) (2 + p * 4);
		}
		
		sendModelDataToGPUBuffers();
	}
}
