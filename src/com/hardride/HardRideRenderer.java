/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

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
    
    private Square   mSquare;

    private String 	mVShaderCode;
    private String 	mFShaderCode;
    
    private float 	mStartTime;
    
    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
       
    public HardRideRenderer(String vShaderCode, String fShaderCode) {
    	super();
    	
    	mStartTime = SystemClock.uptimeMillis() / 1000.0f;
    	
    	mVShaderCode = vShaderCode;
    	mFShaderCode = fShaderCode;
    }
    
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.3f, 1.0f);

        mSquare = new Square(mVShaderCode, mFShaderCode);
        mSquare.setZ(5.0f);
    }

    @Override
    public void onDrawFrame(GL10 unused) {

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthMask(true);
        
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
   
        Matrix.setLookAtM(mViewMatrix, 0, 0.0f, 0.0f, -15.0f, 0f, 0f, 1f, 0.0f, 1.0f, 0.0f);
        
        float time = SystemClock.uptimeMillis() / 1000.0f - mStartTime;
        mSquare.setYaw(13.0f * time);
        mSquare.setPitch(110.0f * time);
        
        // Draw square
        mSquare.draw(mViewMatrix, mProjectionMatrix);
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
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        
        if (compileStatus[0] == 0) {
        	// glGetShaderInfoLog() doesn't work :( For possible solution look here: 
        	// http://stackoverflow.com/questions/4588800/glgetshaderinfolog-returns-empty-string-android
        	// and here:
        	// http://stackoverflow.com/questions/24122075/the-import-com-badlogic-cannot-be-resolved-in-java-project-libgdx-setup
        	Log.e("ShaderCompiler", "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
        	GLES20.glDeleteShader(shader);
        	shader = 0;
        	throw new RuntimeException("Error during compiling shader");
        }
        
        return shader;
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
        return mSquare.getX();
    }

    public void setObjectXPos(float newPos) {
        mSquare.setX(newPos);
    }

    public float getObjectYPos() {
        return mSquare.getY();
    }

    public void setObjectYPos(float newPos) {
        mSquare.setY(newPos);
    }
}