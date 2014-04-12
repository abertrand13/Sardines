package com.lahacks.sardines;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CompassView extends View {
	
	Context context;
	
	public CompassView(Context context)
	{
	    super(context);
	    this.context = context;

	}

	public CompassView(Context context, AttributeSet attrs)
	{
	    super(context, attrs);
	    this.context = context;
	}

	public CompassView(Context context, AttributeSet attrs, int defStyle)
	{
	    super(context, attrs, defStyle);
	    this.context = context;
	}

   @Override
   protected void onDraw(Canvas canvas) {
      // TODO Auto-generated method stub
      super.onDraw(canvas);
      int x = getWidth();
      int y = getHeight();
      int radius;
      radius = 100;
      Paint paint = new Paint();
      paint.setStyle(Paint.Style.FILL);
      paint.setColor(Color.WHITE);
      canvas.drawPaint(paint);
      // Use Color.parseColor to define HTML colors
      paint.setColor(Color.parseColor("#CD5C5C"));
      canvas.drawCircle(x / 2, y / 2, radius, paint);
  }
}
