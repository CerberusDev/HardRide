/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class HardRideRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "HardRideRenderer";
    
    private ArrayList<Actor> mActors;
    private Actor mGreenCube;
    
    private Context mContext;  
    private float 	mStartTime;
    
    private float mLastFPSUpdateTime = 0.0f;
    private int mFPS = 0;
    private float mLastSecondDrawTime = 0.0f;
    
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
       
    private PhongShaderSet mPhongShader;
    private UnlitShaderSet mUnlitShader;
    
    public HardRideRenderer(Context context) {
    	super();
    	
    	mContext = context;
    	mStartTime = SystemClock.uptimeMillis();
    }
    
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.3f, 1.0f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthMask(true);
                
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
                
        Matrix.setLookAtM(mViewMatrix, 0, 0.0f, 0.0f, -15.0f, 0f, 0f, 1f, 0.0f, 1.0f, 0.0f);
           
        mGreenCube = new DebugCubeGreen(mContext);
        mGreenCube.setZ(5.0f);
        
        mActors = new ArrayList<Actor>();

        for (int i = -5; i < 6; i++) {
        	for (int j = -5; j < 6; j++) {
                Actor cube = new WhiteCube(mContext);
                cube.setX(i * 30.0f);
                cube.setZ(j * 30.0f);
                
                mActors.add(cube);
        	}
        }
        
        mPhongShader = new PhongShaderSet(mContext);
        mPhongShader.use();
        mPhongShader.unfiormSetVec4(mPhongShader.U_COLOR, new float[]{0.0f, 1.0f, 0.0f, 1.0f});
        
        mUnlitShader = new UnlitShaderSet(mContext);
        mUnlitShader.use();
        mUnlitShader.unfiormSetVec4(mPhongShader.U_COLOR, new float[]{1.0f, 1.0f, 1.0f, 1.0f});
    }

    @Override
    public void onDrawFrame(GL10 unused) {	
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        float currTime = SystemClock.uptimeMillis();
        float programDuration = currTime - mStartTime;
        mGreenCube.setYaw(0.013f * programDuration);
        mGreenCube.setPitch(0.11f * programDuration);
        
        mPhongShader.use();
        mPhongShader.attribEnable(mPhongShader.A_POSITION);
        mPhongShader.attribEnable(mPhongShader.A_NORMAL);
        
        mGreenCube.draw(mViewMatrix, mProjectionMatrix, mPhongShader);
        
        mPhongShader.attribDisable(mPhongShader.A_POSITION);
        mPhongShader.attribDisable(mPhongShader.A_NORMAL);
        
        mUnlitShader.use();
        mUnlitShader.attribEnable(mUnlitShader.A_POSITION);
        mUnlitShader.attribEnable(mUnlitShader.A_NORMAL);
        
        for (Actor actor : mActors)
        	actor.draw(mViewMatrix, mProjectionMatrix, mUnlitShader);
        
        mUnlitShader.attribDisable(mUnlitShader.A_POSITION);
        mUnlitShader.attribDisable(mUnlitShader.A_NORMAL);
        
        mLastSecondDrawTime += SystemClock.uptimeMillis() - currTime;
       
        if (currTime > mLastFPSUpdateTime + 1000.0f) {
        	mLastFPSUpdateTime = currTime;
        	float avgDrawTime = mLastSecondDrawTime / mFPS;
        	Log.i(TAG, "FPS: " + mFPS + "    avg Draw: " + avgDrawTime + "ms");
        	mFPS = 0;
        	mLastSecondDrawTime = 0.0f;
        }
        mFPS++;
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1.0f, 100.0f);

    }

    /**
    * Utility method for debugging OpenGL calls. Provide the name of the call
    * just after making it:
    *
    * <pre>
    * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    * HardRideRenderer.checkGlError("glGetUniformLocation");</pre>
    *
    * If the operation is not successful, the check throws an error.
    *
    * @param glOperation - Name of the OpenGL call to check.
    */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

	public float getObjectXPos() {
        return mGreenCube.getX();
    }

    public void setObjectXPos(float newPos) {
    	mGreenCube.setX(newPos);
    }

    public float getObjectYPos() {
        return mGreenCube.getY();
    }

    public void setObjectYPos(float newPos) {
    	mGreenCube.setY(newPos);
    }
}