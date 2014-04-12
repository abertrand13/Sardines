package com.lahacks.sardines;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

class CompassViewThread extends Thread {
	private SurfaceHolder _surfaceHolder;
	private CompassView compass;
	private boolean _run = false;

	private double dpsAngle = 10;
	private double dpsRange = 10;
	
	private long lastTime = 0;

	public CompassViewThread(SurfaceHolder surfaceHolder, CompassView cv) {
		_surfaceHolder = surfaceHolder;
		compass = cv;
	}

	public void setRunning(boolean run) { // Allow us to stop the thread
		_run = run;
	}

	@Override
	public void run() {
		Canvas c;
		while (_run) { // When setRunning(false) occurs, _run is
			c = null; // set to false and loop ends, stopping thread

			try {

				c = _surfaceHolder.lockCanvas(null);
				synchronized (_surfaceHolder) {
					int elapsedTime = (int) (System.currentTimeMillis() - lastTime);
					lastTime = System.currentTimeMillis();
					
					double maxChangeAngle = ((double)elapsedTime / 1000.0) * dpsAngle;
					double maxChangeRange = ((double)elapsedTime / 1000.0) * dpsRange;
					
					double newRange = compass.getTargetRange();
					double newAngle = compass.getTargetAngle();
					
					if(newAngle - compass.getAngle() > maxChangeAngle){
						newAngle = compass.getAngle() + maxChangeAngle;
					}
					
					compass.setAngle(newAngle, newRange);
					
					// Insert methods to modify positions of items in onDraw()
					compass.postInvalidate();

				}
			} finally {
				if (c != null) {
					_surfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
	}
}