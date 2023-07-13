package server;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ServerForm extends JFrame {

	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel2;
	private JLabel lbStatus;
	private static JTextArea txtUserOnl;
	private static JLabel lbO;
	JButton btStart, btStop;

	static ServerThread serverThread;
	static SocketServer clientConnect;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerForm frame = new ServerForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ServerForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 593, 380);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		btStart = new JButton("Start Server");
		btStart.setFont(new Font("Tahoma", Font.BOLD, 17));
		btStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					clientConnect = new SocketServer();
					lbO.setText("<html><font color='green'><b>SERVER ĐANG MỞ</b></font></html>");
					btStart.setEnabled(false);
					btStop.setEnabled(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btStart.setBounds(15, 105, 160, 30);
		contentPane.add(btStart);

		btStop = new JButton("Stop Server");
		btStop.setFont(new Font("Tahoma", Font.BOLD, 17));
		btStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					clientConnect.stopServer();
					lbO.setText("<html><font color='red'><b>ĐÃ ĐÓNG SERVER</b></font></html>");
					btStart.setEnabled(true);
					btStop.setEnabled(false);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btStop.setBounds(15, 215, 160, 30);
		contentPane.add(btStop);
		txtUserOnl = new JTextArea();
		txtUserOnl.setEditable(false);
		txtUserOnl.setBackground(new Color(176, 224, 230));
		txtUserOnl.setForeground(new Color(0, 0, 0));
		txtUserOnl.setFont(new Font("Tahoma", Font.PLAIN, 17));

		panel2 = new JPanel();
		panel2.setBackground(new Color(240, 240, 240));
		panel2.setBounds(190, 105, 375, 222);
		panel2.setLayout(new GridLayout(0, 1, 0, 0));
		scrollPane = new JScrollPane(txtUserOnl);
		panel2.add(scrollPane);
		contentPane.add(panel2);

		panel = new JPanel();
		panel.setBackground(new Color(175, 238, 238));
		panel.setBounds(190, 20, 375, 75);
		contentPane.add(panel);
		panel.setLayout(null);

		lbStatus = new JLabel("Trạng thái:");
		lbStatus.setFont(new Font("Tahoma", Font.BOLD, 17));
		lbStatus.setBounds(30, 25, 110, 25);
		panel.add(lbStatus);

		panel_1 = new JPanel();
		panel_1.setBounds(155, 22, 189, 30);
		panel.add(panel_1);

		lbO = new JLabel("ĐÃ ĐÓNG SERVER");
		panel_1.add(lbO);
		lbO.setForeground(new Color(255, 0, 0));
		lbO.setFont(new Font("Tahoma", Font.BOLD, 17));
		lbO.setHorizontalAlignment(SwingConstants.CENTER);

		JButton btShow = new JButton("Show Client");
		btShow.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showUser();
			}
		});
		btShow.setBounds(15, 160, 160, 30);
		contentPane.add(btShow);
	}

	static void showUser() {
		txtUserOnl.setText("");
		int count = 0;

		for (ChatSocket name : ServerThread.list) {
			txtUserOnl.append("No" + (count + 1) + " - " + name.getName() + " đang tham gia chat.\n");
			count++;
		}
	}
}
