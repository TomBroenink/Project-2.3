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

/**
 * The Class FrameworkController.
 */
public class FrameworkController 
{
	
	/** The lv. */
	static LobbyView lv;
	
	/** The AI module. */
	public static AbstractAI AIModule; 
	
	/**
	 * Instantiates a new framework controller.
	 *
	 * @param lv the lv
	 */
	public FrameworkController(LobbyView lv){
		this.lv = lv;
		addActionListeners();
	}
	
	/**
	 * Adds the action listeners.
	 */
	public void addActionListeners(){
		lv.addActionListener(lv.backToLogin,new backToLoginListener());
		lv.addActionListener(lv.challengeOpponent, new challengeOpponentListener());
		lv.addActionListenerJComboBox(lv.chooseGame, new chooseGameListener());
		lv.addActionListener(lv.subscribe, new subscribeListener());
	}
	
	/**
	 * The listener interface for receiving backToLogin events.
	 * The class that is interested in processing a backToLogin
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addbackToLoginListener<code> method. When
	 * the backToLogin event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see backToLoginEvent
	 */
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
	
	/**
	 * The listener interface for receiving challengeOpponent events.
	 * The class that is interested in processing a challengeOpponent
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addchallengeOpponentListener<code> method. When
	 * the challengeOpponent event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see challengeOpponentEvent
	 */
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
	
	/**
	 * The listener interface for receiving chooseGame events.
	 * The class that is interested in processing a chooseGame
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addchooseGameListener<code> method. When
	 * the chooseGame event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see chooseGameEvent
	 */
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
	
	/**
	 * The listener interface for receiving subscribe events.
	 * The class that is interested in processing a subscribe
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addsubscribeListener<code> method. When
	 * the subscribe event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see subscribeEvent
	 */
	class subscribeListener implements ActionListener{	
		public void actionPerformed(ActionEvent e) {
			int result = JOptionPane.showConfirmDialog(FrameworkView.window,"Are you sure you want to subscribe \nfor a game of "+LobbyView.gameChosen+"?",null, JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.YES_OPTION) {
				ClientConnection.subscribe(lv.gameChosen);
			}
		}
	}
	
	/**
	 * Enable game view.
	 *
	 * @param enable the enable
	 */
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
	
	

