package utilPackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommunicateMySQL {

	String insert;
	String check;
	String pass;
	public boolean register(Connection conn,String username,String password){
		if (checkuser(conn, username)) {
			return false;
		}else{
			try {
				insert = "insert into `user` values(?,?)";
				PreparedStatement psmt=conn.prepareStatement(insert);	
				psmt.setString(1, username);
				psmt.setString(2, password);
				psmt.executeUpdate();
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	}
	public boolean checkuser(Connection conn,String username){
		try {
			check = "select * from `user` where username='"+username+"'";
			PreparedStatement psmt = conn.prepareStatement(check);
			ResultSet rs=psmt.executeQuery();;
			if (rs.next()) {
				return true;
			}else{
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public boolean login(Connection conn,String username,String password){
		try {
			check = "select * from `user` where username='"+username+"'";
			PreparedStatement psmt = conn.prepareStatement(check);
			ResultSet rs=psmt.executeQuery();;
			if (rs.next()) {
				pass = rs.getString("password");
				if (password.equals(pass)) {
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public boolean addfriend(Connection conn,String username,String addname){
		if (checkuser(conn, addname)) {
			return false;
		}else{
			try {
				insert = "insert into `relationuser` values(?,?)";
				PreparedStatement psmt=conn.prepareStatement(insert);	
				psmt.setString(1, username);
				psmt.setString(2, addname);
				psmt.executeUpdate();
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	}
	public ResultSet getfriendlist(Connection conn,String username){
		try {
			System.out.println("sql username:"+username);
			check = "select * from `relationuser` where username='"+username+"'";
			PreparedStatement psmt = conn.prepareStatement(check);
			ResultSet rs=psmt.executeQuery();
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
