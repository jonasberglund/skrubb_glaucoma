package edu.chalmers.glaucoma;

import java.util.HashMap;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.Toast;
import edu.chalmers.glaucoma.visionfield.VisionFieldResultView;

public class VisionFieldTestResultActivity extends Activity{

	private VisionFieldResultView resultView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the results from the test activity.
		String resultString = getIntent().getStringExtra("resultString");
		HashMap<PointF, Boolean> resultMap = 
				(HashMap<PointF, Boolean>) getIntent().getSerializableExtra("resultMap");
		
		// Check if the results really are results.
		if (resultString != null && !resultString.equals("") && resultMap != null) {
			
			Toast.makeText(this, resultString, Toast.LENGTH_LONG).show();
			
			// Set view.
			resultView = new VisionFieldResultView(this);
			setContentView(resultView);
			
			resultView.showResult(resultMap);
		
		} else {
			finish();
		}
		
	}
	
}
