package edu.chalmers.glaucoma;

import android.app.Activity;
import android.os.Bundle;
import edu.chalmers.glaucoma.visionfield.VisionFieldTestView;

public class VisionFieldActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		VisionFieldTestView view = new VisionFieldTestView(this);
		setContentView(view);
	}
}
