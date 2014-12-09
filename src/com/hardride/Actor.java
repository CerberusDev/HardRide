/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class Actor {
	protected ShaderSet mShader;
	
	protected final FloatBuffer vertexBuffer;
	protected final FloatBuffer normalBuffer;
	protected final ShortBuffer indexBuffer;
    
	protected float mYaw;
	protected float mPitch;
	protected float mRoll;
    
	protected float mX;
	protected float mY;
	protected float mZ;
    
	protected final float[] mRotationMatrix = new float[16];
	protected final float[] mTranslationMatrix = new float[16];
	protected final float[] mModelMatrix = new float[16];
	protected final float[] mMVMatrix = new float[16];
	protected final float[] mMVPMatrix = new float[16];

	protected int mFLOATS_PER_VERTEX;
	protected float mVertices[];
	protected float mNormals[];
	protected short mIndices[];
	
    public Actor(ShaderSet shaderProgram, String modelName, Context context) {
    	mShader = shaderProgram;
    	loadModel(modelName, context);
    	
    	updateRotationMatrix();
    	updateTranslationMatrix();
    	
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(mVertices.length * 4);	// 4 bytes for float
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(mVertices);
        vertexBuffer.position(0);
        
        // initialize vertex byte buffer for shape normals
        ByteBuffer nbb = ByteBuffer.allocateDirect(mNormals.length * 4);	// 4 bytes for float
        nbb.order(ByteOrder.nativeOrder());
        normalBuffer = nbb.asFloatBuffer();
        normalBuffer.put(mNormals);
        normalBuffer.position(0);
        
        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(mIndices.length * 2);	// 2 bytes for short
        dlb.order(ByteOrder.nativeOrder());
        indexBuffer = dlb.asShortBuffer();
        indexBuffer.put(mIndices);
        indexBuffer.position(0);
        
        initUniforms();
    }

    protected void initUniforms() {
        mShader.use();
        mShader.unfiormSetVec4("u_Color", new float[]{1.0f, 0.0f, 0.0f, 1.0f});  	
    }
    
    public void draw(float[] ViewMatrix, float[] mProjectionMatrix) {
        mShader.use();
        
        Matrix.multiplyMM(mMVMatrix, 0, ViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVMatrix, 0);
        
        mShader.unfiormSetMat4("u_MVMatrix", mMVMatrix);
        mShader.unfiormSetMat4("u_MVPMatrix", mMVPMatrix);
        
        mShader.attribEnableAndSetDataFloat("a_Position", mFLOATS_PER_VERTEX, vertexBuffer);
        mShader.attribEnableAndSetDataFloat("a_Normal", mFLOATS_PER_VERTEX, normalBuffer);
        
        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndices.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        mShader.attribDisable("a_Position");
        mShader.attribDisable("a_Normal");
    }

	protected void loadModel(String modelName, Context context) {
		List<String> strLines = new ArrayList<String>();
		
		try {
        	InputStream ims = context.getAssets().open("models/" + modelName);
        	DataInputStream in = new DataInputStream(ims);
        	BufferedReader br = new BufferedReader(new InputStreamReader(in));
        	String line;
        	while ((line = br.readLine()) != null)
        		strLines.add(line);
            ims.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boolean headerRead = false;
		int verticesAmount;
		int indicesAmount;
		
		int vIdx = 0, nIdx = 0, iIdx = 0;
		
		for (int i = 0; i < strLines.size(); i++) {
			String line = strLines.get(i);
			
			if (!line.isEmpty() && !line.startsWith("//")) {
				if (!headerRead) {
					String[] tokens = line.split("[ ]+");
					verticesAmount = Integer.parseInt(tokens[0]);
					mFLOATS_PER_VERTEX = Integer.parseInt(tokens[1]);
					indicesAmount = Integer.parseInt(tokens[2]);
					
					mVertices = new float[mFLOATS_PER_VERTEX * verticesAmount];
					mNormals = new float[mFLOATS_PER_VERTEX * verticesAmount];
					mIndices = new short[indicesAmount];
					headerRead = true;
				}
				else if (line.startsWith("v ")) {
					String[] tokens = line.split("[ ]+");
					mVertices[vIdx] = Float.parseFloat(tokens[1]);
					mVertices[vIdx + 1] = Float.parseFloat(tokens[2]);
					mVertices[vIdx + 2] = Float.parseFloat(tokens[3]);
					vIdx += 3;
				}
				else if (line.startsWith("vn ")) {
					String[] tokens = line.split("[ ]+");
					mNormals[nIdx] = Float.parseFloat(tokens[1]);
					mNormals[nIdx + 1] = Float.parseFloat(tokens[2]);
					mNormals[nIdx + 2] = Float.parseFloat(tokens[3]);
					nIdx += 3;
				}
				else if (line.startsWith("f ")) {
					String[] tokens = line.split("[ ]+");
					for (int j = 1; j < tokens.length; j++) {
						mIndices[iIdx++] = Short.parseShort(tokens[j]);
					}
				}			
			}
		}
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
