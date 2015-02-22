package application;

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
	
	public static double getProximity (Point pt1,Point pt2){
		
		 double distance = Math.sqrt(pt2.x - pt1.x) * (pt2.x - pt1.x)
				 			+ (pt2.y - pt1.y) *  (pt2.y - pt1.y);
		 
		 //System.out.println(Math.abs(distance));
				 
		 return distance;				
		
	}
	
	public static  boolean isNear(Line line1, Line line2){
		
		return getProximity(line1.pt1, line2.pt1) < 100;
		
	}
	
	public static Point getIntersectionPoints (Line line1, Line line2){
		
		double A1 = line1.getDY();
		double B1 = line1.getDX();
		double C1 = ( A1 * line1.pt1.x) + (B1 * line1.pt1.y);					
		
		double A2 = line2.getDY();
		double B2 = line2.getDX();
		double C2= ( A2 * line2.pt1.x) + (B2 * line2.pt1.y);				
						
		double det  = (A1 * B2) - (A2 * B1);				
		double xIntersectionPoint = (B2 * C1 - B1 * C2)/det;
		double yIntersectionPoint = (A1 * C2 - A2 * C1)/det;
		
		return new Point(xIntersectionPoint,yIntersectionPoint);
		
	}
	
	public static Line getMidLinePoints(Line line1, Line line2)
	{			
		
		double firstMidXPoint = (line1.pt1.x + line2.pt1.x)/2;
		double firstMidYPoint = (line1.pt1.y + line2.pt1.y)/2;
		
		Point firstMidPoint = new Point (firstMidXPoint,firstMidYPoint);
		
		double secondMidXPoint = (line1.pt2.x + line2.pt2.x)/2;
		double secondMidYPoint = (line1.pt2.y + line2.pt2.y)/2;
		
		Point secondMidPoint = new Point (secondMidXPoint,secondMidYPoint);		
		
		return new Line(firstMidPoint,secondMidPoint);
			
	}
	
	
	
	
	
	
	

}
