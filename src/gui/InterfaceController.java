package gui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import logic.Auction;

public class InterfaceController implements Initializable {
	
	private static final String FAIL_COLOR = "Red";
	private static final String WORKING_COLOR = "Green";
	
	@FXML
    private TextField productsTextField;    
    @FXML
    private TextField agentsTextField;	
    @FXML
    private Label resultsText;
    @FXML
    private Button loadButton;
    
	@FXML
    void chooseProductsFile(ActionEvent event) {
    	chooseFileHandler(true);
    }
    
    @FXML
    void chooseAgentsFile(ActionEvent event) {
    	chooseFileHandler(false);    	
    }
    
    @FXML
    void loadAndRun(ActionEvent event) {
    	boolean debugMode = true;
    	System.out.println(productsTextField.getText() + " " + agentsTextField.getText());
		Auction auction = new Auction(debugMode, productsTextField.getText(), agentsTextField.getText());
		auction.launchJade();
		auction.initializeAuction();
		auction.launchBuyerAgents();
		try {
			System.out.println("Registering buyers...");
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Registering auctioneer...");
		auction.createAuctioneerAgent();
		auction.printProducts();
		auction.printBuyers();
    }
    
    private void chooseFileHandler(boolean isProductsFile) {

    	String type = (isProductsFile? "Products" : "Agents");
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Choose "+type+" File");
    	File file = fileChooser.showOpenDialog(null);
    	
    	validateFile(file,type);
    }
    
    private String validateFile(File file, String type) {
    	String errors = "";
    	String filepath = "";
    	boolean isError = false;
    	
    	if (file != null) {
    		if(file.exists()) {
    			filepath = file.getAbsolutePath();
        		isError = false;
    		}
    		else {
    			errors = type+" file doesn't exist.";
    			isError = true;
    		}
    	}
    	else {
    		errors = "No "+type+" file selected.";
    		isError = true;
    	}
    	
    	handleFileChange(errors, filepath, isError, type);
    	
    	return errors;
    }
    
    private void handleFileChange(String message, String filename, boolean isError, String type) {
		if(isError) {
			resultsText.setTextFill(Paint.valueOf(FAIL_COLOR));			
			if(type.equals("Products"))
				productsTextField.setText("");
			else
				agentsTextField.setText("");
			loadButton.setDisable(true);
		}
		else {
			resultsText.setTextFill(Paint.valueOf(WORKING_COLOR));
			if(type.equals("Products"))
				productsTextField.setText(filename);
			else
				agentsTextField.setText(filename);
			loadButton.setDisable(false);
		}
		
		resultsText.setText(message);
			
	}
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadButton.setDisable(true);		
	}
}
