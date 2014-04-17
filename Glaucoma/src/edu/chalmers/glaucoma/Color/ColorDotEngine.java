package edu.chalmers.glaucoma.Color;

import java.util.Collections;
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
		private LinkedList<PointF> pointList;
		private List<PointF> noticedChanges;
		private int noticedPoints = 0;
		private int[] colors, radiuses;
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
				
				noticedChanges = new LinkedList<PointF>();
				pointList = new LinkedList<PointF>();
				colors = new int[3];
				radiuses = new int[3];
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
			for(int r = 1; r < 4; r++){
				radiuses[r-1]= (centerY/4)* r; 
			}
		}
		public PointF nextDot() {
			// Poll a PointF from the point list.
			return pointList.poll();

		}
		
		public int getNumOfDots() {
			return noticedPoints;
		}
		
		public void runTest(){
			for(int c = 0; c < 3; c++){
				for(int r = 0; r < 3; r++){
					runCircleTest(colors[c],radiuses[r]);
				}
			}
		}
		
		public void runCircleTest(int c, int radius){
			
			PointF point = new PointF();
			this.color = new Integer(c);	//package the int to an integer for sending
			int degree = 0;
			double radian = 0.0174532925;
			double angle;
			
			setChanged();
			notifyObservers(this.color);
			clearChanged();

			while(degree < 360){
				dotRegistered = false;
				
				while(!dotRegistered){
						
						angle = degree * radian;
						int x = (int)Math.round(radius * Math.sin(angle));
						int y = (int)Math.round(radius * Math.cos(angle));
						point.set(centerX + startX + x , centerY + startY + y);
						if(degree == 360) break;
						else degree = degree + 1;
						
						// Give a new point
						setChanged();
						notifyObservers(point);
						clearChanged();
						//wait before moving on
						long time = System.currentTimeMillis() + 20;
						while (time > System.currentTimeMillis());
				}
				if(dotRegistered){
					noticedChanges.add(new PointF(point.x, point.y));
					noticedPoints = noticedPoints + 1;
					
				}
			}	
		}
		public List<PointF> getDots() {
			if (getNumOfDots()>0)
				return noticedChanges;
			else
				return null;
		}
		
		public void registerDot() {
			dotRegistered = true;
		}
	}