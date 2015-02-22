/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride.models.base;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class Model extends Renderable {
	
	public float mCollisionRectSizeX;
	public float mCollisionRectSizeZ;
	
    public Model(String modelName, Context context) {
    	loadModelDataFromFile(modelName, context);
    	sendModelDataToGPUBuffers();
    }
	
	protected void loadModelDataFromFile(String modelName, Context context) {
		List<String> strLines = new ArrayList<String>();
		
		try {
        	InputStream ims = context.getAssets().open("models/" + modelName);
        	DataInputStream in = new DataInputStream(ims);
        	BufferedReader br = new BufferedReader(new InputStreamReader(in));
        	String line;
        	while ((line = br.readLine()) != null)
        		strLines.add(line);
            ims.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boolean headerRead = false;
		int verticesAmount;
		
		int vIdx = 0, nIdx = 0, iIdx = 0;
		
		for (int i = 0; i < strLines.size(); i++) {
			String line = strLines.get(i);
			
			if (!line.isEmpty() && !line.startsWith("//")) {
				if (!headerRead) {
					String[] tokens = line.split("[ ]+");
					verticesAmount = Integer.parseInt(tokens[0]);
					mIndicesAmount = Integer.parseInt(tokens[1]);
					
					mVerticesRawData = new float[(FLOATS_PER_POSITION + FLOATS_PER_NORMAL) * verticesAmount];
					mIndicesRawData = new short[mIndicesAmount];
					headerRead = true;
				}
				else if (line.startsWith("v ")) {
					String[] tokens = line.split("[ ]+");
					mVerticesRawData[2 * vIdx] = Float.parseFloat(tokens[1]);
					mVerticesRawData[2 * vIdx + 1] = Float.parseFloat(tokens[2]);
					mVerticesRawData[2 * vIdx + 2] = Float.parseFloat(tokens[3]);
					vIdx += 3;
				}
				else if (line.startsWith("vn ")) {
					String[] tokens = line.split("[ ]+");
					mVerticesRawData[2 * nIdx + 3] = Float.parseFloat(tokens[1]);
					mVerticesRawData[2 * nIdx + 4] = Float.parseFloat(tokens[2]);
					mVerticesRawData[2 * nIdx + 5] = Float.parseFloat(tokens[3]);
					nIdx += 3;
				}
				else if (line.startsWith("f ")) {
					String[] tokens = line.split("[ ]+");
					for (int j = 1; j < tokens.length; j++) {
						mIndicesRawData[iIdx++] = Short.parseShort(tokens[j]);
					}
				}			
			}
		}
	}
}
