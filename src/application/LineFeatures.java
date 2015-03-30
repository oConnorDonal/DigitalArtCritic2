/*
 * Donal O Connor
 * C11529667
 * 
 * Final Year Project
 * 
 */



package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class LineFeatures {

	Filter filter = new Filter();

	public Mat showLineSegments(Mat image, Mat originalImage) {
		
		
		
		

		Mat lines = new Mat();
		List<Line> pLines = new ArrayList<Line>();
		List<Line> mLines = new ArrayList<Line>();
		List<Line> verticalLines = new ArrayList<Line>();
		List<Line> correctLines = new ArrayList<Line>();
		List<Point> correctPoints = new ArrayList<Point>();

		TreeMap<Double, Point> mappedPoints = new TreeMap<Double, Point>();

		Imgproc.HoughLinesP(image, lines, 1, Math.PI / 180, 100, 50, 10);

		for (int i = 0; i < lines.rows(); i++) {
			double[] val = lines.get(i, 0);
			Point pt1 = new Point();
			Point pt2 = new Point();

			pt1.x = val[0];
			pt1.y = val[1];

			pt2.x = val[2];
			pt2.y = val[3];

			Line temp = new Line(pt1, pt2);

			if (temp.getSlope() != 0)
				if (temp.getDX() != 0) {
					pLines.add(temp);
				} else {
					verticalLines.add(temp);
				}
		}

		for (int i = 0; i < pLines.size(); i++) {
			for (int j = i + 1; j < pLines.size(); j++) {
				if (pLines.get(i).isNear(pLines.get(j))) {
					Line temp = pLines.get(i).getMidLinePoints(pLines.get(j));
					mLines.add(temp);
				}
			}
		}

		for (int m = 0; m < mLines.size(); m++) {

			Imgproc.line(originalImage, mLines.get(m).pt1, mLines.get(m).pt2,
					new Scalar(0, 255, 0), 2);

			for (int n = m + 1; n < mLines.size(); n++) {
				Point tempP = mLines.get(m).getIntersectionPoint(mLines.get(n));
				double dist = mLines.get(m).getProximity(tempP);
				// Imgproc.circle(originalImage, tempP, 5, new
				// Scalar(0,0,255),1);
				mappedPoints.put(dist, tempP);
			}
		}

		if (!mappedPoints.isEmpty()) {
			Point p = mappedPoints.firstEntry().getValue();

			for (int i = 0; i < mLines.size(); i++) {

				if (mLines.get(i).isPointOnLine(p)) {
					Imgproc.line(originalImage, mLines.get(i).pt1,
							mLines.get(i).pt2, new Scalar(255, 0, 0), 2);
					Line correctLine = new Line(mLines.get(i).pt1, p);
					correctLines.add(correctLine);
				}

			}

			Imgproc.circle(originalImage, p, 5, new Scalar(0, 0, 255), -2);

			for (int i = 0; i < correctLines.size(); i++) {
				for (int j = 0; j < verticalLines.size(); j++) {
					Point temp = correctLines.get(i).getIntersectionPoint(
							verticalLines.get(j));
					correctPoints.add(temp);

				}
			}

			for (int i = 0; i < correctPoints.size(); i++) {
				for (int j = i + 1; j < correctPoints.size(); j++) {
					Imgproc.line(originalImage, correctPoints.get(i),
							correctPoints.get(j), new Scalar(0, 255, 0), 2);
				}
			}
		}

		return originalImage;

	}

	public Mat getThirds(Mat inImage) {

		double X = 0;
		double Y = 0;
		double w = inImage.width();
		double h = inImage.height();

		Point topLineStartPoint = new Point(X, h / 3);
		Point topLineEndPoint = new Point(X + w, h / 3);

		Point bottomLineStartPoint = new Point(X, h / 3 + h / 3);
		Point bottomLineEndPoint = new Point(X + w, h / 3 + h / 3);

		Point leftLineStartPoint = new Point(w / 3, Y);
		Point leftLineEndPoint = new Point(w / 3, Y + h);

		Point rightLineStartPoint = new Point(w / 3 + w / 3, Y);
		Point rightLineEndPoint = new Point(w / 3 + w / 3, Y + h);

		Line topLine = new Line(topLineStartPoint, topLineEndPoint);
		Line bottomLine = new Line(bottomLineStartPoint, bottomLineEndPoint);

		Line leftLine = new Line(leftLineStartPoint, leftLineEndPoint);
		Line rightLine = new Line(rightLineStartPoint, rightLineEndPoint);

		Imgproc.line(inImage, topLine.pt1, topLine.pt2, new Scalar(0, 255, 0),
				1);
		Imgproc.line(inImage, bottomLine.pt1, bottomLine.pt2, new Scalar(0,
				255, 0), 1);

		Imgproc.line(inImage, leftLine.pt1, leftLine.pt2,
				new Scalar(0, 255, 0), 1);
		Imgproc.line(inImage, rightLine.pt1, rightLine.pt2, new Scalar(0, 255,
				0), 1);

		return inImage;

	}

	public Mat getHorizonLine(Mat inImage, Mat originalImage) {

		Mat lines = filter.toCanny(inImage);
		Mat hLines = new Mat();

		List<Line> sLines = new ArrayList<Line>();
		List<Scalar> colours = new ArrayList<Scalar>();
		List<Double> dists = new ArrayList<Double>();

		Imgproc.HoughLinesP(lines, hLines, 1, Math.PI / 180, 100, 100, 10);

		for (int i = 0; i < hLines.rows(); i++) {
			double[] val = hLines.get(i, 0);
			Point pt1 = new Point();
			Point pt2 = new Point();

			pt1.x = val[0];
			pt1.y = val[1];

			pt2.x = val[2];
			pt2.y = val[3];

			Line temp = new Line(pt1, pt2);

			Point rectTl = new Point(temp.pt1.x, temp.pt1.y - 20);
			Point rectBr = new Point(temp.pt2.x, temp.pt2.y + 20);

			Rect rect = new Rect(rectTl, rectBr);

			Mat roi = new Mat(inImage, rect);

			for (int k = 0; k < roi.rows(); k++) {
				Scalar tempC = new Scalar(roi.get(k, 0));
				colours.add(tempC);
			}

			for (int l = 0; l < colours.size() - 1; l++) {
				if (colours.get(l).equals(colours.get(l + 1)))
					;
				colours.remove(l + 1);
			}

			for (int x = 0; x < colours.size(); x++) {
				for (int y = 0; y < colours.size(); y++) {
					double[] colVals1 = colours.get(x).val;
					double[] colVals2 = colours.get(y).val;
					double x1 = colVals1[0];
					double y1 = colVals1[1];
					double z1 = colVals1[2];

					double x2 = colVals2[0];
					double y2 = colVals2[1];
					double z2 = colVals2[2];

					double xd = x2 - x1;
					double yd = y2 - y1;
					double zd = z2 - z1;

					double distance = Math.sqrt((xd * xd) + (yd * yd)
							+ (zd * zd));
					dists.add(distance);

					// Imgproc.line(originalImage,temp.pt1, temp.pt2, new
					// Scalar(255,0 ,0 ),1);

				}

			}

			Collections.sort(dists);
			double min = dists.get(0);
			double max = dists.get(dists.size() - 1);
			double range = max - min;
			if (range > 150)
				Imgproc.line(originalImage, temp.pt1, temp.pt2, new Scalar(0,
						0, 255), 1);

			// Imgproc.rectangle(originalImage, rectTl, rectBr, new
			// Scalar(0,0,255));
			// Imgproc.line(originalImage,temp.pt1, temp.pt2, new Scalar(0,0
			// ,255 ),1);

		}

		return originalImage;
	}

}
