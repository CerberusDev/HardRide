/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride;

import android.content.Context;

public class DebugCubeBlue extends DebugCube {
	
	public DebugCubeBlue(Context context) {
		super(new ShaderSet(context, ShaderSet.S_PHONG), context, new float[]{0.0f, 0.0f, 1.0f, 1.0f});
	}
}
