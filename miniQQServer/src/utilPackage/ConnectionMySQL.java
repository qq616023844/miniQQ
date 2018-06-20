package utilPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMySQL {
	public Connection ComMySQL() {
		String url="jdbc:mysql://localhost:3306/miniqq?user=root&password=";
		String driverName="com.mysql.jdbc.Driver";
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("加载驱动出错");
		}
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url,"root","");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("连接数据库出错");
		}
		System.out.println("数据库连接成功");
		return conn;
	}	
}
