package edu.chalmers.glaucoma.Color;



import java.util.HashMap;
import java.util.Observable;
import java.math.*;
import android.graphics.PointF;
public class ColorDotEngine extends Observable {
		
		// Instance variables.
		private int width, height;
		private int centerX, centerY, startX = 0, startY = 0;
		private boolean dotRegistered;
		private HashMap<Integer, PointF> noticedChanges, foundChanges;
		private int noticedPoints, foundPoints, numColors = 3, numRadiuses = 3;
		private int[] colors, radiuses, numChangesColor, numChangesColor2;
		private double radian = 0.0174532925;
		private Integer[] toView;
		
		public ColorDotEngine(int width, int height) {
			if(width >0 && height >0){	//check so resolution given is valid
				this.width = width;
				this.height = height;

				// Set the screen ratio to 5:4. (50 degrees x 40 degrees)
				if ((this.height * 5) == (this.width * 4)){ //Screen already have the correct ratio
					
				}
				else if((this.height * 5) > (this.width * 4)){ //Screen is too high for the ratio, change it
					this.height = ((this.width * 4) / 5);
				}
				else if((this.height * 5) < (this.width * 4)){ //Screen is too wide for ratio, change it
					this.width = ((this.height * 5) / 4);
				}
				//Ratio might not be possible without changing both axis!
				
				centerX = this.width / 2;
				centerY = this.height / 2;
				startX = (width - this.width) / 2;
				startY = (height - this.height) / 2;
				
				noticedChanges = new HashMap<Integer, PointF>();
				foundChanges = new HashMap<Integer, PointF>();
				colors = new int[numColors];
				radiuses = new int[numRadiuses];
				numChangesColor = new int[numColors];
				numChangesColor2 = new int[numColors];
				toView = new Integer[2];
				addColors();
				addRadiuses();
			}
		}
		
		private void addColors(){
			colors[0] = -65536; //red
			colors[1] = -16776961; //blue
			colors[2] =  -16711936; //green
		}
		private void addRadiuses(){
			for(int r = 0; r < numRadiuses; r++){
				radiuses[r]= (centerY/4)*(r+1); 
			}
		}
		
		public int getNumNoticedDots() {
			return noticedPoints;
		}
		public int getNumFoundDots() {
			return foundPoints;
		}
		
		public void runTest(){
			noticedPoints = 0;
			foundPoints = 0;
			for(int c = 0; c < numColors; c++){
				numChangesColor[c]=0;
				for(int r = 0; r < numRadiuses; r++){
					runFirstTest(c, radiuses[r]);
				}
			}
			if(getNumNoticedDots()>0){
				int pos = 0;
				int colorPoints = numChangesColor[pos];
				numChangesColor2[pos]=0;
				
				for(int dot = 0; dot < getNumNoticedDots(); dot++){
					if(dot == colorPoints ){						//if dot has passed all dots of a color, change the color counter
						if(pos < numColors){						//check if there is another color to be used
							pos++;								//change the color counter
							numChangesColor2[pos]=0;				//set the counter to zero
							colorPoints = colorPoints + numChangesColor[pos];	//increase the limit with the new color dots.
						}
					}
					runSecondTest(noticedChanges.get(dot), pos);
				}
			}
				
		}
		
		public void runFirstTest(int color, int radius ){
			
			toView[0] = colors[color];	//package the color int to an integer for sending
			toView[1] = height/15;				//size of circle to draw.
			int degree = 0;
			double angle;
			PointF point = new PointF();
			
			setChanged();
			notifyObservers(toView[0]);
			clearChanged();
			setChanged();
			notifyObservers(toView[1]);
			clearChanged();
			dotRegistered = false;
			
			while(degree < 360){
				while(!dotRegistered){
						angle = degree * radian;
						int x = (int)Math.round((radius + 20) * Math.sin(angle));
						int y = (int)Math.round(radius * Math.cos(angle));
						point.set(centerX + startX + x , centerY + startY + y);
						
						if(degree == 360) break;
						else degree = degree + 1;
						
						// Give a new point
						setChanged();
						notifyObservers(point);
						clearChanged();
						//wait before moving on
						long time = System.currentTimeMillis() + radius/2;
						while (time > System.currentTimeMillis());
				}
				if(dotRegistered){
					noticedChanges.put(noticedPoints, new PointF(point.x, point.y));
					numChangesColor[color]++;
					noticedPoints++;
					dotRegistered = false;
				}
			}
		}
		
		private void runSecondTest(PointF point, int color){
			toView[0] = colors[color];	//package the color int to an integer for sending
			toView[1] = height/30;				//size of circle to draw.
			int degree = 0;
			double angle;
			int radius = centerY/10;
			PointF point2 = new PointF();
			
			setChanged();
			notifyObservers(toView[0]);
			clearChanged();
			setChanged();
			notifyObservers(toView[1]);
			clearChanged();
			dotRegistered = false;
			
			while(degree < 360){
				while(!dotRegistered){
						angle = degree * radian;
						int x = (int)Math.round(radius * Math.sin(angle));
						int y = (int)Math.round(radius * Math.cos(angle));
						point2.set(x + point.x, y + point.y);
						
						if(degree == 360) break;
						else degree++;
						
						// Give a new point
						setChanged();
						notifyObservers(point2);
						clearChanged();
						//wait before moving on
						long time = System.currentTimeMillis() + radius/2;
						while (time > System.currentTimeMillis());
				}
				if(dotRegistered){
					foundChanges.put(foundPoints, new PointF(point2.x, point2.y));
					numChangesColor2[color]++;
					foundPoints++;
					dotRegistered = false;
				}
			}
		}
		public int[] getColors(){
			return colors;
		}
		public int[] getNumChangesColor(){
			return numChangesColor;
		}
		public int[] getNumChangesColor2(){
			return numChangesColor2;
		}

		public HashMap<Integer, PointF> getDots() {
			if (getNumFoundDots()>0)
				return foundChanges;
			else
				return null;
		}
		
		public void registerDot() {
			dotRegistered = true;
		}
	}