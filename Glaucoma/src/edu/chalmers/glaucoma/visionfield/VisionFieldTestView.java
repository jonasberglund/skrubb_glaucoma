package edu.chalmers.glaucoma.visionfield;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

public class VisionFieldTestView extends View {
	
	private Paint paint = new Paint();
	private PointF dot = null;

	public VisionFieldTestView(Context context) {
		super(context);
		
//		setSystemUiVisibility(SYSTEM_UI_FLAG_FULLSCREEN | SYSTEM_UI_FLAG_HIDE_NAVIGATION | SYSTEM_UI_FLAG_IMMERSIVE);
		
		// Set the background color
		setBackgroundColor(Color.BLACK);
	}
	
	protected void onDraw(Canvas canvas) {
		
		// Set paint values.
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(2);
		
		// Draw a cross on the screen.
		canvas.drawLine(canvas.getWidth()/2, (canvas.getHeight()/2)-10, canvas.getWidth()/2, (canvas.getHeight()/2)+10, paint);
		canvas.drawLine((canvas.getWidth()/2)-10, canvas.getHeight()/2, (canvas.getWidth()/2)+10,canvas.getHeight()/2, paint);
		
		// Set the paint value for the dot.
		paint.setStrokeWidth(5);
		
		// Draw the dot.
		if (dot != null)
			canvas.drawPoint(dot.x, dot.y, paint);
		
	}
	
	public void drawDot(PointF p) {
		dot = p;
		invalidate();
	}
	
	public void removeDot() {
		drawDot(null);
	}

}
