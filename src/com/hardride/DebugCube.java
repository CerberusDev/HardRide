/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride;

import android.content.Context;

public class DebugCube extends Actor {

	public DebugCube(ShaderSet shaderProgram, Context context) {
		super(shaderProgram, "cube.raw", context);
	}
}
