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
import com.hardride.actors.Mesh1Actor;
import com.hardride.actors.Mesh2Actor;
import com.hardride.actors.VehicleActor;
import com.hardride.actors.base.Actor;
import com.hardride.actors.base.ParticleEmitter;
import com.hardride.shaders.ParticleShaderSet;
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
    private static final float RED[] = new float[]{1.0f, 0.0f, 0.0f, 1.0f};
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
    private ParticleShaderSet mParticleShader;
    
    private ParticleEmitter mPE;
    
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

        mVehicle = new VehicleActor(mContext, -70.0f, -2.0f, -70.0f, 0.0f, 0.0f, 0.0f);
        mLogic.setVehicle(mVehicle);
        updateViewMatrix(mVehicle.getX(), mVehicle.getZ(), 0.0f, 1.0f);
        
        initLevel();
        
        mPhongShader = new PhongShaderSet(mContext);
        mUnlitShader = new UnlitShaderSet(mContext);
        mParticleShader = new ParticleShaderSet(mContext);
    }

    private void initLevel() {    	
    	mActors = new ArrayList<Actor>();
    	
    	//mActors.add(new ParticleEmitter(mContext, -70.0f, -2.0f, -70.0f, 0.0f, 0.0f, 0.0f));
    	mPE = new ParticleEmitter(mContext, -70.0f, 0.0f, 10.0f, 0.0f, 0.0f, 0.0f);
    	
    	mActors.add(new Mesh2Actor(mContext, -70.0f, 0.0f, 10.0f, 0.0f, 0.0f, 0.0f));
    	
    	mActors.add(new Mesh1Actor(mContext, -25.0f, 0.0f, -90.0f, 0.0f, 0.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -25.0f, 0.0f, -10.0f, 0.0f, 0.0f, 0.0f));
    	
    	mActors.add(new Mesh1Actor(mContext, -55.0f, 0.0f, -80.0f, 0.0f, 0.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -55.0f, 0.0f, -60.0f, 0.0f, 0.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -55.0f, 0.0f, -40.0f, 0.0f, 0.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -55.0f, 0.0f, -20.0f, 0.0f, 0.0f, 0.0f));
    	
    	mActors.add(new Mesh1Actor(mContext, -85.0f, 0.0f, -90.0f, 0.0f, 0.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -85.0f, 0.0f, -70.0f, 0.0f, 0.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -85.0f, 0.0f, -50.0f, 0.0f, 0.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -85.0f, 0.0f, -30.0f, 0.0f, 0.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -85.0f, 0.0f, -10.0f, 0.0f, 0.0f, 0.0f));
    	
    	// top curve
    	mActors.add(new Mesh1Actor(mContext, -29.0f, 0.0f, 5.0f, 0.0f, 150.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -40.0f, 0.0f, 16.0f, 0.0f, 120.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -55.0f, 0.0f, 20.0f, 0.0f, 90.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -70.0f, 0.0f, 16.0f, 0.0f, 60.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -81.0f, 0.0f, 5.0f, 0.0f, 30.0f, 0.0f));
    	 
    	// bottom curve
    	mActors.add(new Mesh1Actor(mContext, -81.0f, 0.0f, -105.0f, 0.0f, 330.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -70.0f, 0.0f, -116.0f, 0.0f, 300.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -55.0f, 0.0f, -120.0f, 0.0f, 270.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -40.0f, 0.0f, -116.0f, 0.0f, 240.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -29.0f, 0.0f, -105.0f, 0.0f, 210.0f, 0.0f));
    	
    	// T-junction
    	mActors.add(new Mesh1Actor(mContext, -50.0f, 0.0f, -34.0f, 0.0f, 330.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -39.0f, 0.0f, -45.0f, 0.0f, 300.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -24.0f, 0.0f, -50.0f, 0.0f, 90.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -39.0f, 0.0f, -55.0f, 0.0f, 60.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -50.0f, 0.0f, -66.0f, 0.0f, 30.0f, 0.0f));
    	
    	mActors.add(new Mesh1Actor(mContext, -16.0f, 0.0f, -80.0f, 0.0f, 90.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -5.0f, 0.0f, -50.0f, 0.0f, 90.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, -16.0f, 0.0f, -20.0f, 0.0f, 90.0f, 0.0f));
    	
    	// left curve
    	mActors.add(new Mesh1Actor(mContext, 4.0f, 0.0f, -80.0f, 0.0f, 270.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, 19.0f, 0.0f, -76.0f, 0.0f, 240.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, 30.0f, 0.0f, -65.0f, 0.0f, 210.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, 34.0f, 0.0f, -50.0f, 0.0f, 180.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, 30.0f, 0.0f, -35.0f, 0.0f, 150.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, 19.0f, 0.0f, -24.0f, 0.0f, 120.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, 4.0f, 0.0f, -20.0f, 0.0f, 90.0f, 0.0f));
    	
    	// circle
    	mActors.add(new CubeActor(mContext, 100.0f, 0.0f, 100.0f, 0.0f, 0.0f, 0.0f));
    	/*
    	mActors.add(new Mesh1Actor(mContext, 130.0f, 0.0f, 100.0f, 0.0f, 180.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, 126.0f, 0.0f, 115.0f, 0.0f, 150.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, 115.0f, 0.0f, 126.0f, 0.0f, 120.0f, 0.0f));
    	
    	mActors.add(new Mesh1Actor(mContext, 100.0f, 0.0f, 130.0f, 0.0f, 90.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, 85.0f, 0.0f, 126.0f, 0.0f, 60.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, 74.0f, 0.0f, 115.0f, 0.0f, 30.0f, 0.0f));
    	
    	mActors.add(new Mesh1Actor(mContext, 70.0f, 0.0f, 100.0f, 0.0f, 0.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, 74.0f, 0.0f, 85.0f, 0.0f, 330.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, 85.0f, 0.0f, 74.0f, 0.0f, 300.0f, 0.0f));
   
    	mActors.add(new Mesh1Actor(mContext, 100.0f, 0.0f, 70.0f, 0.0f, 270.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, 115.0f, 0.0f, 74.0f, 0.0f, 240.0f, 0.0f));
    	mActors.add(new Mesh1Actor(mContext, 126.0f, 0.0f, 85.0f, 0.0f, 210.0f, 0.0f));
    	*/
    	
    	mGround = new GroundActor(mContext);
    	mGround.setY(-5.5f);
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
        	actor.draw(mProjectionViewMatrix, mPhongShader);
        
        GLES20.glDisableVertexAttribArray(mPhongShader.A_POSITION);
        GLES20.glDisableVertexAttribArray(mPhongShader.A_NORMAL);
        
        mUnlitShader.use();
        GLES20.glEnableVertexAttribArray(mUnlitShader.A_POSITION);
        GLES20.glEnableVertexAttribArray(mUnlitShader.A_NORMAL);
        
        mUnlitShader.unfiormSetVec4(mUnlitShader.U_COLOR, GRAY);
        mGround.draw(mProjectionViewMatrix, mUnlitShader);
        
        GLES20.glDisableVertexAttribArray(mUnlitShader.A_POSITION);
        GLES20.glDisableVertexAttribArray(mUnlitShader.A_NORMAL);
        
        mParticleShader.use();
        GLES20.glEnableVertexAttribArray(mParticleShader.A_POSITION);
        GLES20.glEnableVertexAttribArray(mParticleShader.A_NORMAL);
        
        mParticleShader.unfiormSetVec4(mParticleShader.U_COLOR, RED);
        mPE.drawParticle(mProjectionMatrix, mViewMatrix, mParticleShader);
        
        GLES20.glDisableVertexAttribArray(mParticleShader.A_POSITION);
        GLES20.glDisableVertexAttribArray(mParticleShader.A_NORMAL);
        
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
    
    public ArrayList<Actor> getActors() {return mActors;}
}