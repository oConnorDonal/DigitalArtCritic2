/*
 * Digital Art Critic
 * Donal O Connor
 * C11529667
 * 
 * This class contains custom methods which contain common Opencv functions
 * 
 */



package application;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Filter {
	
	
	/*
	 * toCanny takes in a mat image and turns it into a grayscale image
	 * it then passes it through a blurring filter several times which reduces
	 * the visual noise. It then goes through the canny edge detection and is 
	 * returned.
	 * 
	 * The canny parameters are usually a 1:3 low - high threshold standard
	 */

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
	
	/*
	 * getContours is a custom method which takes in two mat images.
	 * the inImage is used for the processing and the originalImage is used
	 * for the drawfunctions which is then returned
	 * the findContours is an Opencv method which returns contours in the form 
	 * of matrices of points which are then stored in a list
	 * 
	 * A loop goes through the contours and draws a box around them.
	 */

	public Mat getContours(Mat inImage, Mat originalImage) {

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Rect boundingRect = new Rect();

		Imgproc.findContours(inImage, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);

		for (int i = 0; i < contours.size(); i++) 
		{
			Imgproc.drawContours(originalImage, contours, i, new Scalar(0, 0,255));
			boundingRect = Imgproc.boundingRect(contours.get(i));
		}

		Imgproc.rectangle(originalImage, boundingRect.tl(), boundingRect.br(),new Scalar(0, 255, 0));

		return originalImage;
	}

}
