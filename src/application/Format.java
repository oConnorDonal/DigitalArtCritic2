/*
 * Digital Art Critic
 * Donal O Connor
 * C11529667
 * 
 * Format Class. The code in this class references code in an online  
 * tutorialsPoint article included in the report.
 */



package application;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class Format {
	
	
	/*
	 * The methods here are converting java buffered image types to OpenCV
	 * mat types. * 
	 * 
	 */
	
	
	/*
	 * matToBuff takes in a mat object and declares a byte array to hold the mat data
	 * A buffered image is set up of the same dimensions as the mat object with a grayscale type.
	 * The buffered image is then set to the mat data and returned
	 * 
	 * The the same methods are reused with different BufferedImage type e.g. binary,colour,grey
	 */
	
	
	
	
public BufferedImage matToBuff(Mat matIn){		
		
		
		byte [] data1 = new byte [matIn.rows() * matIn.cols() * (int)matIn.elemSize()];
		matIn.get(0, 0,data1);
		BufferedImage image1 = new BufferedImage(matIn.cols(), matIn.rows(),BufferedImage.TYPE_BYTE_GRAY);
		image1.getRaster().setDataElements(0, 0, matIn.cols(),matIn.rows(), data1);
		return image1;
		
	}

	
	public BufferedImage matToBuffColour(Mat matIn){	
	
		byte [] data1 = new byte [matIn.rows() * matIn.cols() * (int)matIn.elemSize()];
		matIn.get(0, 0,data1);
		BufferedImage image1 = new BufferedImage(matIn.cols(), matIn.rows(),5);		
		image1.getRaster().setDataElements(0, 0, matIn.cols(),matIn.rows(), data1);
		
		return image1;
	
	}
	
	public BufferedImage matToBinBuff(Mat matIn){
		
		byte [] data1 = new byte [matIn.rows() * matIn.cols() * (int)matIn.elemSize()];
		matIn.get(0, 0,data1);
		BufferedImage image1 = new BufferedImage(matIn.cols(), matIn.rows(),BufferedImage.TYPE_BYTE_BINARY);
		image1.getRaster().setDataElements(0, 0, matIn.cols(),matIn.rows(), data1);
		return image1;
		
	}
	
	//This method thresholds an image with Opencv grayscale and threshold methods
	
	public Mat toThresh(Mat inImage){
		
		Mat gray = new Mat ();
		Mat outImage = new Mat();
		Imgproc.cvtColor(inImage, gray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.threshold(gray, outImage, 127, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C);
		return outImage;		
		
	}	
	
	//This is the same except the process is reversed
	
	public Mat buffToMat(BufferedImage image){			
		
		byte [] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		Mat originalImageMat  = new Mat(image.getHeight(),image.getWidth(),CvType.CV_8UC3);
		originalImageMat.put(0, 0, data);
		return originalImageMat;
		
	}



}
