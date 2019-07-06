package app.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class OrderPageController implements Initializable{
 
	@FXML
	private Button showOrderListBtn, goToMenuBtn;
	
	@FXML
	private AnchorPane orderSuccessPane;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public void goMainPage() {
		try {
			Scene mainScene = new Scene(FXMLLoader.load(getClass().getResource("../view/MenuPage.fxml")));
			Stage window = (Stage)orderSuccessPane.getScene().getWindow();
			window.setScene(mainScene);
			window.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void goOrderListPage() {
		
	}
	
}
