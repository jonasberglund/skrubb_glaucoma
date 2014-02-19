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
	private List<PointF> seenPoints;
	private int originX = 0, originY = 0, numOfDots;
	
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
		} else {
			// TODO
		}
		
		seenPoints = new LinkedList<PointF>();
		pointList = new LinkedList<PointF>();
		
		// Create a list of points that have to be tested.
		originalPointList = createDotList();
		pointList = (LinkedList<PointF>) originalPointList.clone();
		numOfDots = pointList.size();
		
	}
	
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
	
	public PointF nextDot() {
		
		// Poll a PointF from the point list.
		return pointList.poll();
	}
	
	public int getNumOfDots() {
		return numOfDots;
	}
	
	public List<PointF> getTestDots() {
		return originalPointList;
	}
	
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
			
			if (dotRegistered)
				seenPoints.add(point);
			
		}
		
	}
	
	public List<PointF> getSeenDots() {
		if (pointList.size() == 0)
			return seenPoints;
		else
			return null;
	}
	
	public void registerDot() {
		dotRegistered = true;
	}
}
