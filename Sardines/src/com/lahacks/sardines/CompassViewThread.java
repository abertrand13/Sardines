package com.lahacks.sardines;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

class CompassViewThread extends Thread {
	private static String LOG_TAG = "CompassViewThread";
	
	private SurfaceHolder _surfaceHolder;
	private CompassView compass;
	private boolean _run = false;

	private double dpsAngle = 25;
	private double dpsRange = 8;
	
	private long lastTime = 0;

	public CompassViewThread(SurfaceHolder surfaceHolder, CompassView cv) {
		_surfaceHolder = surfaceHolder;
		compass = cv;
	}
	
	public CompassViewThread(CompassView cv){
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
					if(lastTime > 0){
						int elapsedTime = (int) (System.currentTimeMillis() - lastTime);
						lastTime = System.currentTimeMillis();
						
						double maxChangeAngle = ((double)elapsedTime / 1000.0) * dpsAngle;
						double maxChangeRange = ((double)elapsedTime / 1000.0) * dpsRange;
						
						
						double newAngle = compass.getTargetAngle();
						
						double diffAngle = newAngle - compass.getAngle();
						while(diffAngle < -180) diffAngle += 360;
						while(diffAngle > 180) diffAngle -= 360;
						
						if(Math.abs(diffAngle) > maxChangeAngle){
							if(diffAngle > 0)
								newAngle = compass.getAngle() + maxChangeAngle;
							else
								newAngle = compass.getAngle() - maxChangeAngle;
						}
						
						double newRange = compass.getTargetRange();
						
						if(Math.abs(newRange - compass.getRange()) > maxChangeRange){
							if(newRange - compass.getRange() > 0)
								newRange = compass.getRange() + maxChangeRange;
							else
								newRange = compass.getRange() - maxChangeRange;
						}
						
						compass.setAngle(newAngle, newRange);
						
						// Insert methods to modify positions of items in onDraw()
						compass.postInvalidate();
					}else{
						lastTime = System.currentTimeMillis();
					}
				}
			} finally {
				if (c != null) {
					_surfaceHolder.unlockCanvasAndPost(c);
				}
			}
			
			try{
				Thread.sleep(50);
			}catch(Exception e){
				
			}

		}
	}
}