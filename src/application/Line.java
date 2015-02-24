package application;

import java.awt.geom.Line2D;

import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Line {
	
	Point pt1;
	Point pt2;
	double rho;
	double theta;
	
	
	public Line(Point pt1, Point pt2, double rho,double theta){
		
		this.pt1 = pt1;
		this.pt2 = pt2;		
		this.rho = rho;
		this.theta = theta;
		
	}
	
	public Line(Point pt1, Point pt2){
		
		this.pt1 = pt1;
		this.pt2 = pt2;			
		
	}
	
	public double getDY(){
		
		double dy = pt2.y - pt1.y;
		return dy; 
		
		
	}
	
	public double getDX(){
		
		double dx = pt1.x - pt2.x;
		return dx; 
		
	}
	
	public double getSlope(){
		
		return this.getDY()/this.getDX();
	}
	
	public void scale(float f) {
		this.pt1.x = this.pt1.x * f;
		this.pt1.y = this.pt1.y * f;
		this.pt2.x = this.pt2.x * f;
		this.pt2.y = this.pt2.y * f;
	}
	
	public String toString() {
		return "x1: " + pt1.x + " y1: " + pt1.y + " x2: " + pt2.x + " y2: " + pt2.y;
	}
	
	public double getProximity (Line l){
		
		double distancePoint1 = Math.sqrt((pt1.x - l.pt1.x) * (pt1.x - l.pt1.x) +
										  (pt1.y - l.pt1.y) * (pt1.y - l.pt1.y));

		double distancePoint2 = Math.sqrt((pt2.x - l.pt2.x) * (pt2.x - l.pt2.x) +
										  (pt2.y - l.pt2.y) * (pt2.y - l.pt2.y));
		 
		 return Math.min(distancePoint1, distancePoint2);				
		
	}
	
	public double getProximity (Point p){
		
		return getProximity(new Line(p,p));
	}
	
	public double getLength()
	{
		return Math.sqrt( ((pt2.x - pt1.x) * (pt2.x - pt1.x)) + ((pt2.y - pt1.y) * (pt2.y - pt1.y)) );
	}
	
	
	public boolean isNear(Line l){
		
		return getProximity(l) < 25;
		
	}
	
	public Point getIntersectionPoint (Line l){
		
		double A1 = getDY();
		double B1 = getDX();
		double C1 = ( A1 * pt1.x) + (B1 * pt1.y);					
		
		double A2 = l.getDY();
		double B2 = l.getDX();
		double C2= ( A2 * l.pt1.x) + (B2 * l.pt1.y);				
						
		double det  = (A1 * B2) - (A2 * B1);				
		double xIntersectionPoint = (B2 * C1 - B1 * C2)/det;
		double yIntersectionPoint = (A1 * C2 - A2 * C1)/det;
		
		return new Point(xIntersectionPoint,yIntersectionPoint);
		
	}
	
	public Line getMidLinePoints(Line l)
	{			
		
		double firstMidXPoint = (pt1.x + l.pt1.x)/2;
		double firstMidYPoint = (pt1.y + l.pt1.y)/2;
		
		Point firstMidPoint = new Point (firstMidXPoint,firstMidYPoint);
		
		double secondMidXPoint = (pt2.x + l.pt2.x)/2;
		double secondMidYPoint = (pt2.y + l.pt2.y)/2;
		
		Point secondMidPoint = new Point (secondMidXPoint,secondMidYPoint);		
		
		return new Line(firstMidPoint,secondMidPoint);
			
	}
	
	public boolean isPointOnLine(Point p){
		
		return Line2D.ptLineDist(pt1.x, pt1.y, pt2.x, pt2.y, p.x, p.y) != 0;
		
	}
	
	
	
	

}
