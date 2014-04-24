package edu.chalmers.glaucoma.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.math.*;

import android.graphics.PointF;
public class ColorDotEngine extends Observable {
		
		// Instance variables.
		private int width, height;
		private int centerX, centerY, startX = 0, startY = 0;
		private boolean dotRegistered;
		private HashMap<Integer, PointF> noticedChanges;
		private int noticedPoints = 0, numColors = 3, numRadiuses = 3;
		private int[] colors, radiuses, numChangesColor;
		private Integer color;
		
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
				colors = new int[numColors];
				radiuses = new int[numRadiuses];
				numChangesColor = new int[numColors];
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
		
		public int getNumOfDots() {
			return noticedPoints;
		}
		
		public void runTest(){
			for(int c = 0; c < numColors; c++){
				numChangesColor[c]=0;
				for(int r = 0; r < numRadiuses; r++){
					runFirstTest(colors[c],radiuses[r], c);
				}
			}
		}
		
		public void runFirstTest(int color, int radius, int colorCount ){
			
			this.color = color;	//package the int to an integer for sending
			int degree = 0;
			double radian = 0.0174532925;
			double angle;
			PointF point = new PointF();
			
			setChanged();
			notifyObservers(this.color);
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
					numChangesColor[colorCount]++;
					noticedPoints = noticedPoints + 1;
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

		public HashMap<Integer, PointF> getDots() {
			if (getNumOfDots()>0)
				return noticedChanges;
			else
				return null;
		}
		
		public void registerDot() {
			dotRegistered = true;
		}
	}