package edu.chalmers.glaucoma.visionfield;

import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

public class VisionFieldResultView extends View {
	
	private Paint paint = new Paint();
	private Map<PointF, Boolean> resultMap = null;

	public VisionFieldResultView(Context context) {
		super(context);
		
		// Set the background color
		setBackgroundColor(Color.BLACK);
	}
	
	private void drawCross(Canvas canvas, int x, int y, int color) {

		// Save the current paint color
		int tempColor = paint.getColor();
		
		// Set the paint color to the given color.
		paint.setColor(color);
		
		// Set stroke width
		paint.setStrokeWidth(2);
		
		// Draw lines.
		canvas.drawLine(x, y-10, x, y+10, paint);
		canvas.drawLine(x-10, y, x+10, y, paint);
		
		// Reset the paint color.
		paint.setColor(tempColor);
	}
	
	protected void onDraw(Canvas canvas) {
		
		// Draw a cross on the screen.
		drawCross(canvas, getWidth()/2, getHeight()/2, Color.WHITE);
		
		// Set the paint value for the dots.
		paint.setStrokeWidth(5);
		
		// Draw points in resultMap...
		for (PointF p : resultMap.keySet() ) {
			paint.setColor( resultMap.get(p) ? Color.GREEN : Color.RED );
			canvas.drawPoint(p.x, p.y, paint);
		}
		
	}
	
	public void showResult(Map<PointF, Boolean> resultMap) {
		
		// Set result map and invalidate (in order to call onDraw())
		this.resultMap = resultMap;
		invalidate();
	}

}
