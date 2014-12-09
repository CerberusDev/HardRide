/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hardride;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class Square {
	private ShaderSet mShader;
	
    private final FloatBuffer vertexBuffer;
    private final FloatBuffer normalBuffer;
    private final ShortBuffer indexBuffer;
    
    private float mYaw;
    private float mPitch;
    private float mRoll;
    
    private float mX;
    private float mY;
    private float mZ;
    
    private final float[] mRotationMatrix = new float[16];
    private final float[] mTranslationMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];
    private final float[] mMVMatrix = new float[16];
    private final float[] mMVPMatrix = new float[16];
    
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
	        -5.0f,  5.0f, -5.0f,   	// top 		left 	near
	        -5.0f, -5.0f, -5.0f,   	// bottom 	left	near
	         5.0f, -5.0f, -5.0f,   	// bottom 	right	near
	         5.0f,  5.0f, -5.0f, 	// top 		right	near
			-5.0f,  5.0f,  5.0f,   	// top 		left 	far
			-5.0f, -5.0f,  5.0f,   	// bottom 	left	far
			 5.0f, -5.0f,  5.0f,   	// bottom 	right	far
		 	 5.0f,  5.0f,  5.0f 	// top 		right	far
			 }; 
    
    static final int NORMALS_PER_VERTEX = 3;
    static float squareNormals[] = {
	        -1.0f,  1.0f, -1.0f,   	// top 		left 	near
	        -1.0f, -1.0f, -1.0f,   	// bottom 	left	near
	         1.0f, -1.0f, -1.0f,   	// bottom 	right	near
	         1.0f,  1.0f, -1.0f, 	// top 		right	near
			-1.0f,  1.0f,  1.0f,   	// top 		left 	far
			-1.0f, -1.0f,  1.0f,   	// bottom 	left	far
			 1.0f, -1.0f,  1.0f,   	// bottom 	right	far
		 	 1.0f,  1.0f,  1.0f 	// top 		right	far
			 }; 	

    static final short drawOrder[] = { 
			0, 2, 1, 0, 3, 2,
			4, 5, 6, 4, 6, 7,
			3, 6, 2, 3, 7, 6,
			4, 1, 5, 4, 0, 1, 
			4, 3, 0, 4, 7, 3,
			1, 6, 5, 1, 2, 6,
			};
    
    public Square(ShaderSet shaderProgram) {
    	mShader = shaderProgram;
    	
    	updateRotationMatrix();
    	updateTranslationMatrix();
    	
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);	// 4 bytes for float
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);
        
        // initialize vertex byte buffer for shape normals
        ByteBuffer nbb = ByteBuffer.allocateDirect(squareNormals.length * 4);	// 4 bytes for float
        nbb.order(ByteOrder.nativeOrder());
        normalBuffer = nbb.asFloatBuffer();
        normalBuffer.put(squareNormals);
        normalBuffer.position(0);
        
        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);	// 2 bytes for short
        dlb.order(ByteOrder.nativeOrder());
        indexBuffer = dlb.asShortBuffer();
        indexBuffer.put(drawOrder);
        indexBuffer.position(0);
        
        mShader.use();
        mShader.unfiormSetVec4("u_Color", new float[]{1.0f, 0.0f, 0.0f, 1.0f});
    }

    public void draw(float[] ViewMatrix, float[] mProjectionMatrix) {
        mShader.use();
        
        Matrix.multiplyMM(mMVMatrix, 0, ViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVMatrix, 0);
        
        mShader.unfiormSetMat4("u_MVMatrix", mMVMatrix);
        mShader.unfiormSetMat4("u_MVPMatrix", mMVPMatrix);
        
        mShader.attribEnableAndSetDataFloat("a_Position", COORDS_PER_VERTEX, vertexBuffer);
        mShader.attribEnableAndSetDataFloat("a_Normal", NORMALS_PER_VERTEX, normalBuffer);
        
        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        mShader.attribDisable("a_Position");
        mShader.attribDisable("a_Normal");
    }

    public float getYaw() {
        return mYaw;
    }
    
    public float getPitch() {
        return mPitch;
    }
    
    public float getRoll() {
        return mRoll;
    }
    
    public float getX() {
        return mX;
    }
    
    public float getY() {
        return mY;
    }
    
    public float getZ() {
        return mZ;
    }
    
    public void setYaw(float angle) {
        mYaw = angle;
        updateRotationMatrix();
    }
    
    public void setPitch(float angle) {
        mPitch = angle;
        updateRotationMatrix();
    }
    
    public void setRoll(float angle) {
        mRoll = angle;
        updateRotationMatrix();
    }

    public void setX(float newPos) {
    	mX = newPos;
    	updateTranslationMatrix();
    }
    
    public void setY(float newPos) {
    	mY = newPos;
    	updateTranslationMatrix();
    }
    
    public void setZ(float newPos) {
    	mZ = newPos;
    	updateTranslationMatrix();
    }
    
    protected void updateRotationMatrix() {
    	Matrix.setRotateM(mRotationMatrix, 0, mYaw, 1.0f, 0, 0);
    	Matrix.rotateM(mRotationMatrix, 0, mPitch, 0, 1.0f, 0);
    	Matrix.rotateM(mRotationMatrix, 0, mRoll, 0, 0, 1.0f);
    	
    	// All above can be probably done by this function, but it doesn't work as intended
    	//Matrix.setRotateEulerM(mRotationMatrix, 0, mYaw, 0.0f, 0.0f);
    	
    	Matrix.multiplyMM(mModelMatrix, 0, mTranslationMatrix, 0, mRotationMatrix, 0);
    }
    
    protected void updateTranslationMatrix() {
    	Matrix.setIdentityM(mTranslationMatrix, 0);
    	Matrix.translateM(mTranslationMatrix, 0, mX, mY, mZ);
    	
    	Matrix.multiplyMM(mModelMatrix, 0, mTranslationMatrix, 0, mRotationMatrix, 0);
    }
}