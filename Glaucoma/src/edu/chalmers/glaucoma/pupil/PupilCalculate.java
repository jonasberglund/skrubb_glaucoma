package edu.chalmers.glaucoma.pupil;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

public class PupilCalculate {
	
	private int factor = 15;

	public PupilCalculate(){	//Adjust pupil sizes to fit the scale of the first iris.
	}
	public int[] calculatePupils(int[] irisSizes, int[]pupilSizes){
		int irisRefSize;
		
		if(irisSizes[0] != 0){
    		irisRefSize = irisSizes[0];
    	}
    	else{
    		irisRefSize = 1;
    	}
    	int[] pupilSizesAdjusted = new int[pupilSizes.length];
    	for(int p = 0; p < pupilSizes.length; p++){
    		if(irisSizes[p]!=0){
    			float scale = (float)irisRefSize/(float)irisSizes[p];
    			float tempSize = scale * pupilSizes[p];
    			pupilSizesAdjusted[p]= (int)Math.round(tempSize);
    			Log.i("PupilActivity","Nr:"+p+"Iris:"+irisSizes[p]+" Pupil:"+pupilSizes[p]+" AdjPupil:"+pupilSizesAdjusted[p]+" Scale:"+scale);
    		}
    		else{
    			pupilSizesAdjusted[p]=0;
    		}
    	}
    	return pupilSizesAdjusted;
  	}
	
	public Point[] getPoints(int[] leftPupils, int[] rightPupils, int width, int height){	//Makes all the found pupil into points to be drawn.
		int l = leftPupils.length, r = rightPupils.length, lIris = leftPupils[0], rIris = rightPupils[0]; 
		Point[] pupils = new Point[l + r];
		for(int p = 0; p < (l + r); p++) {		
			if(p < l){										//make a left eye dot.
				pupils[p] = new Point(p*20+5, (width/3) - (leftPupils[p]-lIris)*factor);
			}
			else{											//make a right eye dot.
				pupils[p] = new Point((p-l)*20+5,((width*2)/3) - (rightPupils[p-l]-rIris)*factor);
			}
		}
		return pupils;
	}
	
}
