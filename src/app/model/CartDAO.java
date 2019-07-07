package app.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

public class CartDAO {
	Connection conn = null;
	ResultSet rs = null; 
	PreparedStatement pstmt = null;
	
	java.util.Date date;
	java.sql.Timestamp timestamp;
	
	int menuId, price, quantity;
	
	public ArrayList<Cart> getCartList(String userId) {
		String sql = "SELECT mn.menuid, mn.menuname, mn.price, ci.quantity FROM menu AS mn INNER JOIN cartitems AS ci ON mn.menuid = ci.menuId "
				+ "where cartId = (SELECT cartId from cart where userId = ?)";
		String menuname = "";
		ArrayList<Cart> itemsInCart = new ArrayList<>();
		try {
			conn = DBConnection.db();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs  = pstmt.executeQuery();
			while(rs.next()) {
				Cart cart = new Cart();
				cart.setMenuId(rs.getInt(1));
				cart.setMenuname(rs.getString(2));
				cart.setPrice(rs.getInt(3));
				cart.setQuantity(rs.getInt(4));
				itemsInCart.add(cart);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return itemsInCart;
	}
	
	//카트에 들어있는 해당 물건 있는지 확인(없으면 0)
	public int countItemInCart(int cartId, int menuId) {
		String sql = "SELECT * from cartitems WHERE cartId=? and menuId = ?";
		int qty = 0;
		try {
			conn = DBConnection.db();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cartId);
			pstmt.setInt(2, menuId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				qty = rs.getInt(3);
				return qty;
			}else {
				return 0;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	//카트 만들기
	public boolean createCart(String userId) {
		String sql = "INSERT INTO cart (userId, createdDate) VALUES(?,?)";
		date = new java.util.Date();
		timestamp = new java.sql.Timestamp(date.getTime());
		try {
			conn = DBConnection.db();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setTimestamp(2, timestamp);
			int result = pstmt.executeUpdate();
			if(result==1) {
				return true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt!=null)pstmt.close();
				if(conn!=null)conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	//카트에 물건 넣기
	public int addItemInCart(int cartId, int menuId, int quantity) {
		String sql = "INSERT INTO cartItems (Id, cartId, menuId, quantity, createdDate) VALUES (?, ?, ?, ?, ?)";
		int result=0;
		date = new java.util.Date();
		timestamp = new java.sql.Timestamp(date.getTime());
		try {
			conn = DBConnection.db();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 0);
			pstmt.setInt(2, cartId);
			pstmt.setInt(3, menuId);
			pstmt.setInt(4, quantity);
			pstmt.setTimestamp(5, timestamp);
			result = pstmt.executeUpdate();	
			if(result == 1) {
				//장바구니에 상품 추가 성공 메시지
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	//유저 아이디로 카트 id 가져오기 
	public int getCartId(String userId) {
		String sql = "SELECT cartId FROM cart where userId = ?";
		int userCartId = 0;
		try {
			conn = DBConnection.db();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				userCartId = rs.getInt(1);
				return userCartId;
			}else {
				return 0;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	//카트에 동일 물건 존재 시 합치기
	public int mergeItems(int cartId, int menuId, int quantity) {
		String sql = "UPDATE cartitems SET quantity=quantity + ? WHERE cartId=? and menuId=?";
		int menuCountResult = 0;
		try {
			conn = DBConnection.db();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, quantity);
			pstmt.setInt(2, cartId);
			pstmt.setInt(3, menuId);
			menuCountResult = pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return menuCountResult;
		
	}
	
	public int emptyCart(int cartId) {
		String sql = "DELETE FROM cartitems WHERE cartId = ?";
		int deleteResult = 0;
		try {
			pstmt = DBConnection.db().prepareStatement(sql);
			pstmt.setInt(1, cartId);
			deleteResult = pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt!=null) pstmt.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return deleteResult;
	}
	
	public int deleteSelected(int menuId, int cartId) {
		String sql = "DELETE FROM cartitems WHERE menuId = ? and cartId = ?";
		int selectionDeleteResult;
		try {
			conn = DBConnection.db();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, menuId);
			pstmt.setInt(2, cartId);
			selectionDeleteResult = pstmt.executeUpdate();
			if(selectionDeleteResult > 0) {
				return 1;
			}else if(selectionDeleteResult == 0) {
				return 0;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt!= null) pstmt.close();
				if(conn!=null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	
}
