package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDB {
	private Connection connec = null;
	private PreparedStatement pre = null;
	private Statement stmt = null;
	private ResultSet rs1 = null;

	public ConnectDB() {

		try {
			String userName = "root";
			String password = "18072004";
			String url 		= "jdbc:mysql://localhost:3306/chatSocket";
			Class.forName("com.mysql.cj.jdbc.Driver");
			connec = DriverManager.getConnection(url, userName, password);
			System.out.println("1. Connected ok");
		} catch (Exception e) {
			System.out.println("2. Connected error");
		}
	}

	public void newAccount(String username, String pass) {
		try {
			pre = connec.prepareStatement("INSERT INTO TA_INF_ACCOUNT (t_Username,t_Pass) VALUES(?,?)");
			pre.setString(1, username);
			pre.setString(2, pass);
			pre.executeUpdate();
		} catch (Exception e) {
		}
	}

	public ResultSet login(String username) {
		try {
			stmt = connec.createStatement();
			rs1  = stmt.executeQuery("select t_Pass from TA_INF_ACCOUNT where t_Username = '" + username + "'");
			return rs1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet checkExist() {
		try {
			stmt = connec.createStatement();
			rs1  = stmt.executeQuery("select t_Username from TA_INF_ACCOUNT ");
			return rs1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet checkID(String username) {
		try {
			stmt = connec.createStatement();
			rs1  = stmt.executeQuery("select i_ID from TA_INF_ACCOUNT where t_Username = '" + username + "'");
			return rs1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setMsg(int idSend, int idReceive, String msg, String now) {
		try {
			pre = connec.prepareStatement("INSERT INTO TA_MSG_SAVE (i_IDsend,i_IDreceive,t_Msg, d_Date) VALUES(?,?,?,?)");
			pre.setInt(1, idSend);
			pre.setInt(2, idReceive);
			pre.setString(3, msg);
			pre.setString(4, now);

			pre.executeUpdate();
		} catch (Exception e) {
		}
	}

	public ResultSet getMsg(int idSend, int idReceive) {
		try {
			stmt = connec.createStatement();
			rs1  = stmt.executeQuery("select i_IDsend, t_Msg from  TA_MSG_SAVE where (i_IDsend = '" + idSend
					+ "' and i_IDreceive = '" + idReceive + "')" + "or (i_IDreceive = '" + idSend + "' and i_IDsend = '"
					+ idReceive + "')" + " ORDER BY i_IDmsg");
			return rs1;
		} catch (Exception e) {
		}
		return null;
	}

	public ResultSet name(int id) {
		try {
			stmt = connec.createStatement();
			rs1  = stmt.executeQuery("select t_Username from TA_INF_ACCOUNT where i_ID = '" + id + "'");
			return rs1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
