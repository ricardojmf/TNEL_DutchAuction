package gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIMain extends Application {
	@Override
	public void start(Stage primaryStage) {
	       Parent root = null;
	       
			try {
				root = FXMLLoader.load(getClass().getResource("Interface.fxml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
	       
	        Scene scene = new Scene(root);
	    
	        primaryStage.setTitle("Dutch Auction");
	        primaryStage.setResizable(false);
	        primaryStage.setScene(scene);
	        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
