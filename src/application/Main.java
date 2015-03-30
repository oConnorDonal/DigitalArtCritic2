/*
 * Digtial Art Critic
 * Donal O Connor
 * C11529667
 * 
 * This class set up the primary stage for the GUI.
 * Each primary stage has a scene object which can take a fxml
 * file to load it and css file to style it
 */


package application;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		
		//OpenCV external library being loaded
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);	
		

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"MainView.fxml"));
			Parent root = loader.load();
			MainController controller = loader.getController();
			controller.init(primaryStage);
			Scene scene = new Scene(root, 1100, 600);
			scene.getStylesheets().add(	getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Digital Art Critic");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);

	}
}
