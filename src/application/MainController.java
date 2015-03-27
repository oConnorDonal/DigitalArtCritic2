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
import org.opencv.video.BackgroundSubtractor;

import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.Vec2d;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController {
	
	
	
	@FXML
    private BorderPane borderPane;
	
	@FXML
    private TextArea textArea;
	
	@FXML
	private ImageView smallImage;
	
	@FXML
	private ImageView tutorialImage;
	
	private String string = "Lorem ipsum dolor sit amet, consectetur adipiscing elit,"
			+ " sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
			+ "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
			+ " Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
			+ " Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
	
	
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
	FaceFeatures face = new FaceFeatures();
	LineFeatures lFeature = new LineFeatures();
	
	boolean vpButton = true;
	
	
	BufferedImage image = null;
	
	
	
	@FXML
	   public void handleFileOpen(ActionEvent event) throws IOException  {
		
		
		 FileChooser fileChooser = new FileChooser();		 
		 fileChooser.setTitle("Open Image File");			
		 File file = fileChooser.showOpenDialog(stage);		 
		 image = ImageIO.read(file);		
		
		 Image original = SwingFXUtils.toFXImage(image, null);	
		 Image enlargedImage = SwingFXUtils.toFXImage(image, null);		 
				
		 smallImage.setImage(original);
		 bigImage.setImage(enlargedImage);
			 

	  }	
		
	//BufferedImage image1 = format.matToBuffColour(getTcorners(format.buffToMat(image),format.buffToMat(image))); 
	//BufferedImage image1 = format.matToBuffColour(getOutLine(format.toThresh(format.buffToMat(image)),format.buffToMat(image) )); 
	//BufferedImage image1 = format.matToBuffColour(getThirds(format.buffToMat(image)));
		
		
		
		
		
		
		 
		
	
	@FXML
	public void getVp(ActionEvent event)throws IOException{
		
		BufferedImage tut = null;
		
		if(vpButton == true)
		{
			BufferedImage image1 = format.matToBuffColour( lFeature.showLineSegments( filter.toCanny((format.buffToMat(image)) ) ,format.buffToMat(image) ))  ;		
			Image convertedImage = SwingFXUtils.toFXImage(image1, null);//did have image1			
			bigImage.setImage(convertedImage);
			
			try 
			{
				 tut = ImageIO.read( new File("src/application/vpTutorial.jpg"));
			} 
			catch (IOException e) 
			{			
				e.printStackTrace();
			}		
			
			Image tutorialImageC  = SwingFXUtils.toFXImage(tut, null);  
			tutorialImage.setImage(tutorialImageC);
			textArea.setText(string);
			
			vpButton = false;
		}
		else
		{
			Image originalImage = SwingFXUtils.toFXImage(image, null);
			bigImage.setImage(originalImage);
			vpButton = true;
		}
		
		
		
			
	}
	@FXML
	public void getROT(ActionEvent event)throws IOException{
		
		BufferedImage image1 = format.matToBuffColour(lFeature.getHorizonLine(format.buffToMat(image),format.buffToMat(image)));
		
		System.out.println("hey");
		
	}
	@FXML
	public void getPorportions(ActionEvent event)throws IOException{
		
		BufferedImage image1 = format.matToBuffColour(face.displayFaceFeatures(format.buffToMat(image)));
		
		System.out.println("hey");
		
	}
	@FXML
	public void finishTutorial(ActionEvent event)throws IOException{
		
		System.out.println("hey");
		
	}
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static double toRadians(float inFloat){
		
		return inFloat *  Math.PI/180.0f;
		
	}
	
	public boolean isEqualPoint(Point pt1,Point pt2){		
		
		return pt1.equals(pt2);
		
	}
	
	
	
	
	
	
	
	public Mat getOutLine(Mat inImage,Mat originalImage){		
		
		
		return filter.getContours(inImage,originalImage);
	}
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
