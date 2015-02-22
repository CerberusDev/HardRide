/*
 * Hard Ride
 * 
 * Copyright (C) 2015
 *
 */

package com.hardride.models.base;

public class Particle extends Renderable {

	public Particle() {
		int verticesAmount = 4;
		mIndicesAmount = 6;
		
		mVerticesRawData = new float[(FLOATS_PER_POSITION + FLOATS_PER_NORMAL) * verticesAmount];
		mIndicesRawData = new short[mIndicesAmount];
		
		mVerticesRawData[0] = -2.0f;
		mVerticesRawData[1] = 2.0f;
		mVerticesRawData[2] = 0.0f;
		mVerticesRawData[3] = -1.0f;
		mVerticesRawData[4] = 1.0f;
		mVerticesRawData[5] = -1.0f;
				
		mVerticesRawData[6] = -2.0f;
		mVerticesRawData[7] = -2.0f;
		mVerticesRawData[8] = 0.0f;
		mVerticesRawData[9] = -1.0f;
		mVerticesRawData[10] = -1.0f;
		mVerticesRawData[11] = -1.0f;
		
		mVerticesRawData[12] = 2.0f;
		mVerticesRawData[13] = -2.0f;
		mVerticesRawData[14] = 0.0f;
		mVerticesRawData[15] = 1.0f;
		mVerticesRawData[16] =- 1.0f;
		mVerticesRawData[17] = -1.0f;
		
		mVerticesRawData[18] = 2.0f;
		mVerticesRawData[19] = 2.0f;
		mVerticesRawData[20] = 0.0f;
		mVerticesRawData[21] = 1.0f;
		mVerticesRawData[22] = 1.0f;
		mVerticesRawData[23] = -1.0f;
				
		mIndicesRawData[0] = 0;
		mIndicesRawData[1] = 2;
		mIndicesRawData[2] = 1;
		mIndicesRawData[3] = 0;
		mIndicesRawData[4] = 3;
		mIndicesRawData[5] = 2;
		
		sendModelDataToGPUBuffers();
	}
}
