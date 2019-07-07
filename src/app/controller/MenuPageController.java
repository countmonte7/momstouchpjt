package app.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import app.model.CartDAO;
import app.model.MenuDAO;
import app.model.MenuDTO;
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
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuPageController implements Initializable{
	
	@FXML
	private StackPane menuPane;
	
	@FXML
	private Button btnGetItem1, btnGetItem2, btnGetItem3, 
									btnDrinkMenu, btnBurgerMenu, btnSideMenu, btnChickenMenu,
									prevBtn, nextBtn;
	
	@FXML
	private Label Item1, Item2, Item3, 
								priceLbl1, priceLbl2, priceLbl3, 
								descLbl1, descLbl2, descLbl3;
	
	@FXML
	private ImageView imgView1, imgView2, imgView3; 
	
	@FXML
	private VBox menuVbox1, menuVbox2, menuVbox3;
	
	@FXML
	private HBox menuHbox;
	
	@FXML
	private GridPane gridPane1, gridPane2, gridPane3;
	
	@FXML
	private Spinner qtySpinner1, qtySpinner2, qtySpinner3;
	
	UserSession session;
	
	int countResult, count;
	
	String menuname, price, description, imgURL, nullimgURL;
	
	ArrayList<MenuDTO> menuList = new ArrayList<>();
	
	String category = "burger";
	
	int page = 1;
	
	int start, end;
	
	String addedItem = "";
	
	String quantity = "";
	
	String alertType, alertTitle, contentText;
	
	String userId;
	
	int cartId;
	
	CartDAO cart;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getMenu();
		userId = UserSession.getInstance().getUserId();
		cart = new CartDAO();
		cartId = cart.getCartId(userId);
		if(cartId == 0) {
			cart.createCart(userId);
			cartId = cart.getCartId(userId);
		}
	}
	
	
	//레이블 텍스트 바꾸기, 이미지 url 바꾸기
	public void showMenus(ArrayList<MenuDTO> menuList) {
		GridPane[] gridpanes = {gridPane1, gridPane2, gridPane3};
		List<GridPane> gridpaneList = Arrays.asList(gridpanes);
		VBox[] vboxes = {menuVbox1, menuVbox2, menuVbox3};
		List<VBox> vboxList = Arrays.asList(vboxes);
		ImageView[] imageviews = {imgView1, imgView2, imgView3};
		Label[] menuLbls = {Item1, Item2, Item3};
		Label[] priceLbls = {priceLbl1, priceLbl2, priceLbl3};
		Label[] descLbls = {descLbl1, descLbl2, descLbl3};
		Spinner[] qtySpinners = {qtySpinner1, qtySpinner2, qtySpinner3};
		List<ImageView> imageviewList = Arrays.asList(imageviews);
		List<Label> menuLblsList = Arrays.asList(menuLbls);
		List<Label> priceLblsList = Arrays.asList(priceLbls);
		List<Label> descLblsList = Arrays.asList(descLbls);
		List<Spinner> spinnerList = Arrays.asList(qtySpinners);
		
		if(page > Math.ceil(menuList.size()/(double)itemsPerPage())) {
			page = (int)Math.ceil(menuList.size()/(double)itemsPerPage());
		}
		
		start = (page -1)* itemsPerPage();
		end = page * itemsPerPage();
		
		for(int i = start ;i<end;i++) {
			if(i<menuList.size()) {
				menuname = menuList.get(i).getMenuname();
				price = menuList.get(i).getPrice()+"";
				description = menuList.get(i).getDescription();
				imgURL = menuList.get(i).getImgURL();
				imageviewList.get(i%3).setImage(new Image(imgURL));
				menuLblsList.get(i%3).setText(menuname);
				priceLblsList.get(i%3).setText(price);
				descLblsList.get(i%3).setText(description);
				initSpinner(spinnerList.get(i%3));
			}else {
				nullimgURL = "application/resources/nullimage.png";
				imageviewList.get(i%3).setImage(new Image(nullimgURL));
				menuLblsList.get(i%3).setText("");
				priceLblsList.get(i%3).setText("");
				descLblsList.get(i%3).setText("");
				spinnerList.get(i%3).getEditor().setText("0");
			}
		}
	}
	
	//카테고리로 메뉴 가져오기
	public void getMenu() {
		MenuDAO dao = new MenuDAO();
		menuList = dao.getMenuByCategory(category);
		showMenus(menuList);
	}
	
	//메뉴창 간 전환
	public void switchMenu(ActionEvent e) {
		String btnId = ((Button)e.getSource()).getId();
		MenuDAO dao = new MenuDAO();
		if(btnId.equals("btnBurgerMenu")) {
			this.category = "burger";
			this.page = 1;
			menuList = dao.getMenuByCategory(category);
			showMenus(menuList);
		}else if(btnId.equals("btnSideMenu")) {
			this.category = "side";
			this.page = 1;
			menuList = dao.getMenuByCategory(category);
			showMenus(menuList);
		}else if(btnId.equals("btnDrinkMenu")) {
			this.category = "drink";
			this.page = 1;
			menuList = dao.getMenuByCategory(category);
			showMenus(menuList);
		}else if(btnId.equals("btnChickenMenu")) {
			this.category = "chicken";
			this.page = 1;
			menuList = dao.getMenuByCategory(category);
			showMenus(menuList);
		}
	}
	
	//cart 아이디 없을 시 생성, cart에 아이템 추가
	@FXML
	public void addItem(ActionEvent e) {
		String menuname = "";
		int quantity = 0;
		MenuDAO menu = new MenuDAO();
		CartDAO cart = new CartDAO();
		try {
			int btnInfo = checkbtnId((Button)e.getSource());
			switch(btnInfo) {
			case 1 :
				menuname = Item1.getText();
				quantity = Integer.parseInt(qtySpinner1.getEditor().getText());
				break;
			case 2:
				menuname = Item2.getText();
				quantity = Integer.parseInt(qtySpinner2.getEditor().getText());
				break;
			case 3:
				menuname = Item3.getText();
				quantity = Integer.parseInt(qtySpinner3.getEditor().getText());
				break;
			}
			int menuId = menu.getMenuId(menuname);
			userId = UserSession.getInstance().getUserId();
			cartId = cart.getCartId(userId);
			int qtyValidCheck = cart.countItemInCart(cartId, menuId);
			
			if(quantity > 0) {
				if(qtyValidCheck == 0) {
					cart.addItemInCart(cartId, menuId, quantity);
				}else if(qtyValidCheck > 0) {
					cart.mergeItems(cartId, menuId, quantity);
				}
			}else {
				//수량 0개 선택 시 오류 메시지
				alertType = "warning"; 
				alertTitle = "오류 메시지";
				contentText = "수량은 0이 될 수 없습니다.";
						
				showAlert(alertType, alertTitle, contentText);
			}
		}catch(Exception e1) {
			e1.printStackTrace();
		}
	}
	
	//버튼에서 버튼아이디 가져오기
	private int checkbtnId(Button button) {
		int btnClickedResult = 0;
		if(button.getId().equals("btnGetItem1")) {
			btnClickedResult = 1;
		}else if(button.getId().equals("btnGetItem2")) {
			btnClickedResult = 2;
		}else if(button.getId().equals("btnGetItem3")) {
			btnClickedResult = 3;
		}
		return btnClickedResult;
	}
	
	//메뉴 이전 페이지
	public void prevPage() {
		if(page>1) {
			this.page -= 1;
			getMenu();
		}else {
			this.page = 1;
			getMenu();
		}
		
	}
	
	//페이지 당 보여줄 메뉴 개수 설정
	public int itemsPerPage() {
		return 3;
	}
	
	//메뉴 더 보기
	public void nextPage() {
		this.page += 1;
		getMenu();
	}
	
	
	//장바구니 페이지로 이동
	public void goToCart(ActionEvent event) {
		try {
			Parent cart = FXMLLoader.load(getClass().getResource("../view/Cart.fxml"));
			Scene cartScene = new Scene(cart);
			cartScene.getStylesheets().add(getClass().getResource("../../application/application.css").toExternalForm());
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
			window.setScene(cartScene);
			window.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initSpinner(Spinner qtySpinner) {
		qtySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20));
	}
	
	//메시지창 띄우기
	public void showAlert(String alertType, String alertTitle, String contentText) {
		if(alertType.equals("warning")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle(alertTitle);
			alert.setContentText(contentText);
			alert.showAndWait();
		}
		
		
	}
	
	
}
