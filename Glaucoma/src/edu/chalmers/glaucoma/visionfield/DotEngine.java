package edu.chalmers.glaucoma.visionfield;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import android.graphics.PointF;

public class DotEngine extends Observable {
	
	// Instance variables.
	private int width, height;
	private boolean dotRegistered;
	private LinkedList<PointF> pointList;
	private LinkedList<PointF> originalPointList;
	private LinkedList<PointF> missedPoints;
	private List<PointF> seenPoints;
	private int originX = 0, originY = 0, numOfDots;
	private boolean missedTest = false;
	private boolean flag = false;
	private int seenPos = 0;
	
	public DotEngine(int width, int height) {
		this.width = width;
		this.height = height;
		
		// Set the screen ratio to 3:2. (60 degrees x 40 degrees)
		if (this.width > this.height) {
			int halfWidth = width/2;
			this.width = (3 * this.height) / 2;
			originX = halfWidth-(this.width/2);
		} else if (this.width < this.height) {
			int halfHeight = height/2;
			this.height = (2 * this.width) / 3;
			originY = halfHeight-(this.height/2);
		}
		
		seenPoints = new LinkedList<PointF>();
		pointList = new LinkedList<PointF>();
		missedPoints = new LinkedList<PointF>();
		
		// Create a list of points that have to be tested.
		originalPointList = createDotList();
		pointList = (LinkedList<PointF>) originalPointList.clone();
		numOfDots = pointList.size();
		
	}
	
	/**
	 * createDotList creates a list of given dots to be tested. Also, the method shuffles the list.
	 */
	private LinkedList<PointF> createDotList() {
		
		LinkedList<PointF> relativePointList = new LinkedList<PointF>();
		
		// (relative) points to be tested.
		relativePointList.add(new PointF(0.1f, 0.5f));
		relativePointList.add(new PointF(0.2f, 0.5f));
		relativePointList.add(new PointF(0.3f, 0.5f));
		relativePointList.add(new PointF(0.4f, 0.5f));
		relativePointList.add(new PointF(0.6f, 0.5f));
		relativePointList.add(new PointF(0.7f, 0.5f));
		relativePointList.add(new PointF(0.8f, 0.5f));
		relativePointList.add(new PointF(0.9f, 0.5f));
		
		relativePointList.add(new PointF(0.5f, 0.1f));
		relativePointList.add(new PointF(0.5f, 0.2f));
		relativePointList.add(new PointF(0.5f, 0.3f));
		relativePointList.add(new PointF(0.5f, 0.4f));
		relativePointList.add(new PointF(0.5f, 0.6f));
		relativePointList.add(new PointF(0.5f, 0.7f));
		relativePointList.add(new PointF(0.5f, 0.8f));
		relativePointList.add(new PointF(0.5f, 0.9f));
		
		// Left upper quadrant
		relativePointList.add(new PointF(0.1f, 0.6f));
		relativePointList.add(new PointF(0.2f, 0.6f));
		relativePointList.add(new PointF(0.3f, 0.6f));
		relativePointList.add(new PointF(0.4f, 0.6f));
		
		relativePointList.add(new PointF(0.2f, 0.7f));
		relativePointList.add(new PointF(0.3f, 0.7f));
		relativePointList.add(new PointF(0.4f, 0.7f));
		
		relativePointList.add(new PointF(0.3f, 0.8f));
		relativePointList.add(new PointF(0.4f, 0.8f));
		
		relativePointList.add(new PointF(0.3f, 0.9f));
		relativePointList.add(new PointF(0.4f, 0.9f));
		
		// Left lower quadrant
		relativePointList.add(new PointF(0.1f, 0.4f));
		relativePointList.add(new PointF(0.2f, 0.4f));
		relativePointList.add(new PointF(0.3f, 0.4f));
		relativePointList.add(new PointF(0.4f, 0.4f));
		
		relativePointList.add(new PointF(0.2f, 0.3f));
		relativePointList.add(new PointF(0.3f, 0.3f));
		relativePointList.add(new PointF(0.4f, 0.3f));
		
		relativePointList.add(new PointF(0.3f, 0.2f));
		relativePointList.add(new PointF(0.4f, 0.2f));
		
		relativePointList.add(new PointF(0.3f, 0.1f));
		relativePointList.add(new PointF(0.4f, 0.1f));
		
		// Right upper quadrant
		relativePointList.add(new PointF(0.9f, 0.6f));
		relativePointList.add(new PointF(0.8f, 0.6f));
		relativePointList.add(new PointF(0.7f, 0.6f));
		relativePointList.add(new PointF(0.6f, 0.6f));
		
		relativePointList.add(new PointF(0.8f, 0.7f));
		relativePointList.add(new PointF(0.7f, 0.7f));
		relativePointList.add(new PointF(0.6f, 0.7f));
		
		relativePointList.add(new PointF(0.7f, 0.8f));
		relativePointList.add(new PointF(0.6f, 0.8f));
		
		relativePointList.add(new PointF(0.7f, 0.9f));
		relativePointList.add(new PointF(0.6f, 0.9f));
		
		// Right lower quadrant
		relativePointList.add(new PointF(0.9f, 0.4f));
		relativePointList.add(new PointF(0.8f, 0.4f));
		relativePointList.add(new PointF(0.7f, 0.4f));
		relativePointList.add(new PointF(0.6f, 0.4f));
		
		relativePointList.add(new PointF(0.8f, 0.3f));
		relativePointList.add(new PointF(0.7f, 0.3f));
		relativePointList.add(new PointF(0.6f, 0.3f));
		
		relativePointList.add(new PointF(0.7f, 0.2f));
		relativePointList.add(new PointF(0.6f, 0.2f));
		
		relativePointList.add(new PointF(0.7f, 0.1f));
		relativePointList.add(new PointF(0.6f, 0.1f));
			
		LinkedList<PointF> list = new LinkedList<PointF>();
		
		// Add points
		for (PointF p : relativePointList)
			list.add(new PointF(originX+p.x*width, originY+p.y*height));
		
		// Shuffle the list.
		Collections.shuffle(list);
		
		return list;
	}
	
