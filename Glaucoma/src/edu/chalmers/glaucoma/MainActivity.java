package edu.chalmers.glaucoma;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void startPupillActivity() {
		Intent i =  new Intent(this, PupilActivity.class);
		startActivity(i);
	}
	
	public void startSyntestActivity() {
		Intent i =  new Intent(this, VisionFieldActivity.class);
		startActivity(i);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == R.id.pupillreaktion) {
			startPupillActivity();
		} else if (item.getItemId() == R.id.syntest) {
			startSyntestActivity();
		}
		
		return super.onOptionsItemSelected(item);
	}

}
