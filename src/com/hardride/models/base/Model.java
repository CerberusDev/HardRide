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

import com.hardride.actors.base.Actor;
import com.hardride.shaders.base.BaseObjectShaderSet;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

public class Model extends Renderable 
{
	public float mCollisionRectSizeX;
	public float mCollisionRectSizeZ;
	
    public Model(String modelName, Context context) 
    {
    	if(modelName.length() != 0)
    	{
    		loadModelDataFromFile(modelName, context);
    		sendModelDataToGPUBuffers();    		
    	}
    }
	
	public void draw(BaseObjectShaderSet shader) 
	{
        final int stride = (FLOATS_PER_POSITION + FLOATS_PER_NORMAL) * Actor.BYTES_PER_FLOAT;
        
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVBO[0]);
        GLES20.glVertexAttribPointer(shader.A_POSITION, FLOATS_PER_POSITION, GLES20.GL_FLOAT, false, stride, 0);
        GLES20.glVertexAttribPointer(shader.A_NORMAL, FLOATS_PER_NORMAL, GLES20.GL_FLOAT, false, stride, 
        		FLOATS_PER_POSITION * Actor.BYTES_PER_FLOAT);
        
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, mIBO[0]);
        
        // Draw the model
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndicesAmount, GLES20.GL_UNSIGNED_SHORT, 0);
    }
    
	protected void loadModelDataFromFile(String modelName, Context context) 
	{
		String extension = modelName.substring(modelName.indexOf("."));
		
		if (extension.equals(".obj"))
		{
			ObjModelLoader(modelName, context);
		}
		else
		{
			CustomModelLoader(modelName, context);
		}
	}
	
	protected void ObjModelLoader(String modelName, Context context) 
	{
		List<String> strLines = new ArrayList<String>();
		int verticlesCount = 0;
		int facesCount = 0;
		String line = "";
		boolean bSmooth = false;

		try 
		{
        	InputStream ims = context.getAssets().open("models/" + modelName);
        	DataInputStream in = new DataInputStream(ims);
        	BufferedReader br = new BufferedReader(new InputStreamReader(in));
 
        	while ((line = br.readLine()) != null)
        	{
        		strLines.add(line);
        		
        		if (line.startsWith("v "))
        		{
        			++verticlesCount;
        		}
        		else if (line.startsWith("f ")) 
        		{
        			++facesCount;
        		}
        		else if (line.startsWith("s "))
        		{
        			bSmooth = Integer.parseInt(line.substring(2)) != 0;
        		}
        	}
        	
            ims.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
				
		if (bSmooth)
		{
			int vIdx = 0, nIdx = 0, iIdx = 0;
			String[] LineParts;
			String[] FaceParts;
			
			mVerticesRawData = new float[(FLOATS_PER_POSITION + FLOATS_PER_NORMAL) * verticlesCount];
			mIndicesAmount = facesCount * 3;
			mIndicesRawData = new short[mIndicesAmount];
			
			for (int i = 0; i < strLines.size(); ++i) 
			{
				line = strLines.get(i);
				
				if (!line.isEmpty() && !line.startsWith("#")) 
				{
					if (line.startsWith("v ")) 
					{
						LineParts = line.split("\\s");
						mVerticesRawData[2 * vIdx] = Float.parseFloat(LineParts[1])*2;
						mVerticesRawData[2 * vIdx + 1] = Float.parseFloat(LineParts[2])*2;
						mVerticesRawData[2 * vIdx + 2] = Float.parseFloat(LineParts[3])*2;
						vIdx += 3;
					}
					else if (line.startsWith("vn ")) 
					{
						LineParts = line.split("\\s");
						mVerticesRawData[2 * nIdx + 3] = Float.parseFloat(LineParts[1]);
						mVerticesRawData[2 * nIdx + 4] = Float.parseFloat(LineParts[2]);
						mVerticesRawData[2 * nIdx + 5] = Float.parseFloat(LineParts[3]);
						nIdx += 3;
					}
					else if (line.startsWith("f ")) 
					{
						LineParts = line.split("\\s");
						for (int j = 1; j < LineParts.length; j++) 
						{
							FaceParts = LineParts[j].split("\\/");
							
							mIndicesRawData[iIdx++] = (short) (Short.parseShort(FaceParts[0]) - 1);
							// take care of other face indicies (tx and n) here:
						}
					}
				}
			}
		}
		else
		{
			Log.e("ModelLoader", "Flat normal mode not supported, ...yet.");
			throw new RuntimeException("Flat normal mode not supported, ...yet.");			
		}
	}
	
	protected void CustomModelLoader(String modelName, Context context)
	{
		List<String> strLines = new ArrayList<String>();
		
		try 
		{
        	InputStream ims = context.getAssets().open("models/" + modelName);
        	DataInputStream in = new DataInputStream(ims);
        	BufferedReader br = new BufferedReader(new InputStreamReader(in));
        	String line; 
        	while ((line = br.readLine()) != null)
        		strLines.add(line);
            ims.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		boolean headerRead = false;
		int verticesAmount;
		
		int vIdx = 0, nIdx = 0, iIdx = 0;
		
		for (int i = 0; i < strLines.size(); i++) 
		{
			String line = strLines.get(i);
			
			if (!line.isEmpty() && !line.startsWith("//")) 
			{
				if (!headerRead) 
				{
					String[] tokens = line.split("\\s");
					verticesAmount = Integer.parseInt(tokens[0]);
					mIndicesAmount = Integer.parseInt(tokens[1]);
					
					mVerticesRawData = new float[(FLOATS_PER_POSITION + FLOATS_PER_NORMAL) * verticesAmount];
					mIndicesRawData = new short[mIndicesAmount];
					headerRead = true;
				}
				else if (line.startsWith("v ")) 
				{
					String[] tokens = line.split("\\s");
					mVerticesRawData[2 * vIdx] = Float.parseFloat(tokens[1]);
					mVerticesRawData[2 * vIdx + 1] = Float.parseFloat(tokens[2]);
					mVerticesRawData[2 * vIdx + 2] = Float.parseFloat(tokens[3]);
					vIdx += 3;
				}
				else if (line.startsWith("vn ")) 
				{
					String[] tokens = line.split("\\s");
					mVerticesRawData[2 * nIdx + 3] = Float.parseFloat(tokens[1]);
					mVerticesRawData[2 * nIdx + 4] = Float.parseFloat(tokens[2]);
					mVerticesRawData[2 * nIdx + 5] = Float.parseFloat(tokens[3]);
					nIdx += 3;
				}
				else if (line.startsWith("f ")) 
				{
					String[] tokens = line.split("\\s");
					for (int j = 1; j < tokens.length; j++) 
					{
						mIndicesRawData[iIdx++] = Short.parseShort(tokens[j]);
					}
				}			
			}
		}
	} // end protected void CustomModelLoader(String modelName, Context context)
}
