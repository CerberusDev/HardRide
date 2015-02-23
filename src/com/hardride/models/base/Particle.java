/*
 * Hard Ride
 * 
 * Copyright (C) 2015
 *
 */

package com.hardride.models.base;

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
		int verticesAmount = 4;
		mIndicesAmount = 6;
		
		mVerticesRawData = new float[(FLOATS_PER_POSITION + FLOATS_PER_NORMAL) * verticesAmount];
		mIndicesRawData = new short[mIndicesAmount];
		
		mVerticesRawData[0] = -2.0f;
		mVerticesRawData[1] = 2.0f;
		mVerticesRawData[2] = 0.0f;
		mVerticesRawData[3] = 10.0f;
		mVerticesRawData[4] = 0.0f;
		mVerticesRawData[5] = 0.0f;
				
		mVerticesRawData[6] = -2.0f;
		mVerticesRawData[7] = -2.0f;
		mVerticesRawData[8] = 0.0f;
		mVerticesRawData[9] = 10.0f;
		mVerticesRawData[10] = 0.0f;
		mVerticesRawData[11] = 0.0f;
		
		mVerticesRawData[12] = 2.0f;
		mVerticesRawData[13] = -2.0f;
		mVerticesRawData[14] = 0.0f;
		mVerticesRawData[15] = 10.0f;
		mVerticesRawData[16] = 0.0f;
		mVerticesRawData[17] = 0.0f;
		
		mVerticesRawData[18] = 2.0f;
		mVerticesRawData[19] = 2.0f;
		mVerticesRawData[20] = 0.0f;
		mVerticesRawData[21] = 10.0f;
		mVerticesRawData[22] = 0.0f;
		mVerticesRawData[23] = 0.0f;
				
		mIndicesRawData[0] = 0;
		mIndicesRawData[1] = 2;
		mIndicesRawData[2] = 1;
		mIndicesRawData[3] = 0;
		mIndicesRawData[4] = 3;
		mIndicesRawData[5] = 2;
		
		sendModelDataToGPUBuffers();
	}
}
