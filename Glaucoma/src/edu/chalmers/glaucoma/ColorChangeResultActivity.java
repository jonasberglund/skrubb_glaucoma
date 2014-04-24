package edu.chalmers.glaucoma;


import java.util.HashMap;


import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.Toast;
import edu.chalmers.glaucoma.Color.ColorChangeResultView;

public class ColorChangeResultActivity extends Activity{

	private ColorChangeResultView resultView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		String resultString = getIntent().getStringExtra("resultString");
		HashMap<Integer, PointF> resultMap = (HashMap<Integer, PointF>) getIntent().getSerializableExtra("resultMap");
		int[] colors = (int[]) getIntent().getIntArrayExtra("colors");
		int[] numColor = (int[]) getIntent().getIntArrayExtra("numColor");
		
		if (colors.length > 0 && numColor.length > 0 && resultMap != null) {
			Toast.makeText(this, resultString, Toast.LENGTH_LONG).show();			
			resultView = new ColorChangeResultView(this);
			setContentView(resultView);
			
			resultView.showResult( resultMap, colors, numColor);
		
		} else {
			finish();
		}
		
	}
	
}