/*
 * Hard Ride
 * 
 * Copyright (C) 2015
 *
 */

package com.hardride.shaders;

import com.hardride.shaders.base.ShaderSet;
import android.content.Context;

public class ParticleShaderSet extends ShaderSet {
	
	public final int A_POSITION;			// vec4
	public final int A_END_TRANSLATION;		// vec3
	
	public final int U_MODEL_MATRIX;		// mat4
	public final int U_VIEW_MATRIX;			// mat4
	public final int U_PROJECTION_MATRIX;	// mat4

	public final int U_LIFETIME;			// float
	public final int U_COLOR;				// vec4
	
	public ParticleShaderSet(Context context) {
		super(context, "particle0");
		
		A_POSITION = getAttribID("a_Position");
		A_END_TRANSLATION = getAttribID("a_EndTranslation");
		
		U_MODEL_MATRIX = getUniformID("u_ModelMatrix");
		U_VIEW_MATRIX = getUniformID("u_ViewMatrix");
		U_PROJECTION_MATRIX = getUniformID("u_ProjectionMatrix");
		
		U_COLOR = getUniformID("u_Color");
		U_LIFETIME = getUniformID("u_Lifetime");
	}
}
