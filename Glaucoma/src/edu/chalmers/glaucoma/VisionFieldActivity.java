package edu.chalmers.glaucoma;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import edu.chalmers.glaucoma.visionfield.DotEngine;
import edu.chalmers.glaucoma.visionfield.VisionFieldTestView;
import edu.chalmers.glaucoma.visionfield.VisionFieldDistance;

public class VisionFieldActivity extends Activity{

	private VisionFieldTestView testView = null;
	
	private DotEngine engine = null;
	private boolean testIsRunning = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set view.
		testView = new VisionFieldTestView(this);
		setContentView(testView);
	}
	
	@Override
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
			
			if (!testIsRunning) {
				
				//Get the distance needed for the test				
				VisionFieldDistance distance = new VisionFieldDistance();
				
				DisplayMetrics dm = new DisplayMetrics();
			    getWindowManager().getDefaultDisplay().getMetrics(dm);
				
			    double xp = dm.heightPixels;
			    double xpd = dm.ydpi;
			    float angle = 40;
			    
			    int dist = distance.calcDist(xp, xpd, angle);
			    //Toast.makeText(this, getString(R.string.distanceMessage)+ dist, Toast.LENGTH_LONG).show();
			    
			    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
			    alertBuilder.setTitle(getString(R.string.distanceHeader));
			    alertBuilder.setMessage(getString(R.string.distanceMessage)+ dist +"mm\n");
			    alertBuilder.setPositiveButton(getString(R.string.distanceButton),
			      new DialogInterface.OnClickListener() {
			       public void onClick(DialogInterface dialog, int number) {
			        // Only close the dialog
			       }
			      });
			    AlertDialog infoDialog = alertBuilder.create();
			    infoDialog.show();
			    				
				// Initialize the dot engine
				if (engine == null)
					engine = new DotEngine(testView.getWidth(), testView.getHeight());
				
				// Start the test.
				new VisionFieldTestTask().execute();
			}
			
		}
		
		return true;
	}
	
	private class VisionFieldTestTask extends AsyncTask<Integer, Object, String> implements Observer {

		@Override
		protected String doInBackground(Integer... params) {

			testIsRunning = true;
			engine.addObserver(VisionFieldTestTask.this);
			engine.runTest();
			testIsRunning = false;
			
			int totDots = engine.getNumOfDots();
			
			return engine.getSeenDots().size() + "/" + totDots;
		}
		
		@Override
		protected void onProgressUpdate(Object... progress) {
			
			if (progress[0] instanceof PointF) {
				testView.drawDot((PointF)progress[0]);
			} else if (progress[0] == null) {
				testView.removeDot();
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			HashMap<PointF, Boolean> resultMap = new HashMap<PointF, Boolean>();
			
			// Add all dots
			for (PointF p : engine.getTestDots())
				resultMap.put(p, false);
			
			// Set seen dots to 'true'
			for (PointF p : engine.getSeenDots())
				resultMap.put(p, true);
			
			// Set engine to null.
			engine = null;
			
			// Show the result in another activity.
			Intent i = new Intent(VisionFieldActivity.this, VisionFieldTestResultActivity.class);
			i.putExtra("resultString", "Result: " + result);
			i.putExtra("resultMap", resultMap);
			startActivity(i);
			
			// Stop this activity.
			finish();
		}

		@Override
		public void update(Observable observable, Object data) {
			publishProgress(data);
		}
		
	}
}
