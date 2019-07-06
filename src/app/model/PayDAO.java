package app.model;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.mysql.cj.protocol.Resultset;

public class PayDAO {
	
	Connection conn;
	PreparedStatement pstmt;
	Resultset rs;
	
	java.util.Date date;
	java.sql.Timestamp timestamp;
	
	public void insertPay(String userId, String payType, int payAmount, int cartId) {
		String sql = "INSERT INTO pay VALUES(?,?,?,?,?,?)";
		date = new java.util.Date();
		timestamp = new java.sql.Timestamp(date.getTime());
		try {
			conn = DBConnection.db();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 0);
			pstmt.setString(2, userId);
			pstmt.setString(3, payType);
			pstmt.setInt(4, payAmount);
			pstmt.setTimestamp(5, timestamp);
			pstmt.setInt(6, cartId);
			int payResult = pstmt.executeUpdate();
			if(payResult > 0) {
				//결제 성공
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
	}
	
	
	
}
