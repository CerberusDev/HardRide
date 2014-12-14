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

import com.hardride.actors.CubeActor;
import com.hardride.actors.base.Actor;
import com.hardride.shaders.PhongShaderSet;
import com.hardride.shaders.UnlitShaderSet;

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
    
    private static final float GREEN[] = new float[]{0.0f, 1.0f, 0.0f, 1.0f};
    private static final float WHITE[] = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
    
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
           
        mGreenCube = new CubeActor(mContext);
        mGreenCube.setZ(5.0f);
        
        mActors = new ArrayList<Actor>();

        for (int i = -4; i < 5; i++) {
        	for (int j = -4; j < 5; j++) {
                Actor cube = new CubeActor(mContext);
                cube.setX(i * 30.0f);
                cube.setZ(j * 30.0f + 10.0f);
                
                mActors.add(cube);
        	}
        }
        
        mPhongShader = new PhongShaderSet(mContext);
        mUnlitShader = new UnlitShaderSet(mContext);
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
        GLES20.glEnableVertexAttribArray(mPhongShader.A_POSITION);
        GLES20.glEnableVertexAttribArray(mPhongShader.A_NORMAL);
        
        mPhongShader.unfiormSetVec4(mPhongShader.U_COLOR, GREEN);
        mGreenCube.draw(mViewMatrix, mProjectionMatrix, mPhongShader);
        
        mPhongShader.unfiormSetVec4(mPhongShader.U_COLOR, WHITE);
        for (Actor actor : mActors)
        	actor.draw(mViewMatrix, mProjectionMatrix, mUnlitShader);
        
        GLES20.glDisableVertexAttribArray(mPhongShader.A_POSITION);
        GLES20.glDisableVertexAttribArray(mPhongShader.A_NORMAL);
        
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

    public void rotateViewMatrix(float angle) {
    	Matrix.rotateM(mViewMatrix, 0, angle, 0.0f, 1.0f, 0.0f);
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