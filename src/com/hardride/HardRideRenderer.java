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
import com.hardride.actors.GroundActor;
import com.hardride.actors.VehicleActor;
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
    private static final float GRAY[] = new float[]{0.6f, 0.6f, 0.6f, 1.0f};
    
    private ArrayList<Actor> mActors;
    private VehicleActor mVehicle;
    private GroundActor mGround;
    
    private Context mContext;  
    private HardRideLogic mLogic;
    
    private float mLastFPSUpdateTime = 0.0f;
    private int mFPS = 0;
    private float mLastSecondDrawTime = 0.0f;
    
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mProjectionViewMatrix = new float[16];   
    
    private PhongShaderSet mPhongShader;
    private UnlitShaderSet mUnlitShader;
    
    public HardRideRenderer(Context context, HardRideLogic logic) {
    	super();
    	
    	mContext = context;
    	mLogic = logic;
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
                
        Matrix.setLookAtM(mViewMatrix, 0, 
        		-15.0f, 0.0f, -5.0f, 	// eye XYZ
        		0.0f, 0.0f, -5.0f, 		// center XYZ
        		0.0f, 1.0f, 0.0f);		// up XYZ

        mVehicle = new VehicleActor(mContext);
        mLogic.setVehicle(mVehicle);
        mVehicle.setZ(-5.0f);
        mVehicle.setY(-2.0f);
        
        mActors = new ArrayList<Actor>();

        for (int i = -4; i < 5; i++) {
        	for (int j = -4; j < 5; j++) {
                Actor cube = new CubeActor(mContext);
                cube.setX(i * 30.0f);
                cube.setZ(j * 30.0f + 10.0f);
                
                mActors.add(cube);
        	}
        }
        
        mGround = new GroundActor(mContext);
        mGround.setY(-5.5f);
        
        mPhongShader = new PhongShaderSet(mContext);
        mUnlitShader = new UnlitShaderSet(mContext);
    }

    @Override
    public void onDrawFrame(GL10 unused) {	
    	mLogic.update();
    	
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        float currTime = SystemClock.uptimeMillis();
        
        mPhongShader.use();
        GLES20.glEnableVertexAttribArray(mPhongShader.A_POSITION);
        GLES20.glEnableVertexAttribArray(mPhongShader.A_NORMAL);
        
        mPhongShader.unfiormSetVec4(mPhongShader.U_COLOR, GREEN);
        mVehicle.draw(mProjectionViewMatrix, mPhongShader);
        
        mPhongShader.unfiormSetVec4(mPhongShader.U_COLOR, WHITE);
        for (Actor actor : mActors)
        	actor.draw(mProjectionViewMatrix, mUnlitShader);
        
        mPhongShader.unfiormSetVec4(mPhongShader.U_COLOR, GRAY);
        mGround.draw(mProjectionViewMatrix, mPhongShader);
        
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
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1.0f, 300.0f);
        Matrix.multiplyMM(mProjectionViewMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
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
    
    public void updateViewMatrix(float x, float z, float a, float b) {
        Matrix.setLookAtM(mViewMatrix, 0, 
        		x - 20.0f * a, 10.0f, z - 20.0f * b, 	// eye XYZ
        		x, 5.0f, z, 							// center XYZ
        		0.0f, 1.0f, 0.0f);						// up XYZ
    	Matrix.multiplyMM(mProjectionViewMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
    }
}