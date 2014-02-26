package edu.chalmers.glaucoma.visionfield;

public class VisionFieldDistance {
	
		// Takes pixels, pixels/inch, and an angle and returns the distance needed to cover that angle.
		public int calcDist(double pix,double pixd, float angle){
			double radPerDeg = 0.0174532925;
			float dist = (float)(pixToMm(pix,pixd)/(2 * Math.tan(radPerDeg *(angle/2))));
			return roundZeroDe(dist);
		}    
		//Takes a number and returns a rounded down float of the number with two decimals.	    	
		private float roundTwoDe(double var){
			float value = (float)Math.round(var * 100) / 100;
			return value;
		}
		//Takes a number and returns it rounded down as an int.
		private int roundZeroDe(float var){
			int value = (int)Math.round(var);
			return value;
		}
		//Takes pixels and pixels/inch and returns a rounded length.
		private float pixToMm(double pix,double pixd){
			float inch = (float) 25.4;
			pixd = roundTwoDe(pixd);
			float value = (float)((pix/pixd)*inch);
			value = roundZeroDe(value);
			return value;
		}
}
