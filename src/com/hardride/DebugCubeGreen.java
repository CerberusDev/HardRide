/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride;

import android.content.Context;

public class DebugCubeGreen extends DebugCube {
	
	public DebugCubeGreen(Context context) {
		super(new ShaderSet(context, "basic0"), context, new float[]{0.0f, 1.0f, 0.0f, 1.0f});
	}
}
