package client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatForm extends JFrame {

	private static JFrame frame;
	private static JPanel contentPane, panel, panel_chat, lbShowMsg;
	private JTextField txtMsg;
	private JList listOnl;
	static  DefaultListModel listModel;

	private Socket s = null;
	private static ClientThread myThread;

	static String user;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public ChatForm() {

		try {
			s 		 = new Socket("localhost", 5555);
			myThread = new ClientThread(s);
			myThread.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					myThread.doSendData("CMD_LOGOUT", frame.getTitle());
					s.close();
					System.exit(0);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		setBounds(100, 100, 700, 436);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 0, 685, 395);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		panel = new JPanel();
		panel.setBounds(0, 0, 644, 234);
		panel.setLayout(null);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(0, 0, 685, 395);
		panel_1.add(panel_2);
		panel_2.setLayout(null);

		JLabel lbOnl = new JLabel("ONLINE");
		lbOnl.setForeground(new Color(0, 128, 0));
		lbOnl.setHorizontalAlignment(SwingConstants.CENTER);
		lbOnl.setFont(new Font("Tahoma", Font.BOLD, 25));
		lbOnl.setBounds(507, 60, 120, 40);
		panel_2.add(lbOnl);

		final JPanel panel_list = new JPanel();
		panel_list.setBackground(new Color(255, 255, 255));
		panel_list.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel_list.setBounds(476, 59, 184, 279);
		panel_2.add(panel_list);
		panel_list.setLayout(null);

		final JPanel panel_group = new JPanel();
		panel_group.setBounds(0, 0, 184, 152);
		panel_group.setLayout(null);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(0, 0, 184, 152);
		panel_group.add(scrollPane_2);

		JList list_group = new JList();
		scrollPane_2.setViewportView(list_group);

		final JPanel panel_acount = new JPanel();
		panel_acount.setBounds(0, 41, 184, 237);
		panel_list.add(panel_acount);
		panel_acount.setLayout(null);
		listModel = new DefaultListModel<String>();

		listOnl = new JList(listModel);
		listOnl.setFont(new Font("Tahoma", Font.PLAIN, 17));
		listOnl.setBounds(0, 0, 100, 100);
		JScrollPane scrollPane = new JScrollPane(listOnl);
		scrollPane.setBounds(0, 0, 184, 237);
		panel_acount.add(scrollPane);

		panel_chat = new JPanel();
		panel_chat.setBackground(new Color(255, 255, 255));
		panel_chat.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel_chat.setBounds(26, 59, 440, 320);
		panel_2.add(panel_chat);
		panel_chat.setLayout(null);

		txtMsg = new JTextField();
		txtMsg.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtMsg.setBounds(0, 278, 440, 42);
		panel_chat.add(txtMsg);
		txtMsg.setColumns(10);

		lbShowMsg = new JPanel();
		lbShowMsg.setLayout(new BoxLayout(lbShowMsg, BoxLayout.Y_AXIS));
		lbShowMsg.setBackground(new Color(255, 255, 255));
		JScrollPane scrollPane_1 = new JScrollPane(lbShowMsg);
		scrollPane_1.setBounds(0, 0, 440, 280);
		panel_chat.add(scrollPane_1);

		JButton btSend = new JButton("SEND");
		btSend.setBounds(476, 339, 184, 40);
		panel_2.add(btSend);
		btSend.setForeground(new Color(0, 0, 128));
		btSend.setFont(new Font("Tahoma", Font.BOLD, 20));
		btSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String mess  = txtMsg.getText();
				JLabel label = new JLabel(mess);
				label.setOpaque(true);
				label.setBackground(Color.white);
				label.setFont(new Font("Tahoma", Font.PLAIN, 17));
				label.setBorder(new EmptyBorder(5, 5, 10, 5));
				lbShowMsg.add(label);
				lbShowMsg.add(Box.createRigidArea(new Dimension(10, 10)));
				lbShowMsg.revalidate();
				lbShowMsg.repaint();

				txtMsg.setText("");
				user = frame.getTitle();

				Date date  = new Date();
				String now = sdf.format(date);
				myThread.doSendData("CMD_SEND", mess, user, now);
			}
		});
		btSend.setBackground(new Color(255, 255, 255));
		btSend.setBorder(BorderFactory.createEmptyBorder());

		JButton btOut = new JButton("Log out");
		btOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc đăng xuất không ?");
				if (confirm == 0) {
					try {
						myThread.doSendData("CMD_LOGOUT", frame.getTitle());
						s.close();
						System.exit(0);
						setVisible(false);
						Login f = new Login();
						f.setVisible(true);
						f.setLocationRelativeTo(null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btOut.setFont(new Font("Tahoma", Font.BOLD, 17));
		btOut.setBounds(550, 20, 110, 25);
		panel_2.add(btOut);
		
		JButton btChange = new JButton("Change password");
		btChange.setForeground(new Color(0, 0, 255));
		btChange.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				ChangePass cp = new ChangePass(frame.getTitle());
				cp.setVisible(true);
			}
		});
		btChange.setBounds(286, 20, 180, 25);
		panel_2.add(btChange);
	}

	public static void getUsername(String username) {
		frame = new ChatForm();
		frame.setTitle(username);
		frame.setVisible(true);
		myThread.doSendData("CMD_ONLINE", username);

	}

	public static void getOnline(String online) {
		user = frame.getTitle();
		if (!(online.equals(user))) {
			listModel.addElement(online);
		}
		myThread.doSendData("CMD_OLDMSG", user);
	}

	public static void getMsg(String sender, String msg) {
		System.out.println(sender + ":  " + msg);

		JLabel label1 = new JLabel(sender + ":  " + msg);
		label1.setOpaque(true);
		label1.setForeground(new Color(0, 0, 205));
		label1.setFont(new Font("Tahoma", Font.BOLD, 17));
		label1.setBorder(new EmptyBorder(5, 5, 5, 10));
		lbShowMsg.add(label1);
		lbShowMsg.add(Box.createRigidArea(new Dimension(10, 10)));
		lbShowMsg.revalidate();
		lbShowMsg.repaint();
	}

	public static void getRemove(String name) {
		listModel.removeElement(name);
	}

	public static void getOldMsg(String sender, String message) {
		String userSend = frame.getTitle();		
		if (sender.equals(userSend)) {
			JLabel item = new JLabel();
			item.setOpaque(true);
			item.setBackground(Color.white);
			item.setFont(new Font("Tahoma", Font.PLAIN, 17));
			item.setBorder(new EmptyBorder(5, 5, 10, 5));
			item.setText(message);
			lbShowMsg.add(item);
			lbShowMsg.add(Box.createRigidArea(new Dimension(10, 10)));
			lbShowMsg.revalidate();
			lbShowMsg.repaint();
		} else {
			JLabel items = new JLabel(sender);
			items.setOpaque(true);
			items.setForeground(new Color(0, 0, 205));
			items.setFont(new Font("Tahoma", Font.BOLD, 17));
			items.setBorder(new EmptyBorder(5, 5, 5, 10));
			items.setText(sender + ":  " + message);
			lbShowMsg.add(items);
			lbShowMsg.add(Box.createRigidArea(new Dimension(10, 10)));
			lbShowMsg.revalidate();
			lbShowMsg.repaint();
		}
	}
}
