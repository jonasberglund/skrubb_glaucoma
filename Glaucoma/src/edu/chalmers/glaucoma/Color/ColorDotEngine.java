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
		private int centerX, centerY, startX = 0, startY = 0, endX, endY;
		private boolean dotRegistered;
		private LinkedList<PointF> pointList;
		private LinkedList<PointF> originalPointList;
		private List<PointF> noticedChanges;
		private boolean flag = false;
		private int seenPos = 0;
		private int noticedPoints = 0;
		
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
				endX = this.width;
				endY = this.height;
				
				noticedChanges = new LinkedList<PointF>();
				pointList = new LinkedList<PointF>();
				
				// Create a list of points that have to be tested.
				originalPointList = createDotList();
				pointList = (LinkedList<PointF>) originalPointList.clone();
				}
		}
		
		private LinkedList<PointF> createDotList() {
			
			LinkedList<PointF> relativePointList = new LinkedList<PointF>();
			
			// (relative) points to be tested.
			relativePointList.add(new PointF(0.1f, 0.5f));
			relativePointList.add(new PointF(0.2f, 0.4f));
			relativePointList.add(new PointF(0.3f, 0.3f));
			relativePointList.add(new PointF(0.4f, 0.2f));
			relativePointList.add(new PointF(0.5f, 0.1f));
			relativePointList.add(new PointF(0.6f, 0.2f));
			relativePointList.add(new PointF(0.7f, 0.3f));
			relativePointList.add(new PointF(0.8f, 0.4f));
			relativePointList.add(new PointF(0.9f, 0.5f));
			
			relativePointList.add(new PointF(0.8f, 0.6f));
			relativePointList.add(new PointF(0.7f, 0.7f));
			relativePointList.add(new PointF(0.6f, 0.8f));
			relativePointList.add(new PointF(0.5f, 0.9f));
			relativePointList.add(new PointF(0.4f, 0.8f));
			relativePointList.add(new PointF(0.3f, 0.7f));
			relativePointList.add(new PointF(0.2f, 0.6f));
			
			
			LinkedList<PointF> list = new LinkedList<PointF>();
			
			// Add points
			for (PointF p : relativePointList)
				list.add(new PointF(startX + (p.x * width), startY + (p.y * height)));
			
			return list;
		}
		
		public PointF nextDot() {
			
			// Poll a PointF from the point list.
			return pointList.poll();

		}
		
		public int getNumOfDots() {
			return noticedPoints;
		}
		
		public List<PointF> getTestDots() {
			return originalPointList;
		}
		
		public void runTest() {
			
			PointF point = null;
			PointF pointNext = new PointF();
			
			while ((point = nextDot()) != null ) {
				
				//the next point is used to determine the direction to move in.
				pointNext = pointList.peek();
				if(pointNext == null){
					pointNext = originalPointList.peek(); //for the last dot, look at the first dot
				}
				dotRegistered = false;
				
				// Give a new point
				setChanged();
				notifyObservers(point);
				clearChanged();
				
				// Add seen dots to a list. Missed dots are added to another.
				while(!dotRegistered){
					if((point.x < pointNext.x) && (point.y < pointNext.y))
						point.set((point.x + 1),(point.y + 1));
					
					else if((point.x > pointNext.x) && (point.y > pointNext.y))
						point.set((point.x - 1),(point.y - 1));
					
					else if((point.x > pointNext.x) && (point.y < pointNext.y))
						point.set((point.x - 1),(point.y + 1));
					
					else if((point.x < pointNext.x) && (point.y > pointNext.y))
						point.set((point.x + 1),(point.y - 1));
						

					// Give a new point
					setChanged();
					notifyObservers(point);
					clearChanged();
					
					long time = System.currentTimeMillis() + 100;
					while (time > System.currentTimeMillis());
					
				}
				
				noticedChanges.add(point);
			}
			noticedPoints = noticedChanges.size();
			
		}
		
		public void runCircleTest(){
			PointF point = new PointF();
			int radius = centerY - 30;
			int degree = 0;
			double radian = 0.0174532925;
			double angle;
			
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
					
					long time = System.currentTimeMillis() + 40;
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