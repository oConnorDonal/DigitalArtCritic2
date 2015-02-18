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
	
	public double getDY(){
		
		double dy = pt2.y - pt1.y;
		return dy; 
		
		
	}
	
	public double getDX(){
		
		double dx = pt2.x - pt1.x;
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
	
	
	
	
	
	
	

}
