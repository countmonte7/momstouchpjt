package app.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

public class ManagerMainController implements Initializable{

	@FXML ImageView employeeManager, orderManager, menuManager, issueManager;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		employeeManager.setOnMouseClicked(new EventHandler() {
			@Override
			public void handle(Event event) {
				System.out.println("직원 관리");
			}
		});
	}
	
	
	
}
