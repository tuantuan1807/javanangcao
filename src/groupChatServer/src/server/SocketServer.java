package server;

import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

	private ServerSocket server;
	public boolean isStop = false;

	public SocketServer() throws Exception {
		server = new ServerSocket(5555);
		(new WaitForConnect()).start();
	}

	public void stopServer() throws Exception {
		isStop = true;
		server.close();
	}

	public class WaitForConnect extends Thread {
		@Override
		public void run() {
			try {
				while (!isStop) {
					Socket 		 s  = server.accept();
					ServerThread st = new ServerThread(s);
					Thread 		 t  = new Thread(st);
					t.start();
				}
			} catch (Exception e) {
				System.out.println("Socket closed!");
			}
		}
	}
}
