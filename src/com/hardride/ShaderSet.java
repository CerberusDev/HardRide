/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.util.Log;

public class ShaderSet {
	
	public final int mID;
	protected String mShaderName;
	
    //protected HashMap<String, Integer> mAttribs = new HashMap<String, Integer>();
    //protected HashMap<String, Integer> mUniforms = new HashMap<String, Integer>();
    
	public ShaderSet(Context context, String shaderName) {
		mShaderName = shaderName;
		String vShaderCode = new String();
		String fShaderCode = new String();
		
		try {
        	AssetManager assetManager = context.getAssets();
        	InputStream ims = assetManager.open("shaders/" + mShaderName + ".vs");
        	vShaderCode = convertStreamToString(ims);
        	ims = assetManager.open("shaders/" + mShaderName + ".fs");
        	fShaderCode = convertStreamToString(ims);
        	ims.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fShaderCode);

        mID = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mID, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mID, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mID);                  // create OpenGL program executables
	}

	public void use() {
		GLES20.glUseProgram(mID);
	}
	
	public void attribEnableAndSetDataFloat(int attribID, int floatsPerVertex, FloatBuffer data) {      
        GLES20.glEnableVertexAttribArray(attribID);
        
        GLES20.glVertexAttribPointer(attribID, floatsPerVertex, GLES20.GL_FLOAT, false, 0, data);
        HardRideRenderer.checkGlError("glVertexAttribPointer");
	}
	
	public void attribDisable(int attribID) {
		GLES20.glDisableVertexAttribArray(attribID);
	}
	
	public void unfiormSetVec4(int uniformID, float[] value) {       
        GLES20.glUniform4fv(uniformID, 1, value, 0);
        HardRideRenderer.checkGlError("glUniform4fv");
	}
	
	public void unfiormSetMat4(int uniformID, float[] value) {       
        GLES20.glUniformMatrix4fv(uniformID, 1, false, value, 0);
        HardRideRenderer.checkGlError("glUniform4fv");
	}
	
	protected int getAttribID(String name) {
		int attribHandle = GLES20.glGetAttribLocation(mID, name);
		verifyAttribHandle(attribHandle, name);
		return attribHandle;
	}
	
	protected int getUniformID(String name) {
        int uniformHandle = GLES20.glGetUniformLocation(mID, name);
        verifyUniformHandle(uniformHandle, name);		
        return uniformHandle;
	}
	
	protected void verifyUniformHandle(int uniformHandler, String uniformName) {
		HardRideRenderer.checkGlError("glGetUniformLocation");
		if (uniformHandler == -1) {
			throw new RuntimeException("Set uniform failed!\n" +
					"Cannot find uniform '" + uniformName + 
					"' in '" + mShaderName + "' shader!");
		}
	}
	
	protected void verifyAttribHandle(int attribHandler, String attribName) {
		HardRideRenderer.checkGlError("glGetAttribLocation");
		if (attribHandler == -1) {
			throw new RuntimeException("Enable attrib failed!\n" +
					"Cannot find attribute '" + attribName + 
					"' in '" + mShaderName + "' shader!");
		}
	}
	
    public int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        
        if (compileStatus[0] == 0) {
        	// glGetShaderInfoLog() doesn't work :( For possible solution look here: 
        	// http://stackoverflow.com/questions/4588800/glgetshaderinfolog-returns-empty-string-android
        	// and here:
        	// http://stackoverflow.com/questions/24122075/the-import-com-badlogic-cannot-be-resolved-in-java-project-libgdx-setup
        	Log.e("ShaderCompiler", "Error compiling shader " + mShaderName + ":\n" + GLES20.glGetShaderInfoLog(shader));
        	GLES20.glDeleteShader(shader);
        	shader = 0;
        	throw new RuntimeException("Error during compiling shader");
        }
        
        return shader;
    }
	
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
