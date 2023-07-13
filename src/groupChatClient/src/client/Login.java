package client;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;

import java.io.IOException;
import java.net.Socket;

public class Login extends JFrame {

	private static JFrame frame;
	private JPanel contentPane;
	private JTextField txtUserName;
	private JPasswordField txtPassword;
	static DefaultListModel listModel;

	private static String username;
	private static Socket s;
	private ClientThread myThread;
	static ChatForm chat;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Login();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Login() {

		try {
			s 		 = new Socket("localhost", 5555);
			myThread = new ClientThread(s);
			myThread.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		setTitle("Chat Application");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 551, 330);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lbDN = new JLabel("Đăng Nhập");
		lbDN.setForeground(new Color(0, 191, 255));
		lbDN.setHorizontalAlignment(SwingConstants.CENTER);
		lbDN.setFont(new Font("Tahoma", Font.BOLD, 25));
		lbDN.setBounds(305, 50, 145, 31);
		contentPane.add(lbDN);

		JLabel img = new JLabel("");
		img.setHorizontalAlignment(SwingConstants.CENTER);
		img.setIcon(new ImageIcon(new ImageIcon("D:\\Monhoc\\laptrinhjava\\thihockyjava\\img\\firebase-chat-app.png").getImage()
				.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
		img.setBounds(10, 43, 198, 206);
		contentPane.add(img);

		JLabel lbUser = new JLabel("Username");
		lbUser.setHorizontalAlignment(SwingConstants.LEFT);
		lbUser.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lbUser.setBounds(218, 113, 97, 25);
		contentPane.add(lbUser);

		txtUserName = new JTextField();
		txtUserName.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtUserName.setBounds(345, 113, 181, 25);
		contentPane.add(txtUserName);
		txtUserName.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPassword.setBounds(218, 161, 97, 25);
		contentPane.add(lblPassword);

		JButton btDN = new JButton("Đăng nhập");
		btDN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				username 	= txtUserName.getText();
				String pass = txtPassword.getText();
				myThread.doSendData("CMD_LOGIN", username, pass);
			}
		});
		btDN.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btDN.setBounds(240, 245, 136, 31);
		contentPane.add(btDN);

		JButton btDK = new JButton("Đăng ký");
		btDK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SignUp frame = new SignUp();
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
				txtUserName.setText("");
				txtPassword.setText("");
				setVisible(false);
			}
		});
		btDK.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btDK.setBounds(390, 245, 136, 31);
		contentPane.add(btDK);

		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtPassword.setBounds(345, 161, 181, 25);
		contentPane.add(txtPassword);

		final JCheckBox chckbxNewCheckBox = new JCheckBox("Hiện");
		chckbxNewCheckBox.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (chckbxNewCheckBox.isSelected()) {
					txtPassword.setEchoChar((char) 0);
				} else {
					txtPassword.setEchoChar('*');
				}
			}
		});
		chckbxNewCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckbxNewCheckBox.setBounds(348, 197, 93, 21);
		contentPane.add(chckbxNewCheckBox);

		JLabel lbCpr = new JLabel("© Tuấn - Bích - Thảo");
		lbCpr.setHorizontalAlignment(SwingConstants.CENTER);
		lbCpr.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lbCpr.setBounds(35, 251, 145, 25);
		contentPane.add(lbCpr);
	}

	public static void getLogin(String notif) {
		if (notif.equals("Login successful!")) {

			sendUser(username);
			frame.setVisible(false);
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		JFrame frame = new JFrame("JOptionPane showMessageDialog example");
		JOptionPane.showMessageDialog(frame, notif, "Notification", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void sendUser(String username) {
		chat.getUsername(username);
	}
}