	/**
	 * nextDot polls and return a dot from pointList, the list of points to be tested.
	 */
	public PointF nextDot() {
		
		// Poll a PointF from the point list.
		if(!missedTest) {
			return pointList.poll();
		
		} else {
			
			//If the missed-test is running, pull dots from seen and missed dots.
			//Requires at least one seen point.
			if(flag){
				
				if(seenPoints.get(seenPos)== null)
					seenPos = 0;
				
				flag = false;
				return seenPoints.get(seenPos++);
			
			} else {
				flag = true;
				return missedPoints.poll();
			}
		}
			
	}
	
	/**
	 * getNumOfDots returns number of dots in the test.
	 */
	public int getNumOfDots() {
		return numOfDots;
	}
	
	/**
	 * getTestDots returns the dots in the test.
	 */
	public List<PointF> getTestDots() {
		return originalPointList;
	}
	
	/**
	 * runTest runs the test, which means: notify observers with dots to be tested,
	 * wait, put the dot either in a list of seen dots, or a list of missed dots.
	 * runTest should be executed in a thread.
	 */
	public void runTest() {
		
		PointF point = null;
		
		while ((point = nextDot()) != null ) {
			
			dotRegistered = false;
			
			// Wait 1 sec
			long time = System.currentTimeMillis() + 1000;
			while (time > System.currentTimeMillis());
			
			// Give a new point
			setChanged();
			notifyObservers(point);
			clearChanged();
			
			// Wait 0.2 sec (the time the dot should be visible)
			time = System.currentTimeMillis() + 200;
			while (time > System.currentTimeMillis());
			
			// Give null to observers (remove the dot)
			setChanged();
			notifyObservers(null);
			clearChanged();
			
			// Wait 3 sec (the time the user can answer)
			time = System.currentTimeMillis() + 3000;
			while (!dotRegistered && time > System.currentTimeMillis());
			
			// Add seen dots to a list. Missed dots are added to another.
			if(!missedTest){
				if (dotRegistered)
					seenPoints.add(point);
				else
					missedPoints.add(point);
			}
			
			// If missed-test is running, missed points should not be added again.
			else{
				if(dotRegistered && flag)
					seenPoints.add(point);
			}
		}
		
	}
	
	/**
	 * This method should be called after runTest() method. This test tests missed dots one more time.
	 */
	public void runMissed(){
		missedTest = true;
		runTest();
		missedTest = false;
	}
	
	/**
	 * getSeenDots returns a list of dots registered by the user. If all points are not tested, null is returned.
	 */
	public List<PointF> getSeenDots() {
		if (pointList.size() == 0)
			return seenPoints;
		else
			return null;
	}
	
	/**
	 * registerDot sets dotRegistered to true. dotRegistered is used in runTest to know if the user saw the dot.
	 */
	public void registerDot() {
		dotRegistered = true;
	}
}
