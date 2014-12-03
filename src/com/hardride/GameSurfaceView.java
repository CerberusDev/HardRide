/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class GameSurfaceView extends GLSurfaceView {

    private final HardRideRenderer mRenderer;

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    
    public GameSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        setEGLConfigChooser(true);
        
        AssetManager assetManager = context.getAssets();
        String vShaderCode = new String();
        String fShaderCode = new String();
        
        try {
        	InputStream ims = assetManager.open("shaders/basic0.vs");
        	vShaderCode = convertStreamToString(ims);
        	ims = assetManager.open("shaders/basic0.fs");
        	fShaderCode = convertStreamToString(ims);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new HardRideRenderer(vShaderCode, fShaderCode);
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private float mPreviousX;
    private float mPreviousY;

    @SuppressLint("ClickableViewAccessibility") @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                mRenderer.setObjectXPos(mRenderer.getObjectXPos() - dx * 0.05f);
                mRenderer.setObjectYPos(mRenderer.getObjectYPos() - dy * 0.05f);
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

}
