package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientThread extends Thread {
	static final String sepa = "###";
	Socket socket;
	String client;
	DataInputStream  dis;
	DataOutputStream dos;
	StringTokenizer  st;
	
	private Login 		frmLogin;
	private SignUp 		frmAccount;
	private ChangePass  frmChange;
	private ChatForm 	chat;

	public ClientThread(Socket socket) {
		this.socket = socket;

		try {
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (!socket.isClosed()) {
				String data = dis.readUTF();
				st = new StringTokenizer(data, sepa);
				String CMD = st.nextToken();
				String cont01, cont02, cont03;

				switch (CMD) {
				case "CMD_LOGIN":
					cont01 = st.nextToken();
					System.out.println(cont01);
					setLogin(cont01);
					break;

				case "CMD_SIGNUP":
					cont01 = st.nextToken();
					sendMsgAccount(cont01);
					break;
					
				case "CMD_CHANGEPASS":
					cont01 = st.nextToken();
					sendMsgChangePass(cont01);
					break;

				case "CMD_INSERT":
					cont01 = st.nextToken();
					setOnline(cont01);
					break;

				case "CMD_ADD":
					cont01 = st.nextToken();
					setOnline(cont01);
					break;

				case "CMD_SEND":
					cont03 = st.nextToken();
					cont02 = st.nextToken();
					sendMsg(cont03, cont02);
					break;

				case "CMD_LOGOUT":
					cont03 = st.nextToken();
					removeUser(cont03);
					break;

				case "CMD_OLDMSG":
					cont02 = st.nextToken();
					cont03 = st.nextToken();
					showOldMsg(cont02,cont03);
					
				default:
					break;
				}
			}
		} catch (IOException e) {
			System.out.println(client);
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

	public void setLogin(String msg) {
		frmLogin.getLogin(msg);
	}
	
	public void sendMsgAccount(String acc) {
		frmAccount.getAccount(acc);
	}
	
	public void sendMsgChangePass(String msg) {
		frmChange.getChangePass(msg);
	}

	public void setOnline(String msg) {
		chat.getOnline(msg);
	}

	public void sendMsg(String sender, String msg) {
		chat.getMsg(sender, msg);
	}

	public void removeUser(String name) {
		chat.getRemove(name);
	}
	
	public void showOldMsg(String sender, String msg) {
		chat.getOldMsg(sender, msg);
	}
}
