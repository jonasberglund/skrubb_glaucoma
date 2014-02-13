package edu.chalmers.glaucoma.visionfield;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import android.graphics.PointF;

public class DotEngine extends Observable {
	
	private Random random = new Random();
	private int width, height;
	private boolean dotRegistered = false;
	private LinkedList<PointF> pointList;
	private List<PointF> seenPoints;
	
	public DotEngine(int width, int height) {
		this.width = width;
		this.height = height;
		
		seenPoints = new LinkedList<PointF>();
		pointList = new LinkedList<PointF>();
		
		// Create a list of points that have to be tested.
		for (int i=0;i<10;i++) {
			pointList.add(generateDot());
		}
		
		Collections.shuffle(pointList);
		
	}
	
	private PointF generateDot() {
		return new PointF(random.nextInt(width), random.nextInt(height));
	}
	
	public PointF nextDot() {
		return pointList.poll();
		
	}
	
	public void runTest() {
		
		PointF point = null;
		
		while ((point = nextDot()) != null ) {
			
			System.out.println("ENGINE: " + point.toString());
			
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
	
	public List<PointF> getResult() {
		if (pointList.size() == 0)
			return seenPoints;
		else
			return null;
	}
	
	public void registerDot() {
		dotRegistered = true;
	}
}
