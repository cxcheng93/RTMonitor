package userInterface;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.awt.Font;
import javax.swing.JButton;

import RTMonitor.runMonitor;

import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class homePage extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	public String username;
	public String password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					homePage frame = new homePage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public homePage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUserName = new JLabel("User name:");
		lblUserName.setBounds(93, 82, 65, 34);
		contentPane.add(lblUserName);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(93, 127, 75, 34);
		contentPane.add(lblPassword);
		
		textField = new JTextField();
		textField.setBounds(159, 89, 113, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
//		textField_1 = new JTextField();
//		textField_1.setBounds(159, 134, 113, 20);
//		contentPane.add(textField_1);
//		textField_1.setColumns(10);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblLogin.setBounds(55, 34, 103, 37);
		contentPane.add(lblLogin);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					String Username = textField.getText();
					String Password = passwordField.getText();
					if (Username.equals("admin") && Password.equals("123")) {
						JOptionPane.showMessageDialog(null, "Login successful");
						String [] args = new String[0];
						runMonitor.main(args);
						contentPane.setVisible(false);
					}
					else {
						JOptionPane.showMessageDialog(null, "Invalid User");
						textField.setText("");
						passwordField.setText("");
					}
				}
				catch(Exception e){
					System.out.println("Expception");
				}
			}
		});
		btnLogin.setBounds(295, 196, 89, 23);
		contentPane.add(btnLogin);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(159, 134, 113, 20);
		contentPane.add(passwordField);
		passwordField.setColumns(10);
	}
}
