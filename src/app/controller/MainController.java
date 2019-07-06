package app.controller;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import app.model.UserDAO;
import app.model.UserDTO;
import app.model.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainController implements Initializable{
	
	@FXML 
	private Button loginBtn;
	@FXML 
	private Button signUpPgBtn, btnPrevPage, btnSignUp;
	@FXML
	private TextField idTf, tfNewId, tfname, tfemail;
	@FXML
	private PasswordField pwTf, pfSetpw, pfSetpw2;
	@FXML
	private BorderPane loginPane;
	
	UserDTO dto; 
	
	public String userId;
	
	Alert alert;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	public void loginBtnAction(ActionEvent event) {
		UserDAO dao = new UserDAO();
		String id = idTf.getText();
		String pw = pwTf.getText();
		int result = dao.login(id, pw);
		if(result ==1) {
			UserSession.getInstance().setUserId(id);
			Alert(result);
		}else {
			Alert(result);
		}
	}
	
	public void Alert(int result) {
		if(result == 1) {
			alert = new Alert(AlertType.CONFIRMATION, "로그인 성공", ButtonType.OK);
			alert.setTitle("성공 메시지");
			alert.setHeaderText(null);
			Optional<ButtonType> login = alert.showAndWait();
			if(login.get() == ButtonType.OK) {
				getMenuPage();
			}
		}else if(result==0){
			alert = new Alert(AlertType.ERROR, "비밀번호가 틀립니다.", ButtonType.OK);
			alert.setTitle("실패 메시지");
			alert.setHeaderText(null);
			alert.showAndWait();
		}
	}
	
	private void getMenuPage() {
		try {
			FXMLLoader loader =new FXMLLoader(getClass().getResource("../view/MenuPage.fxml"));
			Parent root;
			root = loader.load();
			Stage stage = (Stage)loginPane.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void signUpPgBtnAction(ActionEvent event) {
		try {
			Parent signUpWindow = FXMLLoader.load(getClass().getResource("../view/SignUpPage.fxml"));
			Scene signUpScene = new Scene(signUpWindow);
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
			window.setScene(signUpScene);
			window.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void btnPrevPageAction(ActionEvent event) {
		try {
			Parent mainWindow = FXMLLoader.load(getClass().getResource("../../application/LoginPage.fxml"));
			Scene loginScene = new Scene(mainWindow);
			
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
			window.setScene(loginScene);
			window.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void  SignUpAction(ActionEvent event) {
		try {
			String userId = tfNewId.getText();
			String userPw = pfSetpw.getText();
			String userPw2 = pfSetpw2.getText();
			String userName = tfname.getText();
			String userEmail = tfemail.getText();
			
			Boolean validCheck = isValid(userId, userPw, userPw2, userName, userEmail);
			if(validCheck) {
				UserDAO dao = new UserDAO();
				int signUpResult = dao.signUp(userId, userPw, userName, userEmail);
				if(signUpResult==1) {
					System.out.println("가입 성공");
				}else if(signUpResult==0) {
					System.out.println("가입 실패. 다시 시도해주세요.");
				}else if(signUpResult==-1) {
					System.out.println("데이터베이스 오류. 관리자에게 문의하세요.");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isValid(String userId, String userpw, String userpw2, String username,String email) {
		if(userId==null||userId.equals("")) {
			System.out.println("아이디를 입력해주세요.");
			return false;
		}else if(userpw== null || userpw.equals("")){
			System.out.println("비밀번호를 입력해주세요.");
			return false;
		}else if(!userpw.equals(userpw2)) {
			System.out.println("비밀번호가 일치하지 않습니다.");
			return false;
		}else if(username==null||username.equals("")) {
			System.out.println("이름을 입력해주세요.");
			return false;
		}else if(email==null||email.equals("")) {
			System.out.println("이메일을 입력해주세요.");
			return false;
		}
		String nameRegex = "^[a-zA-Z가-힣]{1,20}\\s?[a-zA-Z가-힣]{1,20}$";
		if(!username.matches(nameRegex)) {
			System.out.println("올바른 이름이 아닙니다.");
			return false;
		}
		String emailRegex = "^[_a-z0-9-\\.]{3,20}@[a-z0-9-\\.]+\\.[a-z\\.]+$";
		if(!email.matches(emailRegex)) {
			System.out.println("올바르지 않은 이메일 형식입니다.");
			return false;
		}
		
		return true;
	}
}
