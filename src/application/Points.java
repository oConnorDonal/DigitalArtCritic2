package application;

import org.opencv.core.Point;

public class Points {
	
	private double dist;
	private Point linePt;
	
	public Points(Point pt1, double distanceFromPoint){
		
		this.dist = distanceFromPoint;
		this.linePt = pt1;
	}

}
