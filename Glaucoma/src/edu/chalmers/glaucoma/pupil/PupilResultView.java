package edu.chalmers.glaucoma.pupil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.View;

public class PupilResultView extends View {
		
		private Paint paint = new Paint();
		private Point[] pupils = null;
		public PupilResultView(Context context) {
			super(context);
			
			// Set the background color
			setBackgroundColor(Color.BLACK);
		}
		private void drawLines(Canvas canvas) {
			
			// Set the paint color.
			paint.setColor(Color.GRAY);
			
			// Set stroke width
			paint.setStrokeWidth(2);
			
			// Draw lines.
			canvas.drawLine(0, getHeight()/3, getWidth(), getHeight()/3, paint);
			canvas.drawLine(0, (getHeight()/3)*2, getWidth(), (getHeight()/3)*2, paint);
			// Draw flash lines
			canvas.drawLine(25,0,25,getWidth(), paint);
			canvas.drawLine(225,0,225,getWidth(), paint);
			canvas.drawLine(425,0,425,getWidth(), paint);
			canvas.drawLine(625,0,625,getWidth(), paint);
		}
		
		protected void onDraw(Canvas canvas) {
			
			drawLines(canvas);
			
			// Set the paint value for the dots.
			paint.setColor(Color.WHITE);
			paint.setStrokeWidth(5);
			// Draw points in array
			for (int p = 0; p < pupils.length; p++) {
				canvas.drawPoint(pupils[p].x ,pupils[p].y, paint);	
			}

		}
		
		public void showResult(Point[] pupilsArray) {
			//Make a deep copy of the array
			pupils = new Point[pupilsArray.length];
			for (int p = 0; p < pupils.length; p++) {
			    Point point = pupilsArray[p];
			    if (point != null) {
			        pupils[p] = new Point(point);
			    }
				invalidate();
			}
		}

	}