/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride.actors.base;

import com.hardride.models.base.Model;
import com.hardride.shaders.base.BaseObjectShaderSet;

import android.content.Context;
import android.opengl.Matrix;

public class Actor {
	
	protected Model mModel;
	
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
	
	protected float mCollisionRectSizeX;
	protected float mCollisionRectSizeZ;
	
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
    
    protected void setModel(Model model) {
    	mModel = model;
    	
    	mCollisionRectSizeX = mModel.mCollisionRectSizeX;
    	mCollisionRectSizeZ = mModel.mCollisionRectSizeZ;
    }
    
    public boolean checkIntersect(float x, float z) {
    	if (x > mX - mCollisionRectSizeX && x < mX + mCollisionRectSizeX &&
    		z > mZ - mCollisionRectSizeZ && z < mZ + mCollisionRectSizeZ)
    		return true;
    	
    	return false;
    }
    
    public void draw(float[] ProjectionViewMatrix, BaseObjectShaderSet shader) {      
    	Matrix.multiplyMM(mMVPMatrix, 0, ProjectionViewMatrix, 0, mModelMatrix, 0);
    	
    	shader.unfiormSetMat4(shader.U_MODEL_MATRIX, mModelMatrix);
        shader.unfiormSetMat4(shader.U_MVPMATRIX, mMVPMatrix);
        
        mModel.draw(shader);
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
