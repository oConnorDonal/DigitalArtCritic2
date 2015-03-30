/*
 * C11529667
 * Donal O Connor
 * 
 * MainController Class
 * 
 */



package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController {
	
	//GUI components are referencing in the fxml file
	
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
	
	private Stage stage;
	
	//sends primary stage through from the main class so controller can use it and refer to the same stage
	
	public void init(Stage primaryStage) {
		
		this.stage = primaryStage;		
	}
	
	
	
	Core core = new Core();
	Format format = new Format();
	Filter filter = new Filter();
	Corners corners = new Corners();
	FaceFeatures face = new FaceFeatures();
	LineFeatures lFeature = new LineFeatures();
	
	boolean vpButtonBool = true;
	boolean fButtonBool = true;
	boolean hLineButtonBool = true;
	
	
	BufferedImage image = null;
	
	/*
	 * 
	 */
	
	
	
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
		
		if(vpButtonBool == true)
		{
			BufferedImage image1 = format.matToBuffColour( lFeature.showLineSegments( filter.toCanny((format.buffToMat(image)) ) ,format.buffToMat(image) ))  ;		
			//BufferedImage image1 = format.matToBuffColour(corners.getTcorners(format.buffToMat(image),format.buffToMat(image)));
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
			
			vpButtonBool = false;
		}
		else
		{
			Image originalImage = SwingFXUtils.toFXImage(image, null);
			bigImage.setImage(originalImage);
			vpButtonBool = true;
		}
		
		
		
			
	}
	
	@FXML
	public void getROT(ActionEvent event)throws IOException{
		
		if(hLineButtonBool == true)
		{
			BufferedImage image1 = format.matToBuffColour
								(lFeature.getHorizonLine(format.buffToMat(image),format.buffToMat(image)));
			
								Image convertedImage = SwingFXUtils.toFXImage(image1, null);		
								bigImage.setImage(convertedImage);
								hLineButtonBool = false;
		}
		else
		{
			Image originalImage = SwingFXUtils.toFXImage(image, null);
			bigImage.setImage(originalImage);
			hLineButtonBool = true;			
		}
	}
	
	@FXML
	public void getPorportions(ActionEvent event)throws IOException{
		
		if(fButtonBool == true)
		{
			BufferedImage image1 = format.matToBuffColour(face.displayFaceFeatures(format.buffToMat(image)));
			//BufferedImage image1 = format.matToBuffColour(getOutLine(format.toThresh(format.buffToMat(image)),format.buffToMat(image) )); 
			Image convertedImage = SwingFXUtils.toFXImage(image1, null);		
			bigImage.setImage(convertedImage);
			fButtonBool = false;
		}
		else
		{
			Image originalImage = SwingFXUtils.toFXImage(image, null);
			bigImage.setImage(originalImage);
			vpButtonBool = true;
			
		}
		
		
	}
	@FXML
	public void finishTutorial(ActionEvent event)throws IOException{
		
		
		
	}
	
	
	
	
	public boolean isEqualPoint(Point pt1,Point pt2){		
		
		return pt1.equals(pt2);
		
	}
	public Mat getOutLine(Mat inImage,Mat originalImage){		
		
		
		return filter.getContours(inImage,originalImage);
	}
	
	
}
