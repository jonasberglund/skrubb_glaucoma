package edu.chalmers.glaucoma.Color;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

public class ColorChangeView extends View {
	
	// Instance variables.
	private Paint paint = new Paint();
	private PointF dot = null;
	private int size = 30;

	public ColorChangeView(Context context) {
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
	public void setDetails(int data){	//figure out if the sent data affects color or size
		if(0<data && data<1000){
			setSize(data);
		}
		else setColor(data);
	}
	
	private void setColor(int color){
		paint.setColor(color);
	}
	private void setSize(int size){
		this.size = size;
	}
	
	protected void onDraw(Canvas canvas) {
	
		// Draw a cross on the screen.
		drawCross(canvas, getWidth()/2, getHeight()/2, Color.WHITE);
		
		// Draw the dot.
		if (dot != null)
			canvas.drawCircle(dot.x,dot.y , size, paint );
			
	}
	
	public void drawDot(PointF p) {
		
		// Set new dot and invalidate (in order to call onDraw())
		dot = p;
		invalidate();
	}
	
	public void removeDot() {
		drawDot(null);
	}

}
