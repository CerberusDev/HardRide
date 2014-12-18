/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride.shaders.base;


import android.content.Context;

public class BaseObjectShaderSet extends ShaderSet {
	
	public final int A_POSITION;		// vec4
	public final int A_NORMAL;			// vec3
	
	public final int U_MVPMATRIX;		// mat4
	public final int U_MODEL_MATRIX;	// mat4
	public final int U_COLOR;			// vec4
	
	public BaseObjectShaderSet(Context context, String shaderName) {
		super(context, shaderName);
		
		A_POSITION = getAttribID("a_Position");
		A_NORMAL = getAttribID("a_Normal");
		
		U_MVPMATRIX = getUniformID("u_MVPMatrix");
		U_MODEL_MATRIX = getUniformID("u_ModelMatrix");
		U_COLOR = getUniformID("u_Color");
	}
}
