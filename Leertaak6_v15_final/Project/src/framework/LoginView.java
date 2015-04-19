package framework;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

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
	public static Thread ccThread;
	
	public LoginView(FrameworkView fv)
	{
		loginPanel = new JPanel(new GridLayout(4,1));
		loginPanel.setVisible(true);
		loginPanel.setBackground(Color.lightGray);
		loginPanel.setPreferredSize(new Dimension(350,250));
		loginPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		//add textfields for ip and username and login button
		AddLoginComponents(); 
		
		fv.add(loginPanel);
		fv.revalidate();
	}
	
	public void AddLoginComponents()
	{	
		ipAddressField = new JTextField("Insert ip address to connect with");
		ipAddressField.setForeground(Color.gray);
		ipAddressField.setBorder(BorderFactory.createLineBorder(Color.lightGray, 10));
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
		portNumberField.setBorder(BorderFactory.createLineBorder(Color.lightGray, 10));
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
		userNameField.setBorder(BorderFactory.createLineBorder(Color.lightGray, 10));
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
		
		loginButton = new JButton("Proceed to lobby");
		loginButton.setBackground(Color.black);
		loginButton.setForeground(Color.white);
		loginButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				if(ipAddressField.getText().contains("Insert ip address to connect with")||portNumberField.getText().contains("Insert port number to connect with")||userNameField.getText().contains("Insert an in-game username")){
					JOptionPane.showMessageDialog(FrameworkView.window,"Make sure all fields are filled in correctly","Error",JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					ipAddress = ipAddressField.getText();
					if(portNumberField.getText().matches("[0-9]+")){
						portNumber = Integer.parseInt(portNumberField.getText());
					}
	
					userName = userNameField.getText();
					
					cc = new ClientConnection(ipAddress, portNumber, userName);
					ccThread = new Thread(cc);
					ccThread.start();
					
					ClientConnection.inLobby = true;
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
