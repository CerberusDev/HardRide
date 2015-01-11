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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import com.hardride.actors.base.Actor;
import com.hardride.shaders.base.BaseObjectShaderSet;

import android.content.Context;
import android.opengl.GLES20;

public class Model {
	
	protected int mVBO[] = new int[1];
	protected int mIBO[] = new int[1];
	
	protected int FLOATS_PER_POSITION = 3;
	protected int FLOATS_PER_NORMAL = 3;
	
	protected float mVerticesRawData[];
	protected short mIndicesRawData[];
	protected int mIndicesAmount;
	
	public float mCollisionRectSizeX;
	public float mCollisionRectSizeZ;
	
    public Model(String modelName, Context context) {
    	loadModelDataFromFile(modelName, context);
    	sendModelDataToGPUBuffers();
    }

	public void draw(BaseObjectShaderSet shader) {
        final int stride = (FLOATS_PER_POSITION + FLOATS_PER_NORMAL) * Actor.BYTES_PER_FLOAT;
        
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVBO[0]);
        GLES20.glVertexAttribPointer(shader.A_POSITION, FLOATS_PER_POSITION, GLES20.GL_FLOAT, false, stride, 0);
        GLES20.glVertexAttribPointer(shader.A_NORMAL, FLOATS_PER_NORMAL, GLES20.GL_FLOAT, false, stride, 
        		FLOATS_PER_POSITION * Actor.BYTES_PER_FLOAT);
        
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, mIBO[0]);
        
        // Draw the model
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndicesAmount, GLES20.GL_UNSIGNED_SHORT, 0);
    }
    
	protected void sendModelDataToGPUBuffers() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(mVerticesRawData.length * Actor.BYTES_PER_FLOAT);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer newVertexBuffer = vbb.asFloatBuffer();
        newVertexBuffer.put(mVerticesRawData);
        newVertexBuffer.position(0);
        
        ByteBuffer ibb = ByteBuffer.allocateDirect(mIndicesRawData.length * Actor.BYTES_PER_SHORT);
        ibb.order(ByteOrder.nativeOrder());
        ShortBuffer indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(mIndicesRawData);
        indexBuffer.position(0);
        
        GLES20.glGenBuffers(1, mVBO, 0);
        GLES20.glGenBuffers(1, mIBO, 0);
               
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVBO[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, newVertexBuffer.capacity() * Actor.BYTES_PER_FLOAT,
        		newVertexBuffer, GLES20.GL_STATIC_DRAW);       

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, mIBO[0]);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.capacity() * Actor.BYTES_PER_SHORT, 
        		indexBuffer, GLES20.GL_STATIC_DRAW);
        
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);		
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
