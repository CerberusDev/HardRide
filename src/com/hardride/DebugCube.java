/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride;

import android.content.Context;

public class DebugCube extends Actor {

	protected float[] mColor;
	
	public DebugCube(ShaderSet shaderProgram, Context context, float[] color) {
		super(shaderProgram, "cube.raw", context);
		
		mColor = color;
		
		mShader.use();
		mShader.unfiormSetVec4("u_Color", mColor);  	
	}
}
