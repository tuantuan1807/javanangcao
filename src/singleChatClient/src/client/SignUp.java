package client;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.JButton;

public class SignUp extends JFrame {

	private JPanel contentPane;
	private JTextField tfUser;
	private JPasswordField tfPass;
	private JPasswordField tfConfirmPass;
	
	private Socket s = null;
	private ClientThread myThread;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SignUp frame = new SignUp();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SignUp() {
		
		try {
			myThread = new ClientThread(new Socket("localhost", 3333));
			myThread.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		setTitle("Đăng kí");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 543, 406);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbTitle = new JLabel("Đăng Ký");
		lbTitle.setForeground(new Color(220, 20, 60));
		lbTitle.setFont(new Font("Tahoma", Font.BOLD, 25));
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lbTitle.setBounds(200, 30, 129, 31);
		contentPane.add(lbTitle);
		
		JLabel lbUser = new JLabel("Username");
		lbUser.setHorizontalAlignment(SwingConstants.LEFT);
		lbUser.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbUser.setBounds(43, 93, 97, 22);
		contentPane.add(lbUser);
		
		JLabel lbPass = new JLabel("Password");
		lbPass.setHorizontalAlignment(SwingConstants.LEFT);
		lbPass.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbPass.setBounds(43, 149, 97, 22);
		contentPane.add(lbPass);
		
		JLabel lbConfirmPass = new JLabel("Confirm Password");
		lbConfirmPass.setHorizontalAlignment(SwingConstants.LEFT);
		lbConfirmPass.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbConfirmPass.setBounds(43, 205, 149, 22);
		contentPane.add(lbConfirmPass);
		
		tfUser = new JTextField();
		tfUser.setFont(new Font("Tahoma", Font.PLAIN, 17));
		tfUser.setBounds(195, 92, 280, 24);
		contentPane.add(tfUser);
		tfUser.setColumns(10);
		
		tfPass = new JPasswordField();
		tfPass.setFont(new Font("Tahoma", Font.PLAIN, 17));
		tfPass.setBounds(195, 148, 280, 24);
		contentPane.add(tfPass);
		
		tfConfirmPass = new JPasswordField();
		tfConfirmPass.setFont(new Font("Tahoma", Font.PLAIN, 17));
		tfConfirmPass.setBounds(195, 204, 280, 24);
		contentPane.add(tfConfirmPass);
		
		final JCheckBox chckbxNewCheckBox = new JCheckBox("Hiện mật khẩu ");
		chckbxNewCheckBox.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(chckbxNewCheckBox.isSelected()) {
					tfPass.setEchoChar((char)0);
					tfConfirmPass.setEchoChar((char)0);
				} else {
					tfPass.setEchoChar('*');
					tfConfirmPass.setEchoChar('*');
				}
			}
		});
		chckbxNewCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckbxNewCheckBox.setBounds(205, 242, 149, 21);
		contentPane.add(chckbxNewCheckBox);
		
		JButton btCreate = new JButton("Tạo tài khoản");
		btCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (s == null || s.isClosed()) {
						s = new Socket("localhost", 3333);
						System.out.println("Client connect ");
					}

					String username = tfUser.getText();
					String pass 	= tfPass.getText();
					String repass 	= tfConfirmPass.getText();
					System.out.println("Username: " + username + " - Pass: " + pass);
					
					myThread.doSendData("CMD_SIGNUP", username, pass, repass);
					clear();					
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btCreate.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btCreate.setBounds(104, 309, 171, 31);
		contentPane.add(btCreate);
		
		JButton btCancel = new JButton("Cancel");
		btCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				Login frame = new Login();
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
			}
		});
		btCancel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btCancel.setBounds(308, 309, 103, 31);
		contentPane.add(btCancel);
	}
	
	void clear() {
		tfUser.setText("");
		tfPass.setText("");
		tfConfirmPass.setText("");
	}
	
	public static void getAccount(String notif) {
		JFrame frame = new JFrame("JOptionPane showMessageDialog example");
		JOptionPane.showMessageDialog(frame, notif, "Notification", JOptionPane.INFORMATION_MESSAGE);
	}
}
	
