package framework;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;

import server.ClientConnection;

public class LoginView 
{
	public static JPanel loginPanel;
	JTextField ipAddressField;
	JTextField portNumberField;
	JTextField userNameField;
	JButton loginButton;
	String ipAddress;
	int portNumber;
	String userName;
	ClientConnection cc;
	
	public LoginView(FrameworkView fv)
	{
		loginPanel = new JPanel(new GridLayout(4,1));
		loginPanel.setVisible(true);
		loginPanel.setBackground(Color.black);
		loginPanel.setPreferredSize(new Dimension(350,250));
		loginPanel.setBorder(BorderFactory.createLineBorder(Color.black, 3));
		
		//add textfields for ip and username and login button
		AddLoginComponents();
		
		fv.add(loginPanel);
		fv.revalidate();
	}
	
	public void AddLoginComponents()
	{
		ipAddressField = new JTextField("Insert ip address to connect with");
		ipAddressField.setForeground(Color.gray);
		ipAddressField.setBorder(BorderFactory.createLineBorder(Color.black, 10));
		ipAddressField.setHorizontalAlignment(SwingConstants.CENTER);
		ipAddressField.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent arg0) {
				if(ipAddressField.getText().contains("Insert ip address to connect with")){
					ipAddressField.setText("");				
					ipAddressField.setForeground(Color.black);
				}
			}

			public void focusLost(FocusEvent arg0) {
				if(ipAddressField.getText().length()<1){
					ipAddressField.setText("Insert ip address to connect with");				
					ipAddressField.setForeground(Color.gray);
				}
			}
			
		});
		
		portNumberField = new JTextField("Insert port number to connect with");
		portNumberField.setForeground(Color.gray);
		portNumberField.setBorder(BorderFactory.createLineBorder(Color.black, 10));
		portNumberField.setHorizontalAlignment(SwingConstants.CENTER);
		portNumberField.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent arg0) {
				if(portNumberField.getText().contains("Insert port number to connect with")){
					portNumberField.setText("");				
					portNumberField.setForeground(Color.black);
				}
			}

			public void focusLost(FocusEvent arg0) {
				if(portNumberField.getText().length()<1){
					portNumberField.setText("Insert port number to connect with");				
					portNumberField.setForeground(Color.gray);
				}
			}
			
		});
		
		userNameField = new JTextField("Insert an in-game username");
		userNameField.setForeground(Color.gray);
		userNameField.setBorder(BorderFactory.createLineBorder(Color.black, 10));
		userNameField.setHorizontalAlignment(SwingConstants.CENTER);
		userNameField.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent arg0) {
				if(userNameField.getText().contains("Insert an in-game username")){
					userNameField.setText("");				
					userNameField.setForeground(Color.black);
				}
			}

			public void focusLost(FocusEvent arg0) {
				if(userNameField.getText().length()<1){
					userNameField.setText("Insert an in-game username");				
					userNameField.setForeground(Color.gray);
				}
			}
			
		});
		
		//!!!!!!!!!!Listener moet misschien in controller klasse maar is denk ik een beetje teveel van het goede
		loginButton = new JButton("Proceed to lobby");
		loginButton.setBorder(BorderFactory.createLineBorder(Color.black, 10));
		loginButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				if(ipAddressField.getText().contains("Insert ip address to connect with")||portNumberField.getText().contains("Insert port number to connect with")||userNameField.getText().contains("Insert an in-game username")){
					System.out.println("Make sure all fields are filled in correctly");
				}
				else
				{
					ipAddress = ipAddressField.getText();
					if(portNumberField.getText().matches("[0-9]+")){
						portNumber = Integer.parseInt(portNumberField.getText());
					}
					else{
						System.out.println("Port shoudl be an integer value");
					}
					userName = userNameField.getText();
					
					cc = new ClientConnection(ipAddress, portNumber, userName);
					Thread ccThread = new Thread(cc);
					ccThread.start();
				}
			}
		});
		
		loginPanel.add(ipAddressField);
		loginPanel.add(portNumberField);
		loginPanel.add(userNameField);
		loginPanel.add(loginButton); 
	}
	
	public void add(Component component)
	{
		loginPanel.add(component);
	}
}
