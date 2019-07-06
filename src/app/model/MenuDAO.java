package app.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MenuDAO {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	
	public int getMenuId(String menuname) {
		String sql = "SELECT * FROM menu WHERE menuname = ?";
		int menuId = 0;
		try {
			 conn = DBConnection.db();
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, menuname);
			 rs = pstmt.executeQuery();
			 while(rs.next()) {
				 menuId = rs.getInt("menuid");
				 return menuId;
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
		return menuId; //해당 menu 아이디가 없을 경우 0 리턴
	}
	
	public MenuDTO getMenuInfo(int menuId) {
		String sql = "SELECT * from menu WHERE menuid = ?";
		MenuDTO dto = new MenuDTO();
		try {
			conn = DBConnection.db();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, menuId);
			rs =pstmt.executeQuery();
			
			while(rs.next()) {
				dto.setMenuname(rs.getString("menuname"));
				dto.setPrice(rs.getInt("price"));
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
		return dto;
	}
	
	public ArrayList<MenuDTO> getMenuByCategory(String category) {
		String sql = "SELECT menuname, price, description, imgAddress FROM menu WHERE category = ?";
		ArrayList<MenuDTO> menuInfoList = new ArrayList<>();
		try {
			conn = DBConnection.db();
			pstmt = conn.prepareStatement(sql);
			System.out.println(category);
			pstmt.setString(1, category);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MenuDTO dto = new MenuDTO();
				dto.setMenuname(rs.getString(1));
				dto.setPrice(rs.getInt(2));
				dto.setDescription(rs.getString(3));
				dto.setImgURL(rs.getString(4));
				menuInfoList.add(dto);
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
		
		return menuInfoList;
	}
	
	
}
