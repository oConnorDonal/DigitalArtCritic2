package application;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class FaceFeatures
{
public Mat displayFaceFeatures(Mat inImage){
		
		Mat gray = new Mat();	
		List<Point>eyePoints = new ArrayList<Point>();
		MatOfRect faceBoxes = new MatOfRect();
		MatOfRect eyeBoxes = new MatOfRect();
		MatOfRect mouthBoxes = new MatOfRect();
		MatOfRect noseBoxes = new MatOfRect();
		
		Point startf = null;
		Point endf = null;
		
		Imgproc.cvtColor(inImage, gray,Imgproc.COLOR_BGR2GRAY);
		
		String faceCascadeFile = "src/application/haarcascade_frontalface_alt.xml";
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
			
			if(faces[i] != null)
			{
				
				for(int j = 0; j < eyes.length; j++)
				{	
					//Mat roiForCorners = new Mat(gray,eyes[j]);
					//Point [] eyeCorners = getTcornersP(roiForCorners);
					Point pt1 = new Point(faces[i].tl().x + eyes[j].tl().x,faces[i].tl().y + eyes[j].tl().y);
					//Point pt2 = new Point(pt1.x + eyes[j].width,pt1.y + eyes[j].height);
					Point leye = new Point(pt1.x + 2,pt1.y + eyes[j].height/2);
					//Point leye2 = new Point(leye.x + eyes[j].width-2,leye.y);
					Point reye = new Point(pt1.x + eyes[j].width -2,pt1.y + eyes[j].height/2);
					//Point reye2 = new Point(pt1.x + eyes[j].width -2,pt1.y + eyes[j].height/2);
					double dist = getLength(leye,reye);
					startf = new Point (leye.x - dist,leye.y );					
					endf = new Point (startf.x + dist * 5,leye.y );
					
					eyePoints.add(leye);
					//eyePoints.add(leye2);
					eyePoints.add(reye);
					eyePoints.add(startf);
					eyePoints.add(endf);			
					
						
					//Imgproc.rectangle(inImage, pt1, pt2, new Scalar(255,0,0),1);
					/*for(int k = 0; k < eyeCorners.length; k++)
					{
					
						Point temp = new Point(pt1.x+eyeCorners[k].x,pt1.y + eyeCorners[k].y);
						Imgproc.circle(inImage,temp, 3, new Scalar(0,255,0),-1);
					}*/
				
			
			}
			}
			else{
				System.out.println("Face feature not found");
			}
			if(nose.length != 0)
			{
				
				for(int m = 0; m < nose.length; m++){
					nosePt1 = new Point(faces[i].tl().x + nose[m].tl().x,faces[i].tl().y + nose[m].tl().y);				
					Point pt2 = new Point(nosePt1.x + nose[m].width,nosePt1.y + nose[m].height);
					//Imgproc.rectangle(inImage, nosePt1, pt2, new Scalar(255,255,0),1);
				}
				/*for(int l = 0; l < mouth.length; l++){
					Point pt1 = new Point(faces[i].tl().x + mouth[l].tl().x,faces[i].tl().y + mouth[l].tl().y);
					Point pt2 = new Point(pt1.x + mouth[l].width,pt1.y + mouth[l].height);
					if(pt1.y > nosePt1.y)				
					//Imgproc.rectangle(inImage, pt1, pt2, new Scalar(0,0,0),1);
				}*/
			}
			
			else
			{
				System.out.println("Nose feature not found");
			}			
			
		
		}
		for (int i = 0; i < eyePoints.size();i++){
			Imgproc.circle(inImage, eyePoints.get(i),3, new Scalar(0,255,0),-1);
		}
		
		//Imgproc.line(inImage, startf, endf, new Scalar(255,0,0),1);
		return inImage;
		
		
	}

	public double getLength(Point pt1, Point pt2)
	{
		return Math.sqrt( ((pt2.x - pt1.x) * (pt2.x - pt1.x)) + ((pt2.y - pt1.y) * (pt2.y - pt1.y)) );
	}


}
