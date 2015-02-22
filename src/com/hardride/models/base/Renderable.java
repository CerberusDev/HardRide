/*
 * Hard Ride
 * 
 * Copyright (C) 2015
 *
 */

package com.hardride.models.base;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

import com.hardride.actors.base.Actor;
import com.hardride.shaders.base.BaseObjectShaderSet;

public class Renderable {
	
	protected int mVBO[] = new int[1];
	protected int mIBO[] = new int[1];
	
	protected int FLOATS_PER_POSITION = 3;
	protected int FLOATS_PER_NORMAL = 3;
	
	protected float mVerticesRawData[];
	protected short mIndicesRawData[];
	protected int mIndicesAmount;
	
	protected void sendModelDataToGPUBuffers() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(mVerticesRawData.length * Actor.BYTES_PER_FLOAT);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer newVertexBuffer = vbb.asFloatBuffer();
        newVertexBuffer.put(mVerticesRawData);
        newVertexBuffer.position(0);
        
        ByteBuffer ibb = ByteBuffer.allocateDirect(mIndicesRawData.length * Actor.BYTES_PER_SHORT);
        ibb.order(ByteOrder.nativeOrder());
        ShortBuffer indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(mIndicesRawData);
        indexBuffer.position(0);
        
        GLES20.glGenBuffers(1, mVBO, 0);
        GLES20.glGenBuffers(1, mIBO, 0);
               
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVBO[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, newVertexBuffer.capacity() * Actor.BYTES_PER_FLOAT,
        		newVertexBuffer, GLES20.GL_STATIC_DRAW);       

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, mIBO[0]);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.capacity() * Actor.BYTES_PER_SHORT, 
        		indexBuffer, GLES20.GL_STATIC_DRAW);
        
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);		
	}
	
	public void draw(BaseObjectShaderSet shader) {
        final int stride = (FLOATS_PER_POSITION + FLOATS_PER_NORMAL) * Actor.BYTES_PER_FLOAT;
        
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVBO[0]);
        GLES20.glVertexAttribPointer(shader.A_POSITION, FLOATS_PER_POSITION, GLES20.GL_FLOAT, false, stride, 0);
        GLES20.glVertexAttribPointer(shader.A_NORMAL, FLOATS_PER_NORMAL, GLES20.GL_FLOAT, false, stride, 
        		FLOATS_PER_POSITION * Actor.BYTES_PER_FLOAT);
        
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, mIBO[0]);
        
        // Draw the model
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndicesAmount, GLES20.GL_UNSIGNED_SHORT, 0);
    }
}
