package edu.chalmers.glaucoma;

import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FlashLightActivity extends Activity {

	private boolean isLighOn = false;
	private Camera camera;
	private int nr_test=3;
	private int flashTimeMs = 500;
	private int waitTimeMs =2000;
	
	private Button button;
	
	@Override
	protected void onStop() {
		super.onStop();

		if (camera != null) {
			camera.release();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flash_light);
		
		button = (Button) findViewById(R.id.buttonFlashlight);
		
		Context context = this;
		PackageManager pm = context.getPackageManager();

		// if device support camera?
		if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			Log.e("err", "Device has no camera!");
			return;
		}

		camera = Camera.open();
		final Parameters p = camera.getParameters();
		
		
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!isLighOn) {
					isLighOn = true;
					
					Log.i("info", "torch is turn off!");

					for(int i=0;i<=nr_test;i++){
						
						Log.i("info", "torch is turn on!");
						
						p.setFlashMode(Parameters.FLASH_MODE_TORCH);
	
						camera.setParameters(p);
						camera.startPreview();
						
						changeIntensity();
						changeIntensity();
						
						//How many ms the flashlight will be on
						try {
						    Thread.sleep(flashTimeMs);
						} catch(InterruptedException ex) {
						    Thread.currentThread().interrupt();
						}
						//film
						//
						
						p.setFlashMode(Parameters.FLASH_MODE_OFF);
						camera.setParameters(p);
						camera.stopPreview();
						 
						//time to wait between flash
						try {
						    Thread.sleep(waitTimeMs);
						} catch(InterruptedException ex) {
						    Thread.currentThread().interrupt();
						}
					}
					//
					
					isLighOn = false;


				} 

			}

			
		});
	}
	public void changeIntensity()
	{
	    camera.stopPreview();
	    camera.startPreview();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.flash_light, menu);
		return true;
	}

}
