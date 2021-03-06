package edu.chalmers.glaucoma;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Vision button
		 final Button button_vision = (Button) findViewById(R.id.button_start_vision);
		 button_vision.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startVisionFieldActivity();
			}
		});
		 
		//Pupil button
		 final Button button_pupil = (Button) findViewById(R.id.button_start_pupil);
		 button_pupil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startPupilActivity();
			}
		});
        
		//Color button
		 final Button button_color = (Button) findViewById(R.id.button_start_color);
		 button_color.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startColorChangeActivity();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void startPupilMonitorActivity() {
		Intent i =  new Intent(this, PupilMonitorActivity.class);
		startActivity(i);
	}
	
	public void startVisionFieldActivity() {
		Intent i =  new Intent(this, VisionFieldActivity.class);
		startActivity(i);
	}
	
	public void startPupilActivity() {
		Intent i =  new Intent(this, PupilActivity.class);
		startActivity(i);
	}
	
	public void startColorChangeActivity() {
		Intent i =  new Intent(this, ColorChangeActivity.class);
		startActivity(i);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == R.id.pupillreaktion) {
			startPupilActivity();
		} else if (item.getItemId() == R.id.syntest) {
			startVisionFieldActivity();
		} else if (item.getItemId() == R.id.pupillmonitor) {
			startPupilMonitorActivity();
		} else if (item.getItemId() == R.id.colorchange) {
			startColorChangeActivity();
		}
		
		return super.onOptionsItemSelected(item);
	}

}
