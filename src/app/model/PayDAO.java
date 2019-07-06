package app.model;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.mysql.cj.protocol.Resultset;

public class PayDAO {
	
	Connection conn;
	PreparedStatement pstmt;
	Resultset rs;
	
	public void insertPay(String userId) {
		String sql = "INSERT INTO pay VALUES(0,?,?,?,?)";
		try {
			conn = DBConnection.db();
			pstmt = conn.prepareStatement(sql);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
