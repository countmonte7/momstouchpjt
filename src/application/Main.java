package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {
	
	public Stage window;
	public Parent root;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Font.loadFont(getClass().getResourceAsStream("resources/NanumGothic.ttf"), 14);
			window = primaryStage;
			Scene scene = new Scene(FXMLLoader.load(getClass().getResource("LoginPage.fxml")));
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			window.setScene(scene);
			window.setTitle("맘스터치 주문 앱");
			window.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
