/*
 * Digital Art Critic 
 * Donal O Connor
 * C11529667  
 * 
 */


package application;

import java.awt.geom.Line2D;
import org.opencv.core.Point;

public class Line {

	Point pt1;
	Point pt2;
	
	//Each line object starts with start and end points

	public Line(Point pt1, Point pt2) {

		this.pt1 = pt1;
		this.pt2 = pt2;

	}
	
	/*
	 * Methods below using the slope formula 
	 * by subtracting x and y points
	 */

	public double getSlope() {
		return this.getDY() / this.getDX();
	}

	public double getDY() {
		double dy = pt2.y - pt1.y;
		return dy;
	}

	public double getDX() {
		double dx = pt1.x - pt2.x;
		return dx;
	}

	//to string method to output locations during testing
	
	
	public String toString() {
		return "x1: " + pt1.x + " y1: " + pt1.y + " x2: " + pt2.x + " y2: "
				+ pt2.y;
	}
	
	/*
	 * Method which returns the distance between two lines start and end
	 * points using the distance formula. 
	 * 
	 */

	public double getProximity(Line l) {

		double distancePoint1 = Math.sqrt((pt1.x - l.pt1.x) * (pt1.x - l.pt1.x)
				+ (pt1.y - l.pt1.y) * (pt1.y - l.pt1.y));

		double distancePoint2 = Math.sqrt((pt2.x - l.pt2.x) * (pt2.x - l.pt2.x)
				+ (pt2.y - l.pt2.y) * (pt2.y - l.pt2.y));

		return Math.min(distancePoint1, distancePoint2);

	}

	public double getProximity(Point p) {

		return getProximity(new Line(p, p));
	}

	public double getLength() {
		return Math.sqrt(((pt2.x - pt1.x) * (pt2.x - pt1.x))
				+ ((pt2.y - pt1.y) * (pt2.y - pt1.y)));
	}
	
	// Returns false if the distance is less than 25 pixels
	

	public boolean isNear(Line l) {

		return getProximity(l) < 25;

	}
	
	/*
	 * Method which returns an intersection point object	  
	 * of two lines
	 */

	public Point getIntersectionPoint(Line l) {

		double A1 = getDY();
		double B1 = getDX();
		double C1 = (A1 * pt1.x) + (B1 * pt1.y);

		double A2 = l.getDY();
		double B2 = l.getDX();
		double C2 = (A2 * l.pt1.x) + (B2 * l.pt1.y);

		double det = (A1 * B2) - (A2 * B1);
		double xIntersectionPoint = (B2 * C1 - B1 * C2) / det;
		double yIntersectionPoint = (A1 * C2 - A2 * C1) / det;

		return new Point(xIntersectionPoint, yIntersectionPoint);

	}
	
	/*
	 * Method which takes in two lines and returns a new line object
	 * from the mid line points between the lines
	 * 
	 */
	   

	public Line getMidLinePoints(Line l) {

		double firstMidXPoint = (pt1.x + l.pt1.x) / 2;
		double firstMidYPoint = (pt1.y + l.pt1.y) / 2;

		Point firstMidPoint = new Point(firstMidXPoint, firstMidYPoint);

		double secondMidXPoint = (pt2.x + l.pt2.x) / 2;
		double secondMidYPoint = (pt2.y + l.pt2.y) / 2;

		Point secondMidPoint = new Point(secondMidXPoint, secondMidYPoint);

		return new Line(firstMidPoint, secondMidPoint);

	}
	
	//Line2D is a java.awt class which contains geometry functions
	//which was used to check if a line contains 2 points
	//ptLineDist returns 0 if the line does contain the point

	public boolean isPointOnLine(Point p) {

		return Line2D.ptLineDist(pt1.x, pt1.y, pt2.x, pt2.y, p.x, p.y) != 0;

	}

}
