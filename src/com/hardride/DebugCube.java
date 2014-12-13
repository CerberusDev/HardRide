/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride;

import android.content.Context;

public class DebugCube extends Actor {
	
	public DebugCube(BaseObjectShaderSet shaderProgram, Context context, float[] color) {
		super(shaderProgram, "cube.raw", context, color);
	}

}
