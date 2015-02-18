package application;
	
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	
	@Override
	public void start(Stage primaryStage) {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		try {
			
			//BufferedImage image  = ImageIO.read(new File("src/application/lines.jpg"));
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));			
			Parent root = loader.load();	
			MainController controller = loader.getController();
			controller.init(primaryStage);
			Scene scene = new Scene(root,1100,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Digital Art Critic");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	 
	
	public static void main(String[] args) {
		launch(args);
	}
}
