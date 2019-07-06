package app.model;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.ResourceBundle;

public class DBConnection {
		private static Connection conn;
		
		public static Connection db() {	
			try {
				Properties prop = new Properties();
				FileInputStream ip = new FileInputStream("C:/spring/momsTouch/src/config.properties");
				prop.load(ip);
				Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
				String url = "jdbc:mysql://localhost:3306/momstouch?characterEncoding=UTF-8&&serverTimezone=UTC";
				String userId = prop.getProperty("dbId");
				String userPw = prop.getProperty("dbPw");
				System.out.println(userId);
				conn = DriverManager.getConnection(url, userId, userPw);
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			return conn;
		}
		
	}
