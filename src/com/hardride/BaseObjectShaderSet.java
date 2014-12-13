/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride;

import android.content.Context;

public class BaseObjectShaderSet extends ShaderSet {
	
	public final int A_POSITION;		// vec4
	public final int A_NORMAL;			// vec3
	
	public final int U_MVPMATRIX;		// mat4
	public final int U_MVMATRIX;		// mat4
	public final int U_COLOR;			// vec4
	
	public BaseObjectShaderSet(Context context, String shaderName) {
		super(context, shaderName);
		
		A_POSITION = getAttribID("a_Position");
		A_NORMAL = getAttribID("a_Normal");
		
		U_MVPMATRIX = getUniformID("u_MVPMatrix");
		U_MVMATRIX = getUniformID("u_MVMatrix");
		U_COLOR = getUniformID("u_Color");
	}
}
