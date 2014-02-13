package edu.chalmers.glaucoma.visionfield;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;

public class VisionFieldTestView extends View {
	
	private Paint paint = new Paint();
	private PointF dot = null;
	private DotEngine engine = null;
	private boolean testIsRunning = false;

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
	
	private void drawDot(PointF p) {
		dot = p;
		invalidate();
	}
	
	private void removeDot() {
		drawDot(null);
	}
	
	
	public boolean onTouchEvent(MotionEvent e) {
		
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			
			if (testIsRunning) {
				
				// Register dot.
				engine.registerDot();
				
			} else {
				
				// Initialize the dot engine
				if (engine == null) {
					engine = new DotEngine(getWidth(), getHeight());
				}
				
				// Start the test.
				testIsRunning = true;
				VisionFieldTestTask testTask = new VisionFieldTestTask();
				testTask.execute();
			}
			
		}
		
		return true;
	}
	
	private class VisionFieldTestTask extends AsyncTask<Integer, Object, Integer> implements Observer {

		@Override
		protected Integer doInBackground(Integer... params) {
			
			engine.addObserver(VisionFieldTestTask.this);
			engine.runTest();
			return engine.getResult().size();
		}
		
		@Override
		protected void onProgressUpdate(Object... progress) {
			if (progress[0] instanceof PointF) {
				drawDot((PointF)progress[0]);
			} else if (progress[0] == null) {
				removeDot();
			}
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			
		}

		@Override
		public void update(Observable observable, Object data) {
			publishProgress(data);
		}
		
	}

}
