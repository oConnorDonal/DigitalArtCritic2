package application;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import org.opencv.core.Size;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import com.sun.javafx.geom.Vec2d;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController {
	
	
	
	@FXML
    private BorderPane borderPane;
	
	@FXML
	private ImageView smallImage;
	
	
	@FXML
	private ImageView bigImage;
	
	//private BufferedImage image; 
	
	private Stage stage;
	
	//sends primary stage through form the main so controller can use it and refer to the same stage
	
	public void init(Stage primaryStage) {
		
		this.stage = primaryStage;
		//this.image = image;
	}
	
	Core core = new Core();
	Format format = new Format();
	Filter filter = new Filter();
	
	
	BufferedImage image = null;
	
	
	@FXML
	   public void handleFileOpen(ActionEvent event) throws IOException  {
		
		
		/* FileChooser fileChooser = new FileChooser();		 
		 fileChooser.setTitle("Open Image File");			
		 File file = fileChooser.showOpenDialog(stage);		 
		 BufferedImage image = ImageIO.read(file);		*/
		
		try {
			image = ImageIO.read( new File("src/application/house.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
		 
		 //BufferedImage image1 = matToBuff(toCanny(buffToMat(image)));		 
		 //BufferedImage image1 = matToBinBuff(toThresh(buffToMat(image)));		
		BufferedImage image1 = format.matToBuffColour(  showLineSegments( filter.toCanny((format.buffToMat(image)) ) ,format.buffToMat(image) ))   ;
		// BufferedImage image1 = format.matToBinBuff(showLines( format.toThresh(format.buffToMat(image)),format.buffToMat(image)));
		//BufferedImage image1 = format.matToBuff(toCanny(format.buffToMat(image)));
		//BufferedImage image1 = format.matToBuff(getTcorners(format.buffToMat(image),format.buffToMat(image))); 
		//BufferedImage image1 = matToBinBuff(toThresh(buffToMat(image)));
		 
		Image original = SwingFXUtils.toFXImage(image, null);	
		Image convertedImage = SwingFXUtils.toFXImage(image1, null);		 
			
		smallImage.setImage(original);
		bigImage.setImage(convertedImage);
		 

	    }
	
	
	
	
	
	public Mat showLineSegments(Mat image, Mat originalImage){

		
		Mat lines = new Mat();			
		List <Line> pLines = new ArrayList();
		List <Line> mLines = new ArrayList();
		List <Point> points = new ArrayList(); 
		List <Double> diffs = new ArrayList();
		
		Imgproc.HoughLinesP(image, lines, 1, Math.PI/180,50,50,10);				
		
		for(int i = 0; i < lines.rows(); i++)
		{			
			double [] val  = lines.get(i,0);
			Point pt1 = new Point();
			Point pt2 = new Point();
			
			pt1.x = val[0];
			pt1.y = val[1];
			
			pt2.x = val[2];
			pt2.y = val[3];	
			
			Line temp = new Line(pt1,pt2);			
			
			if(temp.getSlope() != 0)
				if(temp.getDX() != 0)
				{			
					pLines.add(temp);						
				}
		}
		
		for(int j = 0; j < pLines.size(); j++)
		{
			for(int k = 1 ; k < pLines.size(); k++)
			{
				if(j != k)					
				if(Line.isNear(pLines.get(j), pLines.get(k)))
				{
					Line temp = Line.getMidLinePoints(pLines.get(j), pLines.get(k));
					mLines.add(temp);					
				}
			}
		}
		for (int m = 0; m < mLines.size();m++)
		{
			for(int n = 1; n < mLines.size(); n++)
			{
				Imgproc.line(originalImage, mLines.get(m).pt1, mLines.get(m).pt2, new Scalar(0, 255,0 ),2);	
				Point tempP = Line.getIntersectionPoints(mLines.get(m), pLines.get(n));
				points.add(tempP);
				Imgproc.circle(originalImage, tempP, 5, new Scalar(0,0,255),-1);
			}
		}
		for (int x = 0; x < mLines.size();x++)
		{
			for(int y = 0; y < points.size(); y++)
			{
				double difference = Line.getProximity(mLines.get(x).pt1, points.get(y));
				diffs.add(difference);
			}
		}
		for (int z = 0; z < diffs.size(); z++){
			
			double maxVal = diffs.indexOf(Collections.max(diffs));
			
			if(diffs.get(z) > (maxVal/2))
			{
				//do something
			}
		}
		
		return originalImage;
		
	}
	
	
	
	
	public Mat showLines(Mat image,Mat originalImage){
		
		Mat matLines = new Mat();
		
		Imgproc.HoughLines(image, matLines,3,5 *(Math.PI)/180,25);		
		
		
		List <Line> lines = new ArrayList();
		List <Line> slines = new ArrayList();
		List <Integer> linesToRemove = new ArrayList();
		
		int count = 0;
		double oldRho = 0;
		double oldTheta = 0;
		
		
		for(int i = 0; i < matLines.cols(); i++)
		{
			for (int j = 0; j < matLines.rows(); j++) 
			{
			Point pt1 = new Point();
			Point pt2 = new Point();
			double val [] = matLines.get(j, i);	
			
			double rho = val [0];
			double theta = val [1];
			System.out.println(val[0]);
			
			
			double a = Math.cos(theta);
			double b = Math.sin(theta);
			double x0 = a * rho;
			double y0 = b * rho;	
			
			pt1.x = Math.round(x0 + 1000 * (-b));			
			pt1.y = Math.round(y0 + 1000 * (a));
			pt2.x = Math.round(x0 - 1000 * (-b));
			pt2.y = Math.round(y0 - 1000 * (a));
			
			if(!isSimilar(rho,  oldRho, 70))
			{
				if(!isSimilar(theta,  oldTheta, 1))
				{
					lines.add(new Line(pt1,pt2,rho,theta));
					oldRho = rho;
					oldTheta = theta;
				}
			}
			
			
			
			if(i < 100)
				slines.add(new Line(pt1,pt2,rho,theta));
			
			
			
		}
	}
			
			

			
			
		
		
		
		/*for(int i = 0; i < lines.size(); i ++)
		{	
			for(int j = 0; j < lines.size(); j++)
			{
		
				double A1 = lines.get(i).getDY();
				double B1 = lines.get(i).getDX();
				double C1 = ( A1 * lines.get(i).pt1.x) + (B1 * lines.get(i).pt1.y);				
				
				double A2 = lines.get(j).getDY();
				double B2 = lines.get(j).getDX();
				double C2= ( A2 * lines.get(j).pt1.x) + (B2 * lines.get(j).pt1.y);			
				
								
				double det  = (A1 * B2) - (A2 * B1);				
				double xPoint = (B2 * C1 - B1 * C2)/det;
				double yPoint = (A1 * C2 - A2 * C1)/det;	
				
				System.out.println(xPoint);
				System.out.println(yPoint);
				Imgproc.circle(originalImage, new Point(xPoint,yPoint), 5, new Scalar(0,0,255),2 );
			}*/
		
		
		
		for(int i = 0; i < slines.size(); i ++)
		{	
			for(int j = 0; j < lines.size(); j++)
			{
				//if(i != j)
				if(isSimilar(slines.get(i).theta, lines.get(j).theta, 2))
				{
					
					//System.out.println("lines at" + i + " and " + j + "have sim thetas");
					if(isSimilar(slines.get(i).rho, lines.get(j).rho, 20))
					{
						//linesToRemove.add(j);
						System.out.println("lines at" + i + " and " + j + "have sim rhos");
					}
				}
				
			}
		}
		
		int lTRindex = 0;
		System.out.println(lines.size());
		
		HashSet<Integer> s = new HashSet<Integer>();
		s.addAll(linesToRemove);
		linesToRemove.clear();
		linesToRemove.addAll(s);
		
		System.out.println(lines.size());
		for(int i = 0; i < linesToRemove.size(); i++)
		{
			System.out.println("removing line at index " + linesToRemove.get(lTRindex));
			//lines.remove((int)linesToRemove.get(lTRindex));
			
			lines.set((int)linesToRemove.get(lTRindex), null);
			
			lTRindex ++;
			
			if(lTRindex > linesToRemove.size())
			{
				//break;
			}
		}
		
		lines.removeAll(Collections.singleton(null));
		
		System.out.println(lines.size());
		
		
		for(int i = 0 ; i < lines.size(); i++)
		{
			//lines.get(i).scale(1000);
			Imgproc.line(originalImage, lines.get(i).pt1, lines.get(i).pt2, new Scalar(255, 0,0 ), 1);	
		}
		for(int i = 0 ; i < slines.size(); i++)
		{
			//lines.get(i).scale(1000);
			//Imgproc.line(originalImage, slines.get(i).pt1, slines.get(i).pt2, new Scalar(0, 255,0 ), 1);	
		}
		
		return originalImage;
	

	}
	
	
	
	
	
	public void printIntersection(){
		
		Point pt1 = new Point();
		Point pt2 = new Point();
		Point pt3 = new Point();
		Point pt4 = new Point();
		
		pt1.x = -876.0;
		pt1.y = -484.0;
		
		pt2.x = 857.0;
		pt2.y = 516.0;
		
		pt3.x = -691.0;
		pt3.y = 772.0;
		
		pt4.x = 986.0;
		pt4.y = -317.0;
		
		double A1 = pt2.y - pt1.y;
		double B1 = pt1.x - pt2.x;		
		double C1 = (A1*pt1.x) + (B1 * pt1.y);
		
		double A2 = pt4.y - pt3.y;
		double B2 = pt3.x - pt4.x;		
		double C2 = (A2*pt3.x) + (B2 * pt3.y);
		
		double det = (A1*B2) - (A2*B1);		
		
		double x = (B2*C1 - B1*C2)/det;
		double y = (A1*C2 - A2*C1)/det;
		
		System.out.println(x);
		System.out.println(y);
		
		
		
	}
	
	public void slopeIntercept()
	{
		Point pt1 = new Point(27,18);
		Point pt2 = new Point(20,3);
		
		double m = (pt2.y - pt1.y) /( pt2.x - pt1.x);
		
		//y=mx+b  where b is the intercept
		
		double b =  pt2.y- (m *pt2.x) ;
		
		//System.out.println(b);
	}
	
	public boolean isSimilar(double val1,double val2, double range){
		
		return Math.abs(val1 - val2 ) < range;
		
	}
	
	public Mat getCorners (Mat inImage, Mat originalImage){
		
		Mat gray  = new Mat();
		//Mat outImage = new Mat();
		
		Mat dst = Mat.zeros(inImage.size(),CvType.CV_32FC1);	
		
		Mat dstNorm = new Mat();
		Mat mask = new Mat();
		Mat dstNormScaled = new Mat();
		
		Imgproc.cvtColor(inImage, gray,Imgproc.COLOR_BGR2GRAY);		
		
		Imgproc.cornerHarris(gray, dst, 2, 3, Core.BORDER_DEFAULT);
		Core.normalize(dst, dstNorm,0,255,Core.NORM_MINMAX,CvType.CV_32FC1,mask);
		//Core.convertScaleAbs(dstNorm, dstNormScaled);
		
		//dstNorm.
		
		for(int i = 0; i < dstNorm.rows(); i++)
			for(int j = 0; j < dstNorm.cols(); j++ )
			{
				//if(dstNorm)
				//double[] points = dstNorm.get(j, i);
				//int p = (int) points[0];
				//double [] p = dst.get(i,j);
				//System.out.println(p);
				
				
				//if(dstNorm.g > 200)
			//	if(p > 200)
				//{
					
					//Imgproc.circle(originalImage, new Point(i,j), 3, new Scalar(255,0,0));
				//}
				
			}		
		
		
		return originalImage;
		
	}
	
	
	public Mat getTcorners(Mat inImage, Mat originalImage){
		
		MatOfPoint corners = new MatOfPoint();
		Mat gray = new Mat();
		Mat param = new Mat();
		double qLevel = 0.01;
		double minDist = 10;
		int blockSize = 3;
		boolean  useHarris = false;
		double k = 0.04;
		
		
		Imgproc.cvtColor(inImage, gray,Imgproc.COLOR_BGR2GRAY);
		
		Imgproc.goodFeaturesToTrack(gray, corners, 23, qLevel, minDist,param,blockSize,useHarris,k);
		
		Point [] points = corners.toArray();
		
		for(int i = 0; i < points.length;i++){
			Imgproc.circle(originalImage,points[i], 3, new Scalar(255,0,0));
		}
		
		
		
		return originalImage;
	}
	
	public static double toRadians(float inFloat){
		
		return inFloat *  Math.PI/180.0f;
		
	}
	
	public boolean isEqualPoint(Point pt1,Point pt2){		
		
		return pt1.equals(pt2);
		
	}
	
	
		
		
		
		
	
	
	
	
	
	
}
