package app.controller;


import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import app.model.Cart;
import app.model.CartDAO;
import app.model.UserDAO;
import app.model.UserSession;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;



public class PayPageController implements Initializable{

	@FXML
	private Button payConfirmBtn, payCancelBtn;
	
	@FXML
	private RadioButton cardPay, cashPay, couponPay;
	
	@FXML
	private ToggleGroup payGroup;
	
	@FXML
	private AnchorPane payPagePane;
	
	@FXML
	private Label orderItemsLbl,orderPriceLbl, userAddressLbl;
	
	String payType;
	
	Alert alert;
	
	Optional<ButtonType> alertResult;
	
	Stage owner;
	
	Dialog dialog;
	
	private int totalPrice, cartId;
	
	private boolean paymentStatus = true;
	
	
@Override
public void initialize(URL location, ResourceBundle resources) {
	loadOrderingMenu();
	payGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle>
		observ, Toggle oldVal, Toggle newVal) -> {
			payType = (String)newVal.getUserData();
		}
			);
	
}

public void loadOrderingMenu() {
	CartDAO cart = new CartDAO();
	UserDAO user = new UserDAO();
	String userId = UserSession.getInstance().getUserId();
	ArrayList<Cart> orderMenuList = cart.getCartList(userId);
	StringBuilder orderMnName = new StringBuilder();
	int totalPrice = 0;
	for(int i = 0;i<orderMenuList.size();i++) {
		String menuname = orderMenuList.get(i).getMenuname();
		if(i!= orderMenuList.size()-1) {
			orderMnName.append(menuname + ", ");
		}else {
			orderMnName.append(menuname);
		}
		
		int price = orderMenuList.get(i).getPrice() * orderMenuList.get(i).getQuantity();
		totalPrice += price;
	}
	orderItemsLbl.setText(orderMnName.toString());
	orderPriceLbl.setText(totalPrice + " 원");
	userAddressLbl.setText(user.getUserAddress(userId));
}

public void payConfirm() {
	if(payType!=null) {
		switch(payType) {
		case "cash":
			break;
		case "card":
			showCardPayProgress();
			break;
		case "coupon":
			showCouponPayProgress();
			break;
		default:
			break;
		}
	}else {
		messageAlert("warning", "결제 오류", "결제 수단을 선택하세요.");
	}
}

@FXML
public void payCancel(ActionEvent event) {
	messageAlert("confirmation", "주문 취소 확인" , "주문 취소하겠습니까?");
	if(alertResult.get() == ButtonType.OK) {
		try {
			payPagePane.getChildren().clear();
			FXMLLoader loader = new  FXMLLoader(getClass().getResource("../view/Cart.fxml"));
			Parent parent = loader.load();
			payPagePane.getChildren().add(parent);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}else {
		return;
	}
}

public void messageAlert(String messageType, String messageTitle, String messageContent) {
	if(messageType=="warning") {
		alert = new Alert(AlertType.ERROR);
		alert.setTitle(messageTitle);
		alert.setContentText(messageContent);
		alert.showAndWait();
	}else if(messageType=="confirmation") {
		alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(messageTitle);
		alert.setContentText(messageContent);
		alertResult = alert.showAndWait();
	}
	
}

public void showCardPayProgress() {
	System.out.println("카드 결제");
}

public void showCouponPayProgress() {
	
}

public Dialog getPayProgressDialog() {
	try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(""));
		dialog = new Dialog();
	}catch(Exception e) {
		e.printStackTrace();
	}
	return dialog;
}

}
