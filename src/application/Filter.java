package application;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Filter {

	public Mat toCanny(Mat inImage) {

		int lowThreshold = 5;
		int ratio = 3;

		Mat edges = new Mat();
		Mat smoothedImage = new Mat();
		Mat gray = new Mat();
		Mat gray2 = new Mat();
		Mat gray3 = new Mat();
		Mat gray4 = new Mat();
		Mat gray5 = new Mat();
		Mat gray6 = new Mat();

		Imgproc.cvtColor(inImage, gray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(gray, smoothedImage, new Size(3, 3));
		Imgproc.blur(smoothedImage, gray2, new Size(3, 3));
		Imgproc.blur(gray2, gray3, new Size(3, 3));
		Imgproc.blur(gray3, gray4, new Size(3, 3));
		Imgproc.blur(gray4, gray5, new Size(3, 3));
		Imgproc.blur(gray5, gray6, new Size(3, 3));

		Imgproc.Canny(gray6, edges, lowThreshold, lowThreshold * ratio);

		return edges;

	}

	public Mat toSobel(Mat inImage) {

		Mat dst = new Mat();
		Mat gray = new Mat();
		Imgproc.cvtColor(inImage, gray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.Sobel(gray, dst, CvType.CV_16S, 1, 0);

		/*
		 * Mat kernel = new Mat(9,9,CvType.CV_32F) { { put(0,0,-1); put(0,1,0);
		 * put(0,2,1);
		 * 
		 * put(1,0,-2); put(1,1,0); put(1,2,2);
		 * 
		 * put(2,0,-1); put(2,1,0); put(2,2,1); } };
		 * 
		 * Imgproc.filter2D(gray, dst, -1, kernel);
		 */

		return dst;
	}
	
	public Mat getContours(Mat inImage, Mat originalImage){
		
		List<MatOfPoint>contours = new ArrayList<MatOfPoint>();		
		
		Imgproc.findContours(inImage, contours, new Mat(),Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
		
		for (int i = 0; i < contours.size(); i++){
			
			Imgproc.drawContours(originalImage, contours, i, new Scalar(0,0,255));
		}
		
		return originalImage;
	}

}
