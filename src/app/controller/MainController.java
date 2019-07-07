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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainController implements Initializable{
	
	@FXML 
	private Button loginBtn;
	@FXML 
	private Button signUpPgBtn, btnPrevPage, btnSignUp;
	@FXML
	private TextField idTf, tfNewId, tfname, tfemail, tfaddress;
	@FXML
	private PasswordField pwTf, pfSetpw, pfSetpw2;
	@FXML
	private BorderPane loginPane;
	@FXML
	private GridPane signUpPagePane;
	
	UserDTO dto; 
	
	public String userId;
	
	Alert alert;
	
	Stage window;
	
	String alertTitle, alertType, alertContent;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	//로그인 버튼 액션
	public void loginBtnAction(ActionEvent event) {
		UserDAO dao = new UserDAO();
		String id = idTf.getText();
		String pw = pwTf.getText();
		int result = dao.login(id, pw);
		if(id.equals("") || id == null) {
			alertTitle = "로그인 오류";
			alertType = "warning";
			alertContent = "아이디를 입력해주세요.";
			showAlert(alertType, alertTitle, alertContent);
		}else if(pw.equals("") || pw == null){
			alertTitle = "로그인 오류";
			alertType = "warning";
			alertContent = "패스워드를 입력해주세요.";
			showAlert(alertType, alertTitle, alertContent);
		}else {
			if(result ==1) {
				UserSession.getInstance().setUserId(id);
				alertTitle = "로그인 성공";
				alertType = "confirmation";
				alertContent = id + " 님 환영합니다.";
				showAlert(alertType, alertTitle, alertContent);
				getMenuPage(event);
			}else if(result==0) {
				alertTitle = "로그인 오류";
				alertType = "warning";
				alertContent = "비밀번호가 일치하지 않습니다.";
				showAlert(alertType, alertTitle, alertContent);
			}else if(result==-1) {
				alertTitle = "로그인 오류";
				alertType = "warning";
				alertContent = "아이디를 확인해주세요.";
				showAlert(alertType, alertTitle, alertContent);
			}
		}
	}
	
	//메뉴 페이지 불러오는 메소드
	private void getMenuPage(ActionEvent event) {
		try {
			Scene menuScene =new Scene(FXMLLoader.load(getClass().getResource("../view/MenuPage.fxml")));
			menuScene.getStylesheets().add(getClass().getResource("../../application/application.css").toExternalForm());
			window = (Stage)((Node)(event.getSource())).getScene().getWindow();
			window.setScene(menuScene);
			window.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//회원가입 페이지로 이동
	@FXML
	private void signUpPgBtnAction(ActionEvent event) {
		try {
			Scene signUpScene = new Scene(FXMLLoader.load(getClass().getResource("../view/SignUpPage.fxml")));
			signUpScene.getStylesheets().add(getClass().getResource("../../application/application.css").toExternalForm());
			window = (Stage)((Node)event.getSource()).getScene().getWindow();
			window.setScene(signUpScene);
			window.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//이전 페이지(로그인 페이지)로 이동
	@FXML
	private void btnPrevPageAction(ActionEvent event) {
		try {
			Scene loginScene = new Scene(FXMLLoader.load(getClass().getResource("../../application/LoginPage.fxml")));
			loginScene.getStylesheets().add(getClass().getResource("../../application/application.css").toExternalForm());
			window = (Stage)(signUpPagePane.getScene().getWindow());
			window.setScene(loginScene);
			window.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//회원가입 버튼 액션
	@FXML
	private void  SignUpAction(ActionEvent event) {
		try {
			String userId = tfNewId.getText();
			String userPw = pfSetpw.getText();
			String userPw2 = pfSetpw2.getText();
			String userName = tfname.getText();
			String userEmail = tfemail.getText();
			String userAddress = tfaddress.getText();
			
			Boolean validCheck = isValid(userId, userPw, userPw2, userName, userEmail, userAddress);
			if(validCheck) {
				UserDAO dao = new UserDAO();
				int signUpResult = dao.signUp(userId, userPw, userName, userEmail, userAddress);
				if(signUpResult==1) {
					alertTitle = "성공 메시지";
					alertType = "confirmation";
					alertContent = "회원가입에 성공했습니다. " + userId + " 님 환영합니다.";
					showAlert(alertType, alertTitle, alertContent);
					getMenuPage(event);
				}else if(signUpResult==0) {
					alertTitle = "실패 메시지";
					alertType = "warning";
					alertContent = "회원가입에 실패했습니다. 다시 시도해주세요.";
					showAlert(alertType, alertTitle, alertContent);
				}else if(signUpResult==-1) {
					alertTitle = "실패 메시지";
					alertType = "warning";
					alertContent = "데이터베이스 오류. 관리자에게 문의하세요.";
					showAlert(alertType, alertTitle, alertContent);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//회원가입 유효성 검사 메소드
	public boolean isValid(String userId, String userpw, String userpw2, String username,String email, String Address) {
		if(userId==null || userId.equals("")) {
			alertTitle = "회원가입 오류";
			alertType = "warning";
			alertContent = "아이디를 입력해주세요.";
			showAlert(alertType, alertTitle, alertContent);
			return false;
		}else if(userpw== null || userpw.equals("")){
			alertTitle = "회원가입 오류";
			alertType = "warning";
			alertContent = "패스워드를 입력해주세요.";
			showAlert(alertType, alertTitle, alertContent);
			return false;
		}else if(!userpw.equals(userpw2)) {
			alertTitle = "회원가입 오류";
			alertType = "warning";
			alertContent = "패스워드가 일치하지 않습니다.";
			showAlert(alertType, alertTitle, alertContent);
			return false;
		}else if(username==null || username.equals("")) {
			alertTitle = "회원가입 오류";
			alertType = "warning";
			alertContent = "이름을 입력해주세요.";
			showAlert(alertType, alertTitle, alertContent);
			return false;
		}else if(email==null || email.equals("")) {
			alertTitle = "회원가입 오류";
			alertType = "warning";
			alertContent = "이메일을 입력해주세요.";
			showAlert(alertType, alertTitle, alertContent);
			return false;
		}else if(Address==null || Address.equals("")) {
			alertTitle = "회원가입 오류";
			alertType = "warning";
			alertContent = "주소를 입력해주세요.";
			showAlert(alertType, alertTitle, alertContent);
			return false;
		}
		
		String nameRegex = "^[a-zA-Z가-힣]{1,20}\\s?[a-zA-Z가-힣]{1,20}$";
		if(!username.matches(nameRegex)) {
			alertTitle = "회원가입 오류";
			alertType = "warning";
			alertContent = "올바른 이름 형식을 입력해주세요.";
			showAlert(alertType, alertTitle, alertContent);
			return false;
		}
		String emailRegex = "^[_a-z0-9-\\.]{3,20}@[a-z0-9-\\.]+\\.[a-z\\.]+$";
		if(!email.matches(emailRegex)) {
			alertTitle = "회원가입 오류";
			alertType = "warning";
			alertContent = "올바른 이메일 형식을 입력해주세요.";
			showAlert(alertType, alertTitle, alertContent);
			return false;
		}
		return true;
	}
	
	//관리자 페이지 가져오는 메소드
	public void getManagerMain() {
		try {
		Scene managerMainStage = new Scene(FXMLLoader.load(getClass().getResource("../view/ManagerMain.fxml")));
		managerMainStage.getStylesheets().add(getClass().getResource("../../application/application.css").toExternalForm());
		window = (Stage)loginPane.getScene().getWindow();
		window.setScene(managerMainStage);
		window.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//알림창 메소드
	public void showAlert(String type, String title, String content) {
		if(type.equals("confirmation")) {
			alert = new Alert(AlertType.CONFIRMATION, title, ButtonType.OK);
			alert.setHeaderText(null);
			alert.setContentText(content);
			alert.showAndWait();
		}else if(type.equals("warning")){
			alert = new Alert(AlertType.ERROR, title, ButtonType.OK);
			alert.setHeaderText(null);
			alert.setContentText(content);
			alert.showAndWait();
		}
	}
	
}
