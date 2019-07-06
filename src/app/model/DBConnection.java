package app.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
		private static Connection conn;
		
		public static Connection db() {	
			try {
				Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
				String url = "jdbc:mysql://localhost:3306/momstouch?characterEncoding=UTF-8&&serverTimezone=UTC";
				String userId = "root";
				String userPw = "47690eo5dh";
				conn = DriverManager.getConnection(url, userId, userPw);
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			return conn;
		}
		
	}
