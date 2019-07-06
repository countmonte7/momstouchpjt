package app.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MenuDTO {
	private final StringProperty menuname = new SimpleStringProperty();
	private final IntegerProperty price  = new SimpleIntegerProperty();
	private final IntegerProperty count = new SimpleIntegerProperty();
	
	private int menuid;
	private String description;
	private String category;
	private String imgURL;
	
	public StringProperty menunameProperty() {
		return menuname;
	}

	public IntegerProperty priceProperty() {
		return price;
	}
	
	public IntegerProperty countProperty() {
		return count;
	}
	
	public String getMenuname() {
		return menuname.get();
	}

	public void setMenuname(String menu) {
		this.menuname.set(menu);
	}

	public int getPrice() {
		return price.get();
	}

	public void setPrice(int price) {
		this.price.set(price);
	}
	
	public int getCount() {
		return count.get();
	}
	
	public void setCount(int count) {
		this.count.set(count);
	}

	public int getMenuid() {
		return menuid;
	}

	public void setMenuid(int menuid) {
		this.menuid = menuid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}
	
	
	
}
