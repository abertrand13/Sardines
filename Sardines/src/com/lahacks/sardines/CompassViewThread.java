package com.lahacks.sardines;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

class CompassViewThread extends Thread {
        private SurfaceHolder _surfaceHolder;
        private CompassView _compass;
        private boolean _run = false;


        public CompassViewThread(SurfaceHolder surfaceHolder, CompassView cv) {
            _surfaceHolder = surfaceHolder;
            _compass = cv;
        }


        public void setRunning(boolean run) { //Allow us to stop the thread
            _run = run;
        }


        @Override
        public void run() {
            Canvas c;
            while (_run) {     //When setRunning(false) occurs, _run is 
                c = null;      //set to false and loop ends, stopping thread


                try {


                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {


                     //Insert methods to modify positions of items in onDraw()
                     _compass.postInvalidate();


                    }
  } finally {
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }