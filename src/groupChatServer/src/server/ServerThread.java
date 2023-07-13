package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class ServerThread extends Thread {

	public static ArrayList<ChatSocket> list = new ArrayList<>();
	private ConnectDB connec = null;
	private Socket s;
	DataInputStream  dis;
	DataOutputStream dos;
	StringTokenizer  st;
	static final String sepa = "###";

	public ServerThread(Socket s) {
		this.s = s;
	}

	@Override
	public void run() {
		try {
			System.out.println("Connect success!");
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());

			if (connec == null) {
				connec = new ConnectDB();
			}
			while (!s.isClosed()) {
				String data = dis.readUTF();
				st = new StringTokenizer(data, sepa);
				String CMD = st.nextToken();

				switch (CMD) {
				case "CMD_LOGIN":
					doLogin(st);
					break;

				case "CMD_SIGNUP":
					doSignUp(st);
					break;

				case "CMD_CHANGEPASS":
					doChangePass(st);
					break;

				case "CMD_ONLINE":
					doShowOnlList(st);
					break;

				case "CMD_SEND":
					doSendMsg(st);
					break;

				case "CMD_LOGOUT":
					doLogOut(st);
					break;

				case "CMD_OLDMSG":
					doShowOldMsg(st);
					break;

				default:
					System.out.println("[Command Unknown] : " + CMD);
					break;
				}
			}
		} catch (IOException e) {
			System.out.println(s + " vua thoat!");
		}
	}

	public void doSendData(String cmd, String... params) {
		try {
			synchronized (dos) {
				String data = cmd;
				for (String s : params) {
					data = data + sepa + s;
				}
				dos.writeUTF(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doLogin(StringTokenizer st) {
		String username = st.nextToken();
		String pass 	= st.nextToken();
		ResultSet rs1 	= connec.login(username);
		try {

			// ------------- NOTIFICATION -------------
			String notif_1 = "Login successful!";
			String notif_2 = "Wrong password!";
			String notif_3 = "This account does not exist!";

			if (rs1.next()) {
				if (pass.equals(rs1.getString("t_Pass"))) {
					System.out.println("Login successful!");
					doSendData("CMD_LOGIN", notif_1);
				} else {
					doSendData("CMD_LOGIN", notif_2);
				}
			} else {
				doSendData("CMD_LOGIN", notif_3);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doSignUp(StringTokenizer st) {
		String username = st.nextToken();
		String pass 	= st.nextToken();
		String repass 	= st.nextToken();

		// ------------- NOTIFICATION -------------
		String notif_1 = "Username already exists!";
		String notif_2 = "Create Account success!";
		String notif_3 = "Create Account failed!";

		if (pass.equals(repass)) {
			boolean check = false;
			try {
				ResultSet rs1 = connec.checkExist();
				while (rs1.next()) {
					if (username.equals(rs1.getString("t_Username"))) {
						doSendData("CMD_SIGNUP", notif_1);
						check = true;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (check != true) {
				connec.newAccount(username, pass);
				doSendData("CMD_SIGNUP", notif_2);
			}
		} else {
			doSendData("CMD_SIGNUP", notif_3);
		}
	}

	public void doChangePass(StringTokenizer st) {

		String username 	= st.nextToken();
		String current_pass = st.nextToken();
		String new_pass 	= st.nextToken();
		String repeat_pass 	= st.nextToken();
		
		// ------------- NOTIFICATION -------------
		String notif_1 = "Wrong Password!";
		String notif_2 = "Change password success!";
		String notif_3 = "New password and Confirmation password do not match!";

		if (new_pass.equals(repeat_pass)) {
			boolean check = false;
			try {				
				ResultSet rs1 = connec.checkPass();
				while (rs1.next()) {
					if (username.equals(rs1.getString("t_Username")) && !current_pass.equals(rs1.getString("t_Pass"))) {
						System.out.println(notif_1);
						doSendData("CMD_CHANGEPASS", notif_1);
						check = true;
					}
				}

				if (check != true) {
					connec.changePass(username, new_pass, repeat_pass);
					System.out.println(notif_2);
					doSendData("CMD_CHANGEPASS", notif_2);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(notif_3);
			doSendData("CMD_CHANGEPASS", notif_3);
		}

	}

	public void doShowOnlList(StringTokenizer st) throws IOException {
		System.out.println("Show Online list");

		int n = list.size();
		for (int i = 0; i < n; i++) {
			doSendData("CMD_INSERT", list.get(i).getName());
		}

		String name = st.nextToken();
		if (!name.equals("Cancel")) {
			ChatSocket chatSocket = new ChatSocket(name, s);
			list.add(chatSocket);
			for (ChatSocket cs : list) {
				cs.doSendData("CMD_ADD", name);
			}
		}
	}

	public void doSendMsg(StringTokenizer st) {

		String msg 	  = st.nextToken();
		String sender = st.nextToken();
		String date   = st.nextToken();
		System.out.println(" " + sender + ":  " + msg);

		ResultSet rs2 = connec.checkID(sender);
		int idSend 	  = 0;
		try {
			if (rs2.next()) {
				idSend = rs2.getInt("i_ID");
			}
			connec.setMsg(idSend, msg, date);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (ChatSocket cs : list) {
			if (!(cs.getName().equals(sender))) {
				cs.doSendData("CMD_SEND", sender, msg);
			}
		}
	}

	public void doLogOut(StringTokenizer st) {
		String removeU = st.nextToken();

		for (int i = 0; i < list.size(); i++) {
			String username = list.get(i).getName();
			if (username.equals(removeU)) {
				list.remove(list.get(i));
			}
		}

		for (ChatSocket cs : list) {
			cs.doSendData("CMD_LOGOUT", removeU);
		}
	}

	public void doShowOldMsg(StringTokenizer st) {
		ResultSet rs2, rs3;
		String send   = st.nextToken();

		String sender = null;
		try {
			rs2 = connec.getMsg();
			while (rs2.next()) {
				int id = rs2.getInt("i_IDuser");
				rs3 = connec.name(id);
				if (rs3.next()) {
					sender = rs3.getString("t_Username");
				}
				String mess = rs2.getString("t_Msg");
				for (ChatSocket cs : list) {
					if (cs.getName().equals(send)) {
						cs.doSendData("CMD_OLDMSG", sender, mess);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
