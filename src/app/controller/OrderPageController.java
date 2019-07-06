package app.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;



public class OrderPageController implements Initializable{
	
	@FXML
	private Button showOrderListBtn, goToMenuBtn;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public void goMainPage(ActionEvent event) {
		try {
			Scene menuScene = new Scene(FXMLLoader.load(getClass().getResource("../view/MenuPage.fxml")));
			Stage menuWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
			menuWindow.setScene(menuScene);
			menuWindow.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
