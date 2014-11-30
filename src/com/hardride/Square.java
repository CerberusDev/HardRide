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

    private final FloatBuffer vertexBuffer;
    private final FloatBuffer normalBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mNormalsHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    
    private float mYaw;
    private float mPitch;
    private float mRoll;
    
    // number of coordinates per vertex in this array
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

    private final short drawOrder[] = { 
    		0, 2, 1, 0, 3, 2,
    		4, 5, 6, 4, 6, 7,
    		3, 6, 2, 3, 7, 6,
    		4, 1, 5, 4, 0, 1, 
    		};

    float color[] = { 0.4f, 0.0f, 0.1f, 1.0f };

    private final float[] mMVPMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    
    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Square(String vShader, String fShader) {
    	
    	updateRotationMatrix();
    	
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
        // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        ByteBuffer nbb = ByteBuffer.allocateDirect(
        // (# of coordinate values * 4 bytes per float)
                squareNormals.length * 4);
        nbb.order(ByteOrder.nativeOrder());
        normalBuffer = nbb.asFloatBuffer();
        normalBuffer.put(squareNormals);
        normalBuffer.position(0);
        
        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
        
        // prepare shaders and OpenGL program
        int vertexShader = HardRideRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER,
                vShader);

        int fragmentShader = HardRideRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fShader);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    public void draw(float[] ViewMatrix, float[] mProjectionMatrix) {
    	
        float[] scratch = new float[16];
        
        

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, ViewMatrix, 0);

        

        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
    	
    	
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        mNormalsHandle = GLES20.glGetAttribLocation(mProgram, "vNormal");
        GLES20.glEnableVertexAttribArray(mNormalsHandle);
        
        GLES20.glVertexAttribPointer(
        		mNormalsHandle, NORMALS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                0, normalBuffer);
        
        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        HardRideRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, scratch, 0);
        HardRideRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mNormalsHandle);
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
    
    protected void updateRotationMatrix() {
    	Matrix.setRotateM(mRotationMatrix, 0, mYaw, 1.0f, 0, 0);
    	Matrix.rotateM(mRotationMatrix, 0, mPitch, 0, 1.0f, 0);
    	Matrix.rotateM(mRotationMatrix, 0, mRoll, 0, 0, 1.0f);
    	
    	// This function probably can do all of this, but it doesn't work as intended
    	//Matrix.setRotateEulerM(mRotationMatrix, 0, mYaw, 0.0f, 0.0f);
    }
}