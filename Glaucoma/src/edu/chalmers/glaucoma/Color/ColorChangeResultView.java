package edu.chalmers.glaucoma.Color;


import java.util.HashMap;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

public class ColorChangeResultView extends View {
	
	private Paint paint = new Paint();
	private HashMap<Integer, PointF> result = null;
	private int[] colors, numColor = null;
	private int numPoints = 0;

	public ColorChangeResultView(Context context) {
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
		
		
		// Draw points from result...
		
		int pos = 0, colorPoints = numColor[pos];
		paint.setColor(colors[pos]);
		
		for( int point = 0; point < result.size(); point++){
			if(point == colorPoints ){
				if(numColor.length > pos){
					pos++;
					colorPoints = colorPoints + numColor[pos];
					paint.setColor(colors[pos]);
				}
			}
			canvas.drawPoint(result.get(point).x,result.get(point).y, paint);
		}
	}
	
	public void showResult(HashMap<Integer, PointF> result, int[] colors, int[] numColor) {
		
		// Set result map and invalidate (in order to call onDraw())
		this.result = result;
		this.colors = colors;
		this.numColor = numColor;
		invalidate();
	}

}
