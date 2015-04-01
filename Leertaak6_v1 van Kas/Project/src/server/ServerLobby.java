package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JCheckBox;

import framework.LobbyView;

public class ServerLobby 
{
	public static ArrayList<Socket> Lobby;
	public static ArrayList<String> userNames;
	public static JCheckBox[] playersInLobby = new JCheckBox[10];
	String selectedOpponent;
	public static String selectedOpponentUsername;
	int i = 0;
	
	public ServerLobby()
	{
		Lobby = new ArrayList<Socket>();
		userNames = new ArrayList<String>();
	}
	
	public void addClientToLobby(Socket client, String userName){
		Lobby.add(client);
		userNames.add(userName);
			
		//Make a checkbox containing player name + an index so simular names can be seperated.
		JCheckBox temp = new JCheckBox("Name : "+userName+" | IP : "+client.getInetAddress()+" | Port : "+client.getPort()+" | Localport : "+client.getLocalPort());
		int c =i;
		temp.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if(temp.isSelected()){
					//Unselect alles
					for(int a = 0;a<10;a++){
						if(playersInLobby[a]!=null)
						playersInLobby[a].setSelected(false);
					}
					//Set 1 opnieuw selected
					temp.setSelected(true);
					selectedOpponent = temp.getText();
					
					
					int beginIndex = selectedOpponent.indexOf(":")+2;
					int endIndex = selectedOpponent.indexOf("|")-1;
					selectedOpponentUsername = selectedOpponent.substring(beginIndex, endIndex);
					System.out.println(selectedOpponentUsername);
					System.out.println(selectedOpponent);
				}		
			}
		});
		playersInLobby[i] = temp;
		LobbyView.playerPanel.add(playersInLobby[i]);
		i++;
	}
	
	public ArrayList<Socket> getLobby(){
		return Lobby;
	}
	
	public ArrayList<String> getPlayers(){
		return userNames;
	}
	
	public String getLastPlayer(){
		return userNames.get(userNames.size()-1);
	}
}
