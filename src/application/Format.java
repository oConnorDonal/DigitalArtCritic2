package application;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Format {
	
	
public BufferedImage matToBuff(Mat matIn){
		
		//System.out.println(" Matto buff");

		
		byte [] data1 = new byte [matIn.rows() * matIn.cols() * (int)matIn.elemSize()];
		matIn.get(0, 0,data1);
		BufferedImage image1 = new BufferedImage(matIn.cols(), matIn.rows(),BufferedImage.TYPE_BYTE_GRAY);
		image1.getRaster().setDataElements(0, 0, matIn.cols(),matIn.rows(), data1);
		return image1;
		
	}


	public BufferedImage matToBuffColour(Mat matIn){
	
	//System.out.println(" Matto buff");

	
	byte [] data1 = new byte [matIn.rows() * matIn.cols() * (int)matIn.elemSize()];
	matIn.get(0, 0,data1);
	BufferedImage image1 = new BufferedImage(matIn.cols(), matIn.rows(),5);
	image1.getRaster().setDataElements(0, 0, matIn.cols(),matIn.rows(), data1);
	return image1;
	
	}
	
	public BufferedImage matToBinBuff(Mat matIn){
		
		//System.out.println(" Matto buff");

		
		byte [] data1 = new byte [matIn.rows() * matIn.cols() * (int)matIn.elemSize()];
		matIn.get(0, 0,data1);
		BufferedImage image1 = new BufferedImage(matIn.cols(), matIn.rows(),BufferedImage.TYPE_BYTE_BINARY);
		image1.getRaster().setDataElements(0, 0, matIn.cols(),matIn.rows(), data1);
		return image1;
		
	}
	
	public Mat toThresh(Mat inImage){
		
		Mat gray = new Mat ();
		Mat outImage = new Mat();
		Imgproc.cvtColor(inImage, gray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.threshold(gray, outImage, 0, 255, Imgproc.THRESH_TOZERO);
		return outImage;
		
		
	}
	
	public Mat dilate (Mat inImage){
		
		int dilationSize = 7;
		
		Mat dilatedImage = new Mat();
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * dilationSize +1,2 * dilationSize +1));
		Imgproc.dilate(inImage, dilatedImage, kernel);
		return dilatedImage;
	}
	
	public Mat buffToMat(BufferedImage image){
		
		//System.out.println("buffto Mat");
		
		byte [] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		Mat originalImageMat  = new Mat(image.getHeight(),image.getWidth(),CvType.CV_8UC3);
		originalImageMat.put(0, 0, data);
		return originalImageMat;
		
	}



}
