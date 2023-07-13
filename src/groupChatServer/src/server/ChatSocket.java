package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatSocket {
	String name;
	Socket socket;
	DataInputStream  dis;
	DataOutputStream dos;
	static final String sepa = "###";

	public ChatSocket(String name, Socket socket) throws IOException {
		this.name 	= name;
		this.socket = socket;
		
		dis = new DataInputStream(socket.getInputStream());
		dos = new DataOutputStream(socket.getOutputStream());
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
}