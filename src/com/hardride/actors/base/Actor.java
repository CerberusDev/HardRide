/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride.actors.base;

import com.hardride.models.base.Model;
import com.hardride.models.base.Renderable;
import com.hardride.shaders.base.BaseObjectShaderSet;

import android.content.Context;
import android.opengl.Matrix;

public class Actor {
	
	protected Renderable mModel;
	
	public static final int BYTES_PER_FLOAT = 4;
	public static final int BYTES_PER_SHORT = 2;
	
	protected float mYaw;
	protected float mPitch;
	protected float mRoll;
    
	protected float mX;
	protected float mY;
	protected float mZ;
	
	protected final float[] mRotationMatrix = new float[16];
	protected final float[] mTranslationMatrix = new float[16];
	protected final float[] mModelMatrix = new float[16];
	protected final float[] mMVPMatrix = new float[16];
	
	protected float mCollisionRectOffsetX;
	protected float mCollisionRectOffsetZ;
	
    public Actor(Context context) {    	
    	updateRotationMatrix();
    	updateTranslationMatrix();
    }
    
    public Actor(Context context, float x, float y, float z, float yaw, float pitch, float roll) {
    	mX = x;
    	mY = y;
    	mZ = z;
    	mYaw = yaw;
    	mPitch = pitch;
    	mRoll = roll;
    	
    	updateRotationMatrix();
    	updateTranslationMatrix();
    }
    
    protected void setModel(Renderable msModel) {
    	mModel = msModel;
    	
    	if (mModel instanceof Model) {    		
    		mCollisionRectOffsetX = ((Model)mModel).mCollisionRectSizeX / 2.0f;
    		mCollisionRectOffsetZ = ((Model)mModel).mCollisionRectSizeZ / 2.0f;
    	}
    	
    }
    
    public boolean checkIntersect(float x, float z) {
    	float radPitch = (float) Math.toRadians(mPitch);
    	float modX = (float) (Math.cos(radPitch) * (x - mX) - Math.sin(radPitch) * (z - mZ) + mX);
    	float modZ = (float) (Math.sin(radPitch) * (x - mX) + Math.cos(radPitch) * (z - mZ) + mZ);
    	
    	return (modX > mX - mCollisionRectOffsetX && modX < mX + mCollisionRectOffsetX &&
    			modZ > mZ - mCollisionRectOffsetZ && modZ < mZ + mCollisionRectOffsetZ);
    }
    
    public void draw(float[] ProjectionViewMatrix, BaseObjectShaderSet shader) {      
    	Matrix.multiplyMM(mMVPMatrix, 0, ProjectionViewMatrix, 0, mModelMatrix, 0);
    	
    	shader.unfiormSetMat4(shader.U_MODEL_MATRIX, mModelMatrix);
        shader.unfiormSetMat4(shader.U_MVPMATRIX, mMVPMatrix);
        
        ((Model)mModel).draw(shader);
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
