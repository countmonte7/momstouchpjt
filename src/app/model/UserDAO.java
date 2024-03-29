package app.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class UserDAO {
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	//로그인
	public int login(String id, String pw) {
		String query = "SELECT * FROM user WHERE userId=?";
		try {
			conn = DBConnection.db();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String userPw = rs.getString(2);
				if(pw!= null && pw.equals(userPw)) {
					return 1; // 로그인 성공
				}else if(!pw.equals(userPw)) {
					return 0; //비밀번호 불일치
				}
			}else {
				return -1;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -2;
	}
	
	//회원가입
	public int signUp(String id, String pw, String name, String email, String address) {
		
		String query = "INSERT INTO user VALUES(?,?,?,?,?)";
		try {
			conn = DBConnection.db();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, name);
			pstmt.setString(4, email);
			pstmt.setString(5, address);
			int result = pstmt.executeUpdate();
			if(result==1) {
				return 1; //회원가입 성공 시
			}
			else if(result==0){
				return 0;
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
		return -1;
	}
	
	public String getUserId() {
		String userId = null;
		return userId;
	}
	
	public String getUserAddress(String userId) {
		String sql = "SELECT userAddress FROM user WHERE userId = ?";
		String userAddress = "";
		try {
			conn = DBConnection.db();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				userAddress = rs.getString(1);
				return userAddress;
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
		return null;
	}
	
	public int userIdDuplCheck(String userId) {
		String sql = "SELECT userId FROM user WHERE userId = ?";
		try {
			conn = DBConnection.db();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return 1; //아이디 존재할 시
			}else {
				return 0; //아이디 존재하지 않을 때
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1; //데이터베이스 오류
	}
	
}
