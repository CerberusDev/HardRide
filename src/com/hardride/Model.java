/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride;

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

import android.content.Context;
import android.opengl.GLES20;

public class Model {
	
	protected int mVBO[] = new int[1];
	protected int mIBO[] = new int[1];
	
	protected int FLOATS_PER_POSITION;
	protected int FLOATS_PER_NORMAL;
	protected float mVertices[];
	protected float mNormals[];
	protected short mIndices[];
	protected int mIndicesAmount;
	
    public Model(String modelName, Context context) {
    	loadModel(modelName, context);
    	
        ByteBuffer dlb = ByteBuffer.allocateDirect(mIndices.length * Actor.BYTES_PER_SHORT);
        dlb.order(ByteOrder.nativeOrder());
        ShortBuffer indexBuffer = dlb.asShortBuffer();
        indexBuffer.put(mIndices);
        indexBuffer.position(0);
        
        float [] verticesData = new float[mVertices.length * 2];
        for (int i = 0; i < mVertices.length; i += 3) {
        	verticesData[2*i] = mVertices[i];
        	verticesData[2*i+1] = mVertices[i+1];
        	verticesData[2*i+2] = mVertices[i+2];
        	verticesData[2*i+3] = mNormals[i];
        	verticesData[2*i+4] = mNormals[i+1];
        	verticesData[2*i+5] = mNormals[i+2];
        }
        
        ByteBuffer vbb = ByteBuffer.allocateDirect(verticesData.length * Actor.BYTES_PER_FLOAT);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer newVertexBuffer = vbb.asFloatBuffer();
        newVertexBuffer.put(verticesData);
        newVertexBuffer.position(0);
        
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
    
	protected void loadModel(String modelName, Context context) {
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
		int indicesAmount;
		
		int vIdx = 0, nIdx = 0, iIdx = 0;
		
		for (int i = 0; i < strLines.size(); i++) {
			String line = strLines.get(i);
			
			if (!line.isEmpty() && !line.startsWith("//")) {
				if (!headerRead) {
					String[] tokens = line.split("[ ]+");
					verticesAmount = Integer.parseInt(tokens[0]);
					FLOATS_PER_POSITION = Integer.parseInt(tokens[1]);
					FLOATS_PER_NORMAL = FLOATS_PER_POSITION;
					indicesAmount = Integer.parseInt(tokens[2]);
					
					mVertices = new float[FLOATS_PER_POSITION * verticesAmount];
					mNormals = new float[FLOATS_PER_NORMAL * verticesAmount];
					mIndices = new short[indicesAmount];
					headerRead = true;
				}
				else if (line.startsWith("v ")) {
					String[] tokens = line.split("[ ]+");
					mVertices[vIdx] = Float.parseFloat(tokens[1]);
					mVertices[vIdx + 1] = Float.parseFloat(tokens[2]);
					mVertices[vIdx + 2] = Float.parseFloat(tokens[3]);
					vIdx += 3;
				}
				else if (line.startsWith("vn ")) {
					String[] tokens = line.split("[ ]+");
					mNormals[nIdx] = Float.parseFloat(tokens[1]);
					mNormals[nIdx + 1] = Float.parseFloat(tokens[2]);
					mNormals[nIdx + 2] = Float.parseFloat(tokens[3]);
					nIdx += 3;
				}
				else if (line.startsWith("f ")) {
					String[] tokens = line.split("[ ]+");
					for (int j = 1; j < tokens.length; j++) {
						mIndices[iIdx++] = Short.parseShort(tokens[j]);
					}
				}			
			}
		}
		
		mIndicesAmount = mIndices.length;
	}
}
