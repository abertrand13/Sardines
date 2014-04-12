package com.lahacks.sardines;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CompassView extends View {

	Context context;

	public CompassView(Context context) {
		super(context);
		this.context = context;

	}

	public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public CompassView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int angle = 90;
		int arcRange = 45;
		draw(canvas, angle, arcRange);
	}

	private void draw(Canvas canvas, int angle, int arcRange){
		int x = getWidth();
		int y = getHeight();
		int r = Math.min(x, y);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.WHITE);
		int drawAngle = angle - (arcRange/2);
		if(drawAngle < 0) drawAngle += 360;
		canvas.drawArc(new RectF(0, 0, r, r), drawAngle, arcRange, true, paint);
	}
}
