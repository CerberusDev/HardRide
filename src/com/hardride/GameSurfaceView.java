/*
 * Hard Ride
 * 
 * Copyright (C) 2014
 *
 */

package com.hardride;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class GameSurfaceView extends GLSurfaceView {

    private final HardRideRenderer mRenderer;
    private final HardRideLogic mLogic;

    public GameSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        setEGLConfigChooser(true);
         
        mLogic = new HardRideLogic();
        
        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new HardRideRenderer(context, mLogic);
        setRenderer(mRenderer);
        
        mLogic.setRenderer(mRenderer);
        
        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @SuppressLint("ClickableViewAccessibility") @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	if (x > 427.0f) {
            		mLogic.setInput(true, 0);
            	} else {
            		mLogic.setInput(true, 1);
            	}
            	break;
            case MotionEvent.ACTION_UP:
            	if (x > 427.0f) {
            		mLogic.setInput(false, 0);
            	} else {
            		mLogic.setInput(false, 1);
            	}
            	break;
        }

        return true;
    }

}
