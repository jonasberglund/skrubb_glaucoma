package edu.chalmers.glaucoma;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;
import edu.chalmers.glaucoma.visionfield.DotEngine;
import edu.chalmers.glaucoma.visionfield.VisionFieldTestView;

public class VisionFieldActivity extends Activity{

	private VisionFieldTestView view = null;
	private DotEngine engine = null;
	private boolean testIsRunning = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set view.
		view = new VisionFieldTestView(this);
		setContentView(view);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) && testIsRunning) {
			
			// Register dot.
			engine.registerDot();
			
			return true;
			
		} else {
			
			return super.onKeyDown(keyCode, event);
		}
		
	}
	
	
	public boolean onTouchEvent(MotionEvent e) {
		
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			
			if (testIsRunning) {
				
				// Register dot.
//				engine.registerDot();
				
			} else {
				
				// Initialize the dot engine
				if (engine == null) {
					engine = new DotEngine(view.getWidth(), view.getHeight());
				}
				
				// Start the test.
				testIsRunning = true;
				VisionFieldTestTask testTask = new VisionFieldTestTask();
				testTask.execute();
			}
			
		}
		
		return true;
	}
	
	private void showInToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
	
	private class VisionFieldTestTask extends AsyncTask<Integer, Object, String> implements Observer {

		@Override
		protected String doInBackground(Integer... params) {
			
			engine.addObserver(VisionFieldTestTask.this);
			engine.runTest();
			testIsRunning = false;
			
			int totDots = engine.getNumOfDots();
			
			return engine.getResult().size() + "/" + totDots;
		}
		
		@Override
		protected void onProgressUpdate(Object... progress) {
			if (progress[0] != null)
				System.out.println("CONTROLLER: " + progress[0].toString());
			else
				System.out.println("CONTROLLER: NULL");
			if (progress[0] instanceof PointF) {
				view.drawDot((PointF)progress[0]);
			} else if (progress[0] == null) {
				view.removeDot();
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			showInToast("Result: " + result);
		}

		@Override
		public void update(Observable observable, Object data) {
			publishProgress(data);
		}
		
	}
}
