package application;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Corners {
	public Point[] getTcornersP(Mat inImage) {

		MatOfPoint corners = new MatOfPoint();
		Mat gray = new Mat();
		Mat param = new Mat();
		double qLevel = 0.1;
		double minDist = 10;
		int blockSize = 3;
		boolean useHarris = true;
		double k = 0.04;

		Imgproc.cvtColor(inImage, gray, Imgproc.COLOR_BGR2GRAY);

		Imgproc.goodFeaturesToTrack(gray, corners, 10, qLevel, minDist, param,
				blockSize, useHarris, k);
		Point[] points = corners.toArray();

		return points;
	}

	public Mat getTcorners(Mat inImage, Mat originalImage) {

		MatOfPoint corners = new MatOfPoint();
		Mat gray = new Mat();
		Mat param = new Mat();
		double qLevel = 0.1;
		double minDist = 10;
		int blockSize = 3;
		boolean useHarris = true;
		double k = 0.04;

		Imgproc.cvtColor(inImage, gray, Imgproc.COLOR_BGR2GRAY);

		Imgproc.goodFeaturesToTrack(gray, corners, 200, qLevel, minDist, param,
				blockSize, useHarris, k);

		Point[] points = corners.toArray();

		for (int i = 0; i < points.length; i++) {
			Imgproc.circle(originalImage, points[i], 3, new Scalar(0, 255, 0),
					-1);
		}

		return originalImage;
	}

}
