package framework;

import game.GameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import server.ClientConnection;
import server.ComputerClient;
import server.ServerLobby;
import AI.AbstractAI;
import AI.OthelloAI;
import AI.TicTacToeAI;

public class FrameworkController 
{
	static LobbyView lv;
	public static AbstractAI AIModule; 
	
	public FrameworkController(LobbyView lv){
		this.lv = lv;
		addActionListeners();
	}
	
	public void addActionListeners(){
		lv.addActionListener(lv.backToLogin,new backToLoginListener());
		lv.addActionListener(lv.challengeOpponent, new challengeOpponentListener());
		lv.addActionListenerJComboBox(lv.chooseGame, new chooseGameListener());
		lv.addActionListener(lv.subscribe, new subscribeListener());
	}
	
	class backToLoginListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			int result = JOptionPane.showConfirmDialog(FrameworkView.window,"Are you sure you want to disconnect from te server?",null, JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.YES_OPTION) {
				LoginView.loginPanel.setVisible(true);
				lv.lobbyPanel.setVisible(false);
			
				try {
					ClientConnection.socket.close();
					LoginView.ccThread.interrupt();
					
					//reset all lobby values
					ClientConnection.inLobby = false;
					ClientConnection.gameList.removeAll(ClientConnection.gameList);
					ServerLobby.userNames.removeAll(ServerLobby.userNames);
					
					ServerLobby.i = 0;
					lv.playerPanel.removeAll();
					lv.playerPanel.add(ServerLobby.playersInLobby[0] = new JCheckBox("Computer"));
					ServerLobby.addListenerOfComputer();;
					lv.revalidate();
				} 
				catch (IOException e) {
					JOptionPane.showMessageDialog(FrameworkView.window,e.toString(),"Error",JOptionPane.ERROR_MESSAGE); e.printStackTrace();
				}
			}
		}	
	}
	
	class challengeOpponentListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			if(ServerLobby.selectedOpponentUsername.equals(ClientConnection.userName)){
				JOptionPane.showMessageDialog(FrameworkView.window,"You can not challenge yourself",null, JOptionPane.CANCEL_OPTION);
			}
			else{
				int result = JOptionPane.showConfirmDialog(FrameworkView.window,"Are you sure you want to challenge "+ServerLobby.selectedOpponentUsername+"\nfor a game of "+LobbyView.gameChosen+"?",null, JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.YES_OPTION) {
					if(ServerLobby.selectedOpponentUsername.equals("the Computer")){
						ClientConnection.subscribe(LobbyView.gameChosen);
						ComputerClient pcClient = new ComputerClient(ClientConnection.ipAddress ,ClientConnection.portNumber);
						Thread pcClientThread = new Thread(pcClient);
						pcClientThread.start();
					}
					else{	
						GameProtocol.challenge(ServerLobby.selectedOpponentUsername);	
					}	
				} 
			}
		}	
	}
	
	class chooseGameListener implements ActionListener{	
		public void actionPerformed(ActionEvent e) {
			lv.gameChosen = lv.chooseGame.getSelectedItem().toString();
			
			if(lv.gameChosen.equalsIgnoreCase("Tic-Tac-Toe")){
				AIModule = new TicTacToeAI();
				GameView.changeBoardSize(3,3,90);
				
			}
			else if(lv.gameChosen.equalsIgnoreCase("Reversi")){
				AIModule = new OthelloAI();
				GameView.changeBoardSize(8,8,20);
				
			}
		}
	}
	
	class subscribeListener implements ActionListener{	
		public void actionPerformed(ActionEvent e) {
			int result = JOptionPane.showConfirmDialog(FrameworkView.window,"Are you sure you want to subscribe \nfor a game of "+LobbyView.gameChosen+"?",null, JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.YES_OPTION) {
				ClientConnection.subscribe(lv.gameChosen);
			}
		}
	}
	
	public static void enableGameView(boolean enable){
		GameView.clearBoard();
		
		lv.setVisible(!enable);
		if(GameView.AISwitch.getText().contains("OFF"))GameView.AISwitch.doClick();
		GameView.messageLabel.setText("Initializing.. ");
		GameView.revalidate();
		GameView.gamePanel.setVisible(enable);
		ClientConnection.inLobby = !enable;
		if(enable){
			FrameworkView.window.setTitle(FrameworkView.window.getTitle()+" | Opponent : "+ServerLobby.selectedOpponentUsername);
		}
		else{
			FrameworkView.window.setTitle("DKSolutions");
		}
		
		GameView.gamePanel.repaint();
		GameView.gamePanel.revalidate();
	}
}
	
	

