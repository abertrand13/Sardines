package com.lahacks.sardines;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CompassView extends SurfaceView implements SurfaceHolder.Callback {

	private final static String LOG_TAG = "CompassView";
	
	private Context context;
	private CompassViewThread thread;
	
	private double angle = 350;
	private double arcRange = 10;
	private double targetAngle = 190;
	private double targetArcRange = 60;

	public CompassView(Context context) {
		super(context);
		this.context = context;
		setup();

	}

	public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setup();
	}

	public CompassView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		setup();
	}

	private void setup() {
		getHolder().addCallback(this);
	}

	public void setAngle(double a, double range){
		angle = a;
		arcRange = range;
	}
	
	public void setAngleTarget(double a, double range){
		targetAngle = a;
		targetArcRange = range;
	}
	
	public double getAngle() {
		return angle;
	}

	public double getRange() {
		return arcRange;
	}

	public double getTargetAngle() {
		return targetAngle;
	}

	public double getTargetRange() {
		return targetArcRange;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		draw(canvas, (int)angle, (int)arcRange);
	}

	private void draw(Canvas canvas, int angle, int arcRange) {
		Log.v(LOG_TAG, "ANG: " + angle + " \tRNG: " + arcRange);
		int x = getWidth();
		int y = getHeight();
		int r = Math.min(x, y);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.argb(150, (int)((double)arcRange * (255.0/90.0)), 150, 150));
		int drawAngle = angle - (arcRange / 2);
		if (drawAngle < 0)
			drawAngle += 360;
		canvas.drawArc(new RectF(0, 0, r, r), drawAngle, arcRange, true, paint);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		setWillNotDraw(false); // Allows us to use invalidate() to call onDraw()

		thread = new CompassViewThread(holder, this); // Start the thread that
		thread.setRunning(true); // will make calls to
		thread.start(); // onDraw()
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		try {
			thread.setRunning(false); // Tells thread to stop
			thread.join(); // Removes thread from mem.
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
}
