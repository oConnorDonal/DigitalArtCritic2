package application;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.opencv.core.Size;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.utils.Converters;

import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.Vec2d;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
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
			image = ImageIO.read( new File("src/application/stars.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
		
		 //BufferedImage image1 = matToBuff(toCanny(buffToMat(image)));		 
		 //BufferedImage image1 = matToBinBuff(toThresh(buffToMat(image)));
		
		//BufferedImage image1 = format.matToBuffColour(  showLineSegments( filter.toCanny((format.buffToMat(image)) ) ,format.buffToMat(image) ))  ;
		
		// BufferedImage image1 = format.matToBinBuff(showLines( format.toThresh(format.buffToMat(image)),format.buffToMat(image)));
		//BufferedImage image1 = format.matToBuff(filter.toCanny(format.buffToMat(image)));
		
		
		//BufferedImage image1 = format.matToBuffColour(getTcorners(format.buffToMat(image),format.buffToMat(image))); 
		
		//BufferedImage image1 = format.matToBuffColour(displayFaceFeatures(format.buffToMat(image))); 
		BufferedImage image1 = format.matToBuff(getOutLine(format.buffToMat(image),format.buffToMat(image) )); 
		
		//BufferedImage image1 = format.matToBuff(format.toThresh(format.buffToMat(image)));
		 
		Image original = SwingFXUtils.toFXImage(image, null);	
		Image convertedImage = SwingFXUtils.toFXImage(image1, null);		 
			
		smallImage.setImage(original);
		bigImage.setImage(convertedImage);
		 

	    }
	
	
	
	
	
	public Mat showLineSegments(Mat image, Mat originalImage){

		
		Mat lines = new Mat();			
		List <Line> pLines = new ArrayList<Line>();
		List <Line> mLines = new ArrayList<Line>();	
		List <Line> verticalLines = new ArrayList<Line>();
		List <Line> correctLines = new ArrayList<Line>();
		List<Point> intersectionPoints = new ArrayList();
		
		TreeMap<Double, Point> mappedPoints = new TreeMap<Double, Point>();	
		TreeMap<Double, Point> correctMappedPoints = new TreeMap<Double, Point>();	
		
		Imgproc.HoughLinesP(image, lines, 1, Math.PI/180,100,50,10);				
		
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
				else
				{
					verticalLines.add(temp);
				}
		}
		
		for(int i = 0; i < pLines.size(); i++)
		{
			for(int j = i+1 ;j < pLines.size(); j++)
			{
				if(pLines.get(i).isNear(pLines.get(j)))
				{					
					Line temp = pLines.get(i).getMidLinePoints(pLines.get(j));
					//System.out.println("temp "+temp.toString());
					mLines.add(temp);					
				}
			}
		}
		for (int m = 0; m < mLines.size();m++)
		{
			//System.out.println("draw "+mLines.get(m).toString());
			Imgproc.line(originalImage, mLines.get(m).pt1, mLines.get(m).pt2, new Scalar(0, 255,0 ),2);	
			
			for(int n = m+1; n < mLines.size(); n++)
			{		
					Point tempP = mLines.get(m).getIntersectionPoint(mLines.get(n));
					double dist = mLines.get(m).getProximity(tempP);
					//Imgproc.circle(originalImage, tempP, 5, new Scalar(0,0,255),1);		
					mappedPoints.put(dist, tempP);								
			}
		}		
		
		Point p = mappedPoints.firstEntry().getValue();			
		
		for(int i = 0; i < mLines.size();i++){
			
			
			if(mLines.get(i).isPointOnLine(p))
			{
				Imgproc.line(originalImage, mLines.get(i).pt1, mLines.get(i).pt2, new Scalar(255, 0,0 ),2);							
			}			
			Line correctLine = new Line(mLines.get(i).pt1,p);	
			correctLines.add(correctLine);
		}		
		
		Imgproc.circle(originalImage, p, 5, new Scalar(0,0,255),1);			
		
		for (int i = 0; i < correctLines.size();i++)
		{
			for(int j = i+1; j < verticalLines.size(); j++)
			{		
					Point temp = correctLines.get(i).getIntersectionPoint(verticalLines.get(j));					
					double dist = getLength(temp, p);
					correctMappedPoints.put(dist, temp);		
			}
		}	
		
		Point cp = correctMappedPoints.firstEntry().getValue();
		//Imgproc.line(originalImage, mLines.get(i).pt1, mLines.get(i).pt2, new Scalar(255, 0,0 ),2);
		
			
		
		return originalImage;
		
	}
	
	
	
	
	public Mat showLines(Mat image,Mat originalImage){
		
		Mat matLines = new Mat();
		
		Imgproc.HoughLines(image, matLines,3,5 *(Math.PI)/180,25);			
		
		List <Line> lines = new ArrayList<Line>();
		List <Line> slines = new ArrayList<Line>();
		List <Integer> linesToRemove = new ArrayList<Integer>();
		
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
		Mat dst = new Mat();
		dst = Mat.zeros(inImage.size(), CvType.CV_32FC1);
		MatOfFloat dstNorm = new MatOfFloat();
		Mat mask = new Mat();
		Mat dstNormScaled = new Mat();
		double thresh  = 200;
		
		Imgproc.cvtColor(inImage, gray,Imgproc.COLOR_BGR2GRAY);			
		Imgproc.cornerHarris(gray, dst, 2, 3, Core.BORDER_DEFAULT);	
		
		Core.normalize(dst, dstNorm,0,255,Core.NORM_MINMAX,CvType.CV_32FC1,mask);
		Core.convertScaleAbs(dstNorm, dstNormScaled);
		
		byte [] data1 = new byte [dstNorm.rows() * dstNorm.cols() * (int)dstNorm.elemSize()];
		
		for(int i = 0; i < dstNorm.rows(); i++)
		{
			for(int j = 0; j < dstNorm.cols(); j++)
			{
				float temp = 0;
				double [] vals = dstNorm.get(j, i);
				//if((int) dstNorm.get(j, i, data1) > thresh)
				vals[i] = temp;
				
				
					System.out.println(temp);
				
				
			}
		}
		
		return originalImage;
		
	}
	
	
	public Point[] getTcorners(Mat inImage /*Mat originalImage*/){
		
		MatOfPoint corners = new MatOfPoint();
		//Mat gray = new Mat();
		Mat param = new Mat();
		double qLevel = 0.1;
		double minDist = 35;
		int blockSize = 3;
		boolean  useHarris = true;
		double k = 0.04;
		
		
		//Imgproc.cvtColor(inImage, gray,Imgproc.COLOR_BGR2GRAY);
		
		Imgproc.goodFeaturesToTrack(inImage, corners, 4, qLevel, minDist,param,blockSize,useHarris,k);
		//Imgproc.goodFeaturesToTrack(image, corners, maxCorners, qualityLevel, minDistance, mask, blockSize, useHarrisDetector, k);
		//Imgproc.goodFeaturesToTrack(gray, corners, blockSize, qLevel, minDist);
		
		Point [] points = corners.toArray();
		
		/*for(int i = 0; i < points.length;i++){
			Imgproc.circle(originalImage,points[i], 3, new Scalar(0,255,0),-1);
		}*/
		
		
		
		return points;
	}
	
	public static double toRadians(float inFloat){
		
		return inFloat *  Math.PI/180.0f;
		
	}
	
	public boolean isEqualPoint(Point pt1,Point pt2){		
		
		return pt1.equals(pt2);
		
	}
	public double getLength(Point pt1, Point pt2)
	{
		return Math.sqrt( ((pt2.x - pt1.x) * (pt2.x - pt1.x)) + ((pt2.y - pt1.y) * (pt2.y - pt1.y)) );
	}
	
	
	public Mat displayFaceFeatures(Mat inImage){
		
		Mat gray = new Mat();		
		MatOfRect faceBoxes = new MatOfRect();
		MatOfRect eyeBoxes = new MatOfRect();
		MatOfRect mouthBoxes = new MatOfRect();
		MatOfRect noseBoxes = new MatOfRect();
		
		Imgproc.cvtColor(inImage, gray,Imgproc.COLOR_BGR2GRAY);
		
		String faceCascadeFile = "src/application/haarcascade_frontalface_default.xml";
		String eyeCascadeFile = "src/application/haarcascade_eye.xml";
		String mouthCascadeFile = "src/application/mouth.xml";
		String noseCascadeFile = "src/application/nose.xml";
		
		CascadeClassifier faceCascade =  new CascadeClassifier(faceCascadeFile);				
		CascadeClassifier eyeCascade =  new CascadeClassifier(eyeCascadeFile);
		CascadeClassifier mouthCascade =  new CascadeClassifier(mouthCascadeFile);
		CascadeClassifier noseCascade =  new CascadeClassifier(noseCascadeFile);
		
		if(faceCascade.empty())
		{
			System.out.println("Cannot load the cascade xml file");
		}
		
		faceCascade.detectMultiScale(gray, faceBoxes);
		
		
		Rect[] faces = faceBoxes.toArray();
		Point nosePt1 = null;
		
		for(int i = 0; i < faces.length; i++){	
			
			Imgproc.rectangle(inImage, faces[i].tl(), faces[i].br(), new Scalar(0,0,255),2);			
			
			
			
			Mat roi = new Mat(gray,faces[i]);			
			eyeCascade.detectMultiScale(roi, eyeBoxes);
			mouthCascade.detectMultiScale(roi, mouthBoxes);
			noseCascade.detectMultiScale(roi, noseBoxes);			
			Rect[] eyes = eyeBoxes.toArray();
			Rect [] mouth = mouthBoxes.toArray();
			Rect [] nose = noseBoxes.toArray();
			
			
				for(int j = 0; j < eyes.length; j++)
				{	
					Mat roiForCorners = new Mat(gray,eyes[j]);
					Point [] eyeCorners = getTcorners(roiForCorners);
					Point pt1 = new Point(faces[i].tl().x + eyes[j].tl().x,faces[i].tl().y + eyes[j].tl().y);
					Point pt2 = new Point(pt1.x + eyes[j].width,pt1.y + eyes[j].height);
						
					Imgproc.rectangle(inImage, pt1, pt2, new Scalar(255,0,0),1);
					for(int k = 0; k < eyeCorners.length; k++){
					
						Point temp = new Point(pt1.x+eyeCorners[k].x,pt1.y + eyeCorners[k].y);
						Imgproc.circle(inImage,temp, 3, new Scalar(0,255,0),-1);
				}
				
			
			}
			
			for(int m = 0; m < nose.length; m++){
				nosePt1 = new Point(faces[i].tl().x + nose[m].tl().x,faces[i].tl().y + nose[m].tl().y);				
				Point pt2 = new Point(nosePt1.x + nose[m].width,nosePt1.y + nose[m].height);
				Imgproc.rectangle(inImage, nosePt1, pt2, new Scalar(255,255,0),1);
			}
			for(int l = 0; l < mouth.length; l++){
				Point pt1 = new Point(faces[i].tl().x + mouth[l].tl().x,faces[i].tl().y + mouth[l].tl().y);
				Point pt2 = new Point(pt1.x + mouth[l].width,pt1.y + mouth[l].height);
				if(pt1.y > nosePt1.y)				
				Imgproc.rectangle(inImage, pt1, pt2, new Scalar(0,0,0),1);
			}
			
			
		
		}
		return inImage;
		
		
	}
	
	public Mat getBlobs(Mat inImage, Mat originalImage)
	{
		
		Mat gray = new Mat();
		Imgproc.cvtColor(inImage, gray,Imgproc.COLOR_BGR2GRAY);
		
		
		return originalImage;
	}
	
	public Mat getOutLine(Mat inImage,Mat originalImage){		
		
		
		return filter.getContours(format.toThresh(inImage),originalImage);
	}
	

	
	
	
		
		
		
		
	
	
	
	
	
	
}
