package edu.chalmers.glaucoma;

import java.io.File;
import java.io.IOException;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import edu.chalmers.glaucoma.pupil.PupilResultView;
import edu.chalmers.glaucoma.pupil.PupilCalculate;

public class PupilActivity extends Activity {

	private static final int REQUEST_VIDEO = 0;
	private static final int REQUEST_PUPIL = 1;
	private Button button_start;
	private int[] pupilSizes, irisSizes, leftPupilSizes, rightPupilSizes;
	private PupilResultView resultView = null;
	private PupilCalculate pupilCalculate = null;
	private boolean leftEye, rightEye;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pupilmenu);
		
        pupilCalculate = new PupilCalculate();
        
        
		/** Start BUTTON **/
        button_start = (Button) findViewById(R.id.startbutton);
        button_start.setText("Start");
        button_start.setOnClickListener(new View.OnClickListener() {
			//@Override
			public void onClick(View v) {
				try {    	

					startTest();
					//recordPupil();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
    }
    private void startTest(){
    	
    	if(!leftEye){			//get left eye data
	    	getPupilData();
			}

    	else if(!rightEye){		//get right eye data
    			getPupilData();
    		}
    		
    	if(leftEye && rightEye){	//if both eyes are done, calculate and draw
    		//calculate the rest
			drawPupilCurve();

			leftEye = false;
			rightEye = false;
    	}
    }
    private void calculate(){
    	if(!leftEye && irisSizes != null && pupilSizes != null){
    		Log.i("PupilActivity", "Calculate Left");
    		leftPupilSizes = pupilCalculate.calculatePupils(irisSizes,pupilSizes);
    		leftEye = true;
    	}
    	else if(!rightEye && irisSizes != null && pupilSizes != null){
			Log.i("PupilActivity", "Calculate Right");
			rightPupilSizes = pupilCalculate.calculatePupils(irisSizes,pupilSizes);
			rightEye = true;

		}
		irisSizes = null;
		pupilSizes = null;
    }
    private void getPupilData(){
    	Intent i = new Intent(this, PupilMonitorActivity.class);
    	startActivityForResult(i, REQUEST_PUPIL);
    }
    
    private void recordPupil(){
    	Intent i = new Intent(this, PupilRecordActivity.class);
    	startActivityForResult(i, REQUEST_VIDEO);
    }
    
    private void drawPupilCurve(){
		resultView = new PupilResultView(this);
		setContentView(resultView);
		DisplayMetrics dm = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(dm);
		int h = dm.heightPixels;
		int w = dm.heightPixels;
		try {
			resultView.showResult(pupilCalculate.getPoints(leftPupilSizes,rightPupilSizes,w,h));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    }
    
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	super.onActivityResult(requestCode, resultCode, intent);
    	switch (requestCode) {
    		case REQUEST_PUPIL:
    			if(resultCode == RESULT_OK && intent != null){
    				irisSizes = intent.getIntArrayExtra("irisSizes");
    				pupilSizes = intent.getIntArrayExtra("pupilSizes");
    				calculate();
    			}
    		case REQUEST_VIDEO:
    			if(resultCode == RESULT_OK){
    				
    			}
    	}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
}