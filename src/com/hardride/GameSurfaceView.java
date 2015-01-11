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
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class GameSurfaceView extends GLSurfaceView {

    private final HardRideRenderer mRenderer;
    private final HardRideLogic mLogic;

    private InputType mInput[] = new InputType[2];
    
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
        
        mInput[0] = InputType.NONE;
        mInput[1] = InputType.NONE;
        
        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @SuppressLint("ClickableViewAccessibility") @Override
    public boolean onTouchEvent(MotionEvent e) {
    	int action = MotionEventCompat.getActionMasked(e);
    	int idx = e.getActionIndex();
    	
    	if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
    		mLogic.tapPress();
    		if (MotionEventCompat.getX(e, idx) > 427.0f) {    			
    			mInput[idx] = InputType.RIGHT;
    			mLogic.decInputState(InputType.NONE);
    			mLogic.incInputState(InputType.RIGHT);
    		} else {
    			mInput[idx] = InputType.LEFT;
    			mLogic.decInputState(InputType.NONE);
    			mLogic.incInputState(InputType.LEFT);
    		}
    	} else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
    		mLogic.tapRelease();
    		if (e.getPointerCount() == 1) {
    			if (mInput[0] != InputType.NONE) {
    				idx = 0;
    			} else {
    				idx = 1;
    			}
    		}
    		mLogic.decInputState(mInput[idx]);
    		mInput[idx] = InputType.NONE;
    		mLogic.incInputState(InputType.NONE);
    	}
    	
    	//if (action != MotionEvent.ACTION_MOVE) {
    	//	Log.d("Input", "ID: " + e.getActionIndex() + "  The action is " + actionToString(action)); 	    
    	//	Log.d("Input", "Input state: " + mInput[0] + "  " + mInput[1]);
    	//}
    	
    	return true;
    }

    public static String actionToString(int action) {
        switch (action) {
                    
            case MotionEvent.ACTION_DOWN: return "Down";
            case MotionEvent.ACTION_MOVE: return "Move";
            case MotionEvent.ACTION_POINTER_DOWN: return "Pointer Down";
            case MotionEvent.ACTION_UP: return "Up";
            case MotionEvent.ACTION_POINTER_UP: return "Pointer Up";
            case MotionEvent.ACTION_OUTSIDE: return "Outside";
            case MotionEvent.ACTION_CANCEL: return "Cancel";
        }
        return "";
    }
    
}
