package client;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

import java.io.IOException;
import java.net.Socket;

public class ChangePass extends JFrame {

	private JPanel contentPane;
	private JPasswordField txtCurrent;
	private JPasswordField txtNew;
	private JPasswordField txtRepeat;
	private ClientThread myThread;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChangePass frame = new ChangePass("");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ChangePass(final String username) {
		try {
			myThread = new ClientThread(new Socket("localhost", 5555));
			myThread.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		setTitle("Change Pass");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 495, 325);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lbCurrent = new JLabel("Current Pass");
		lbCurrent.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lbCurrent.setBounds(35, 80, 130, 26);
		contentPane.add(lbCurrent);

		JLabel lbNew = new JLabel("New Pass");
		lbNew.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lbNew.setBounds(35, 125, 130, 26);
		contentPane.add(lbNew);

		txtCurrent = new JPasswordField();
		txtCurrent.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtCurrent.setBounds(190, 80, 259, 26);
		contentPane.add(txtCurrent);

		txtNew = new JPasswordField();
		txtNew.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtNew.setBounds(190, 125, 259, 26);
		contentPane.add(txtNew);

		txtRepeat = new JPasswordField();
		txtRepeat.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtRepeat.setBounds(190, 170, 259, 26);
		contentPane.add(txtRepeat);

		JLabel lbRepeat = new JLabel("Repeat Pass");
		lbRepeat.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lbRepeat.setBounds(35, 170, 130, 26);
		contentPane.add(lbRepeat);
		JButton btnOK = new JButton("Change");
		btnOK.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				String current_pass = txtCurrent.getText();
				String new_pass = txtNew.getText();
				String repeat_pass = txtRepeat.getText();
				System.out.println("Username: " + username + " - Current pass: " + current_pass + " - New Pass: " + new_pass + " - Reapeat Pass: " + repeat_pass);
				
				myThread.doSendData("CMD_CHANGEPASS", username, current_pass, new_pass, repeat_pass);
				
				txtCurrent.setText("");
				txtNew.setText("");
				txtRepeat.setText("");
				
			}
		});
		btnOK.setBounds(90, 245, 131, 31);
		contentPane.add(btnOK);
		
		final JCheckBox chckbxNewCheckBox = new JCheckBox("Show");
		chckbxNewCheckBox.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(chckbxNewCheckBox.isSelected()) {
					txtCurrent.setEchoChar((char)0);
					txtNew.setEchoChar((char)0);
					txtRepeat.setEchoChar((char)0);
				} else {
					txtCurrent.setEchoChar('*');
					txtNew.setEchoChar('*');
					txtRepeat.setEchoChar('*');
				}
			}
		});
		chckbxNewCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckbxNewCheckBox.setBounds(190, 205, 93, 21);
		contentPane.add(chckbxNewCheckBox); 
		
		JButton btnBackToChat = new JButton("Cancel");
		btnBackToChat.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnBackToChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnBackToChat.setBounds(260, 245, 131, 32);
		contentPane.add(btnBackToChat);
		
		JLabel lblNewLabel = new JLabel("Đổi mật khẩu");
		lblNewLabel.setForeground(new Color(220, 20, 60));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblNewLabel.setBounds(120, 21, 250, 31);
		contentPane.add(lblNewLabel);
	}
	
	public static void getChangePass(String notif) {
		JFrame frame = new JFrame("JOptionPane showMessageDialog example");
		JOptionPane.showMessageDialog(frame, notif, "Notification", JOptionPane.INFORMATION_MESSAGE);
		frame.setVisible(false);
	}
}
