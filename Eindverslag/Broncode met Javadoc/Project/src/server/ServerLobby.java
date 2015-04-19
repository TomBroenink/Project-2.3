package server;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JCheckBox;

import framework.LobbyView;

/**
 * The Class ServerLobby.
 */
public class ServerLobby 
{
	
	/** The players in lobby. */
	public static JCheckBox[] playersInLobby = new JCheckBox[1000];
	
	/** The user names. */
	public static ArrayList<String> userNames = new ArrayList<String>();
	
	/** The selected opponent. */
	static String selectedOpponent;
	
	/** The selected opponent username. */
	public static String selectedOpponentUsername;
	
	/** The i. */
	public static int i = 1;
	
	/**
	 * Instantiates a new server lobby.
	 */
	public ServerLobby()
	{
		setComputer();
	}
	
	/**
	 * Adds the client to lobby.
	 *
	 * @param client the client
	 * @param userName the user name
	 */
	public void addClientToLobby(Socket client, String userName)
	{	
		if(!alreadyInLobby(userName))
		{
			updateLobbySize();
			
			userNames.add(userName);
			
			String clientIP = "Unknown";
			String clientPort = "Unknown";
			String clientLocalPort = "Unknown";
			String name;
			
			if(client!=null){
				clientIP = client.getInetAddress().toString();
				clientPort = Integer.toString(client.getPort());
				clientLocalPort = Integer.toString(client.getLocalPort());
			}
			
			if(clientIP.equals("Unknown")){
				name = "Name";
			}
			else{
				name = "You";
			}
			
			JCheckBox temp = new JCheckBox(name+" : "+userName+" | IP : "+clientIP+" | Port : "+clientPort+" | Localport : "+clientLocalPort+"  ");
			
			
			int c =i;
			
			temp.addActionListener(new ActionListener(){
	
				public void actionPerformed(ActionEvent arg0) {
					if(temp.isSelected()){
						//Unselect alles
						for(int a = 0;a<1000;a++){
							if(playersInLobby[a]!=null){
								playersInLobby[a].setOpaque(false);
								playersInLobby[a].setSelected(false);
							}
						}
						//Set 1 opnieuw selected
						temp.setSelected(true);
						temp.setBackground(Color.lightGray);
						temp.setOpaque(true);
						selectedOpponent = temp.getText();
						
						int beginIndex = selectedOpponent.indexOf(":")+2;
						int endIndex = selectedOpponent.indexOf("|")-1;
						selectedOpponentUsername = selectedOpponent.substring(beginIndex, endIndex);
					}
					else{
						temp.setSelected(true);
					}
				}
			});
			
			playersInLobby[i] = temp;
			LobbyView.playerPanel.add(playersInLobby[i]);
			repaintAndValidateLobby();
			i++;
		}
	}
	
	/**
	 * Update lobby size.
	 */
	public static void updateLobbySize(){
		//check if lobby is full
		int playerPanelHeight = LobbyView.playerPanel.getHeight();
		int newPlayerPanelHeight = playerPanelHeight + (3*43);
		int playerPanelWidth  = LobbyView.playerPanel.getWidth();
		int lobbyRows = LobbyView.lobbyLayout.getRows();
		int newLobbyRows = lobbyRows + 3;
		
		
		if((lobbyRows-1) == userNames.size()){
			LobbyView.lobbyLayout.setRows(newLobbyRows);
			LobbyView.playerPanel.setPreferredSize(new Dimension(playerPanelWidth, newPlayerPanelHeight));
			LobbyView.lobbyPanel.repaint();
			LobbyView.lobbyPanel.revalidate();
		}
	}
	
	/**
	 * Gets the players in lobby.
	 *
	 * @return the players in lobby
	 */
	public JCheckBox[] getPlayersInLobby(){
		return playersInLobby;
	}
	
	/**
	 * Gets the usernames.
	 *
	 * @return the usernames
	 */
	public ArrayList<String> getUsernames(){
		return userNames;
	}
	
	/**
	 * Clear usernames.
	 */
	public static void clearUsernames(){
		userNames.removeAll(userNames);
	}
	
	/**
	 * Already in lobby.
	 *
	 * @param userName the user name
	 * @return true, if successful
	 */
	public boolean alreadyInLobby(String userName){
		for (String player : userNames){
			  if (player.equals(userName)){
				    return true;
				  }
			}
		return false;
	}
	
	/**
	 * Repaint and validate lobby.
	 */
	public static void repaintAndValidateLobby(){
		LobbyView.playerPanel.repaint();
		LobbyView.playerPanel.revalidate();
	}
	
	/**
	 * Adds the listener of computer.
	 */
	public static void addListenerOfComputer(){
		playersInLobby[0].addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if(playersInLobby[0].isSelected()){
					//Unselect alles
					for(int a = 0;a<1000;a++){
						if(playersInLobby[a]!=null){
							playersInLobby[a].setOpaque(false);
							playersInLobby[a].setSelected(false);
						}
					}
					
					//Set 1 opnieuw selected
					playersInLobby[0].setSelected(true);
					playersInLobby[0].setBackground(Color.lightGray);
					playersInLobby[0].setOpaque(true);
					selectedOpponent = playersInLobby[0].getText();
					selectedOpponentUsername = "the Computer";
					System.out.println(selectedOpponent);
				}
				else{
					playersInLobby[0].setSelected(true);
				}
			}
		});
	}
	
	/**
	 * Sets the computer.
	 */
	public static void setComputer(){
		LobbyView.playerPanel.add(ServerLobby.playersInLobby[0] = new JCheckBox("Computer"));
		playersInLobby[0].setSelected(true);
		playersInLobby[0].setOpaque(true);
		playersInLobby[0].setBackground(Color.lightGray);
		selectedOpponent = playersInLobby[0].getText();
		selectedOpponentUsername = "the Computer";
		
		addListenerOfComputer();
	}
	
	/**
	 * Removes the clients from lobby.
	 */
	public static void removeClientsFromLobby(){
		LobbyView.playerPanel.removeAll();
		setComputer();
		repaintAndValidateLobby();
	}
}
