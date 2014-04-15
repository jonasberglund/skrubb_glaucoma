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
import edu.chalmers.glaucoma.Color.ColorDotEngine;
import edu.chalmers.glaucoma.Color.ColorChangeView;
import edu.chalmers.glaucoma.visionfield.VisionFieldDistance;

public class ColorChangeActivity extends Activity{

	private ColorChangeView testView = null;
	
	private ColorDotEngine engine = null;
	private boolean testIsRunning = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set view.
		testView = new ColorChangeView(this);
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
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			
			if (!testIsRunning) {
				
				//Get the distance needed for the test				
				VisionFieldDistance distance = new VisionFieldDistance();
				
				DisplayMetrics dm = new DisplayMetrics();
			    getWindowManager().getDefaultDisplay().getMetrics(dm);
				
			    double xp = dm.widthPixels;
			    double xpd = dm.xdpi;
			    double yp = dm.heightPixels;
			    double ypd = dm.ydpi;
			    float angle = 40;
			    int dist = 0;
			    
			    //check orientation on screen for correct distance
			    if(xp<yp){
			    	dist = distance.calcDist(xp, xpd, angle);
			    }
			    else{
			    	dist = distance.calcDist(yp, ypd, angle);
			    	
			    
			    }
			    	//Toast.makeText(this, getString(R.string.distanceMessage)+ dist, Toast.LENGTH_LONG).show();
/*			    
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
*/			    				
				// Initialize the dot engine
				if (engine == null)
					engine = new ColorDotEngine(testView.getWidth(), testView.getHeight());
				
				// Start the test.
				new ColorChangeTask().execute();
			}
			
		}
		
		return true;
	}
	
	private class ColorChangeTask extends AsyncTask<Integer, Object, String> implements Observer {

		@Override
		protected String doInBackground(Integer... params) {

			testIsRunning = true;
			engine.addObserver(ColorChangeTask.this);
			engine.runCircleTest();
			//engine.runTest();
			testIsRunning = false;
			
			return engine.getNumOfDots() + ".";
		}
		
		@Override
		protected void onProgressUpdate(Object... progress) {
			
			if (progress[0] instanceof PointF) {
				testView.drawDot((PointF)progress[0]);
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			HashMap<PointF, Boolean> resultMap = new HashMap<PointF, Boolean>();
			
			// Set registered dots to 'true'
			if(engine.getDots()!= null){
				for (PointF p : engine.getDots())
					resultMap.put(p, true);
			}			
			// Set engine to null.
			engine = null;
			
			// Show the result in another activity.
			Intent i = new Intent(ColorChangeActivity.this, VisionFieldTestResultActivity.class);
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

