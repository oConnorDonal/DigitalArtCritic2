package application;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Filter {
	
	
public Mat toCanny(Mat inImage){
		
		
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
		//Mat gray7 = new Mat();
		//Mat gray8 = new Mat();*/
		//Mat sobel = new Mat();
		
		//Imgproc.GaussianBlur(gray, smoothedImage, new Size(3,3), 0);
		Imgproc.cvtColor(inImage, gray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(gray, smoothedImage, new Size(3, 3));
		Imgproc.blur(smoothedImage, gray2, new Size(3, 3));
		Imgproc.blur(gray2, gray3, new Size(3, 3));
		Imgproc.blur(gray3, gray4, new Size(3, 3));
		Imgproc.blur(gray4, gray5, new Size(3, 3));
		Imgproc.blur(gray5, gray6, new Size(3, 3));
		//Imgproc.blur(gray6, gray7, new Size(3, 3));
		//Imgproc.blur(gray7, gray8, new Size(3, 3));*/
		
		//Mat thresh = format.toThresh(gray);
		Imgproc.Canny(gray6, edges, lowThreshold, lowThreshold * ratio);	
		
		
		//Imgproc.Sobel(edges, sobel, edges.depth(), 0, 1);
		
		return edges;
		
	}


public Mat toSobel(Mat inImage){
	
	Mat dst = new Mat();
	Mat gray = new Mat();
	Imgproc.cvtColor(inImage, gray, Imgproc.COLOR_BGR2GRAY);	
	Imgproc.Sobel(gray, dst, CvType.CV_16S, 1, 0);
	
	/*Mat kernel = new Mat(9,9,CvType.CV_32F)
	{
		{	
			put(0,0,-1);
			put(0,1,0);
			put(0,2,1);
			
			put(1,0,-2);
			put(1,1,0);
			put(1,2,2);
			
			put(2,0,-1);
			put(2,1,0);
			put(2,2,1);
		}
	};
	
	Imgproc.filter2D(gray, dst, -1, kernel);
	*/
	
	return dst;
}

}
