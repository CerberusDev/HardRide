/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride;

import android.content.Context;

public class WhiteCube extends DebugCube {
	
	public WhiteCube(Context context) {
		super(new ShaderSet(context, ShaderSet.S_GOURAUD), context, new float[]{1.0f, 1.0f, 1.0f, 1.0f});
	}
}
