package server;

import java.net.Socket;

import javax.swing.JCheckBox;

import framework.FrameworkMVC;
import framework.LobbyView;
import framework.LoginView;

public class ClientConnection implements Runnable
{
	String userName;
	String ipAddress;
	int portNumber;
	public static boolean succesfullyConnected = false;
	public static Socket socket;
	
	public ClientConnection(String ipAddress, int portNumber, String userName)
	{
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;
		this.userName = userName;
	}
	
	public void run()
	{
		try
		{
			socket = new Socket(ipAddress, portNumber);
			succesfullyConnected = true;//Pointer skips this line if connection can not be made
			
			System.out.println("A client just connected");
			FrameworkMVC.sl.addClientToLobby(socket, userName);
			
			if(succesfullyConnected){
				LoginView.loginPanel.setVisible(false);
				LobbyView.lobbyPanel.setVisible(true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
