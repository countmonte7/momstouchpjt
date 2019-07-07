package app.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import app.model.Cart;
import app.model.CartDAO;
import app.model.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CartPageController implements Initializable{
	
	@FXML
	private TableView<Cart> cartList;
	
	@FXML
	private TableColumn<Cart, String> colMenuname;
	
	@FXML
	private TableColumn<Cart, Number> colMenuprice, colMenuqty;
	
	@FXML
	private Label totPriceLbl;
	
	@FXML
	private Button backPgBtn, purchaseBtn;
	
	private ObservableList<Cart> list;
	
	public int totalPrice;
	
	CartDAO cartDAO;
	
	String dialogMessage, dialogTitle, dialogContent; 

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		colMenuname.setCellValueFactory(new PropertyValueFactory<>("menuname"));
		colMenuqty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		colMenuprice.setCellValueFactory(new PropertyValueFactory<>("price"));
		cartList.setItems(getList(UserSession.getInstance().getUserId()));
		totPriceLbl.setText(totalPrice + " 원");
	}
	
	private ObservableList<Cart> getList(String userId) {
		list = FXCollections.observableArrayList();
		totalPrice = 0;
		cartDAO = new CartDAO();
		ArrayList<Cart> cartList = cartDAO.getCartList(userId);
		for(int i=0;i<cartList.size();i++) {
			Cart cart = new Cart(cartList.get(i).getMenuname(), 
					cartList.get(i).getPrice()*cartList.get(i).getQuantity(), cartList.get(i).getQuantity(), cartList.get(i).getMenuId());
			totalPrice += cart.getPrice();
			list.add(cart);
		}
		return list;
	}
	
	//이전 페이지로 돌아가기
	public void goPrevPage() {
		Stage window;
		Scene menuScene;
		try {
			window = (Stage)backPgBtn.getScene().getWindow();
			menuScene = new Scene(FXMLLoader.load(getClass().getResource("../view/MenuPage.fxml")));
			menuScene.getStylesheets().add(getClass().getResource("../../application/application.css").toExternalForm());
			window.setScene(menuScene);
			window.setResizable(false);
			window.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void emptyUserCart() {
		String dialogMessage = "confirmation";
		String dialogTitle = "장바구니 비우기";
		String dialogContent = "장바구니를 비우겠습니다.";
		int fromDialog = showAlert(dialogMessage, dialogTitle, dialogContent);
		if(fromDialog==1) {
			cartDAO = new CartDAO();
			String userId = UserSession.getInstance().getUserId();
			int cartId = cartDAO.getCartId(userId);
			cartDAO.emptyCart(cartId);
			initialize(null, null);
		}else {
			return;
		}
	}
	
	public void deleteFromSelection(ActionEvent event) {
		int selectedIndex = cartList.getSelectionModel().getSelectedIndex();
		if(selectedIndex>=0) {
			Cart selectedMenu = cartList.getSelectionModel().getSelectedItem();
			int menuId = selectedMenu.getMenuId();
			String userId = UserSession.getInstance().getUserId();
			int cartId = cartDAO.getCartId(userId);
			dialogMessage = "confirmation";
			dialogTitle = "메뉴 삭제 확인";
			dialogContent = "선택하신 메뉴를 삭제하시겠습니까?";
			int fromDialog = showAlert(dialogMessage, dialogTitle, dialogContent);
			if(fromDialog == 1) {
				int targetDelete = cartDAO.deleteSelected(menuId, cartId);
				if(targetDelete>0) {
					dialogMessage = "information";
					dialogTitle = "확인 메시지";
					dialogContent = "선택하신 메뉴를 삭제 완료했습니다.";
					showAlert(dialogMessage, dialogTitle, dialogContent);
					initialize(null, null);
				}else if(targetDelete == 0) {
					dialogMessage = "warning";
					dialogTitle = "실패 메시지";
					dialogContent = "선택하신 메뉴를 찾을 수 없습니다.";
					showAlert(dialogMessage, dialogTitle, dialogContent);
				}else if(targetDelete == -1) {
					dialogMessage = "warning";
					dialogTitle = "실패 메시지";
					dialogContent = "데이터베이스 오류입니다. 관리자에게 문의하세요.";
					showAlert(dialogMessage, dialogTitle, dialogContent);
				}
			}else if(fromDialog == 0) {
				dialogMessage = "information";
				dialogTitle = "확인 메시지";
				dialogContent = "삭제 취소했습니다.";
				showAlert(dialogMessage, dialogTitle, dialogContent);
			}
		}else {
			dialogMessage = "warning";
			dialogTitle = "실패 메시지";
			dialogContent = "메뉴가 선택되지 않았습니다.";
			showAlert(dialogMessage, dialogTitle, dialogContent);
		}
	}
	
	public int showAlert(String message, String title, String contentText) {
		Optional<ButtonType> alertResult;
		Alert alert;
		if(message == "confirmation") {
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle(title);
			alert.setContentText(contentText);
			alertResult = alert.showAndWait();
			if(alertResult.get() == ButtonType.OK) {
				return 1;
			}else {
				return 0;
			}
		}else if(message == "information") {
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(title);
			alert.setContentText(contentText);
			alert.showAndWait();
		}else if(message == "warning") {
			alert = new Alert(AlertType.WARNING);
			alert.setTitle(title);
			alert.setContentText(contentText);
			alert.showAndWait();
		}
		return -1;
	}
	
	public void getPurcahsePage(ActionEvent event) {
		try {
			Scene purchaseScene = new Scene(FXMLLoader.load(getClass().getResource("../view/PayPage.fxml")));
			purchaseScene.getStylesheets().add(getClass().getResource("../../application/application.css").toExternalForm());
			Stage purchaseWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
			purchaseWindow.setScene(purchaseScene);
			purchaseWindow.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
