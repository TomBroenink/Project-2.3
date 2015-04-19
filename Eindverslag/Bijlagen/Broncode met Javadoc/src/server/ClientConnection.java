package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import framework.FrameworkController;
import framework.FrameworkMVC;
import framework.FrameworkView;
import framework.LobbyView;
import framework.LoginView;
import game.GameController;
import game.GameView;

/**
 * The Class ClientConnection.
 */
public class ClientConnection implements Runnable
{
	
	/** The user name. */
	public static String userName;
	
	/** The in lobby. */
	public static boolean inLobby = false;
	
	/** The in game. */
	public static boolean inGame = false;
	
	/** The may move. */
	public static boolean mayMove = false;
	
	/** The socket. */
	public static Socket socket;
	
	/** The last error. */
	public static String lastError;
	
	/** The in. */
	public static BufferedReader in = null;
	
	/** The out. */
	public static PrintWriter out = null;
	
	/** The turn timer. */
	public static Thread turnTimer;
	
	/** The game list. */
	public static ArrayList<String> gameList = new ArrayList<String>();
	
	/** The ip address. */
	public static String ipAddress;
	
	/** The player one. */
	public static String playerOne = "";
	
	/** The player char. */
	public static String playerChar;
	
	/** The previous lobby. */
	String previousLobby;
	
	/** The run once. */
	boolean runOnce;
	
	/** The game list empty. */
	boolean gameListEmpty = true;
	
	/** The port number. */
	public static int portNumber;
	
	/**
	 * Instantiates a new client connection.
	 *
	 * @param ipAddress the ip address
	 * @param portNumber the port number
	 * @param userName the user name
	 */
	public ClientConnection(String ipAddress, int portNumber, String userName)
	{
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;
		this.userName = userName;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		String line;
		
		try
		{
			socket = new Socket(ipAddress, portNumber);
			
			addClientToLobby();
		
			LoginView.loginPanel.setVisible(false);
			LobbyView.lobbyPanel.setVisible(true);
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
		  	out = new PrintWriter(socket.getOutputStream(), true);	 
		
			skipLines(2,in);
		  	
			login(userName);
			checkForErrors(in.readLine());
		
			getGameList();
			
			skipLines(1,in);
				
			while(true)
			{
				line = in.readLine();	
			
				if(line.contains("ERR")){
					checkForErrors(line);
				}
				
				if(!line.contains("OK")){
					if(inLobby){
						updateGameList(line);
						checkForActiveMatches(line);
						checkForIncomingChallenges(line);
						checkForNewPlayers(line);
					}
					if(inGame){
						updateBoardAfterMove(line);
						enablePlayerToMove(isItYourTurn(line));
						checkGameStatus(line);
						System.out.println(line);
					}
				}
			}
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(FrameworkView.window,e.toString(),"Error",JOptionPane.ERROR_MESSAGE); e.printStackTrace();
		}
		
	}
	
	/**
	 * Make ai move.
	 */
	public static void makeAIMove(){
		int position = 23;
		
		if(LobbyView.chooseGame.getSelectedItem().toString().equals("Tic-tac-toe")){
			position = 4;
			Random random=new Random();  
			while(!isEmpty(position)){
				position =random.nextInt(9);
			}
		}
		else{
			position = FrameworkController.AIModule.getBestMove(FrameworkController.AIModule.getCurrentPlayer());
		}
		
		if(mayMove){
			move(position);
			GameView.updateGameBoard(position, playerChar);
			mayMove = false;
		}
		GameView.boardPanel.repaint();
	}
	
	/**
	 * Checks if is empty.
	 *
	 * @param position the position
	 * @return true, if is empty
	 */
	private static boolean isEmpty(int position){
		return GameView.boardPosition.get(position).getText().equals(" ");
	}
	
	/**
	 * Check for errors.
	 *
	 * @param line the line
	 */
	public void checkForErrors(String line){
		if(line.contains("ERR")){
			JOptionPane.showMessageDialog(FrameworkView.window,line,"Error",JOptionPane.ERROR_MESSAGE);
			lastError = line;
		}
		if(line.contains("Duplicate")){
			LobbyView.lobbyPanel.setVisible(false);
			LoginView.loginPanel.setVisible(true);
			inLobby = false;
		}
	}
	
	/**
	 * Skip lines.
	 *
	 * @param lines the lines
	 * @param bfIn the bf in
	 */
	public void skipLines(int lines, BufferedReader bfIn){
		for(int i = 0;i<lines;i++){
			try {
				bfIn.readLine();
			} 
			catch (Exception e) {
				JOptionPane.showMessageDialog(FrameworkView.window,e.toString(),"Error",JOptionPane.ERROR_MESSAGE); e.printStackTrace();
			}
		}
	}
	
	/**
	 * Update board after move.
	 *
	 * @param line the line
	 */
	public void updateBoardAfterMove(String line){
		String fullLine = line;
		String player;
		String position;
		
		if(!line.contains("PLAYERTOMOVE")){
			if(line.contains("MOVE")){
				line  = fullLine.substring(fullLine.indexOf("PLAYER:")+9);
				
				player = line.substring(0, line.indexOf("\""));
				
				line = fullLine.substring(fullLine.indexOf("MOVE:")+7);
				position = line.substring(0, line.indexOf("\""));
				int boardPosition = Integer.parseInt(position);
				
				if(!player.equals(userName)){
					if(playerChar.equalsIgnoreCase("X")){
						GameView.updateGameBoard(boardPosition, "O");
					}
					else{
						GameView.updateGameBoard(boardPosition, "X");
					}
				}
			}
		}
		
	}
	
	/**
	 * Update game list.
	 *
	 * @param line the line
	 */
	public void updateGameList(String line){
		if(line.contains("GAMELIST")){
			checkForGames(line);
			LobbyView.updateGameComboBox(gameList);
			if(LobbyView.chooseGame.getItemAt(0) != null){
				LobbyView.chooseGame.setSelectedIndex(0);
			}
		}
	}
	
	/**
	 * Update players online label.
	 */
	public void updatePlayersOnlineLabel(){
		int i = ServerLobby.userNames.size();
		LobbyView.updatePlayersInLobby(i);
	}

	//checks for games and adds them to e dropdown menu
	/**
	 * Check for games.
	 *
	 * @param line the line
	 */
	public void checkForGames(String line){
		String temp = "";
		while(line.contains("\""))
		{	
			int beginIndex = line.indexOf("\"")+1;
			line = line.substring(beginIndex);
			
			int secondIndex = line.indexOf("\"");
			
			if(line.contains("\""))
			temp = line.substring(0,secondIndex);  
			
			line = line.substring(secondIndex+1);
			
			gameList.add(temp);
			gameListEmpty = false;
		}
	}
	
	/**
	 * Check game status.
	 *
	 * @param line the line
	 */
	public void checkGameStatus(String line){
		if(line.contains("LOSS")){
			GameView.messageLabel.setText("Game has ended");
			GameView.revalidate();
			JOptionPane.showMessageDialog(FrameworkView.window,getPanel("LOSS"),"You lose!",JOptionPane.PLAIN_MESSAGE);
			backToLobby();
		}
		else if(line.contains("WIN")){
			GameView.messageLabel.setText("Game has ended");
			GameView.revalidate();
			JOptionPane.showMessageDialog(FrameworkView.window,getPanel("WIN"),"Victory!",JOptionPane.PLAIN_MESSAGE);
			backToLobby();
		}
		else if(line.contains("DRAW")){
			GameView.messageLabel.setText("Game has ended");
			GameView.revalidate();
			JOptionPane.showMessageDialog(FrameworkView.window,getPanel("DRAW"),"It's a draw!",JOptionPane.PLAIN_MESSAGE);
			backToLobby();
		}	
	}
	
	/**
	 * Back to lobby.
	 */
	public void backToLobby(){
		FrameworkController.enableGameView(false);
		GameView.clearBoard();
		inGame = false;
		inLobby = true;
		out.println("get playerlist");
	}
	
	/**
	 * Checks if is it your turn.
	 *
	 * @param line the line
	 * @return true, if is it your turn
	 */
	public boolean isItYourTurn(String line){
		if(line.contains("YOURTURN") || line.contains("PLAYERTOMOVE: \""+userName)){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Enable player to move.
	 *
	 * @param yourTurn the your turn
	 */
	public void enablePlayerToMove(boolean yourTurn){
		if(yourTurn){
			mayMove = true;
			if(GameController.AIPlaying){
				makeAIMove();
				GameView.messageLabel.setText("AI is playing");
			}
			else {
				GameView.messageLabel.setText("It is your turn");
				GameView.messageLabel.revalidate();
			}
		}
		else{
			if(mayMove){
				GameView.messageLabel.setText("It is your turn");
				GameView.messageLabel.revalidate();
			}
			else{
				GameView.messageLabel.setText("Waiting for opponent..");
			}
			GameView.removeHint();
		}
		GameView.revalidate();
	}
	
	/**
	 * Check for active matches.
	 *
	 * @param line the line
	 */
	public void checkForActiveMatches(String line){
		if(line.contains("MATCH")){
			if(line.contains("PLAYERTOMOVE: \"")){
				playerOne = line.substring(line.indexOf("PLAYERTOMOVE: \"")+15);
				playerOne = playerOne.substring(0,playerOne.indexOf(",")-1);
				
				if(playerOne.equalsIgnoreCase(ClientConnection.userName)){
					playerChar = "X";
				}
				else{
					playerChar = "O";
				}
			}
			if(line.contains("OPPONENT")){
				line = line.substring(line.indexOf("OPPONENT: \"")+11, line.lastIndexOf("\""));
				for(int i = 0;i<ServerLobby.userNames.size();i++){
					if(ServerLobby.userNames.get(i).toString().equals(line)){
						ServerLobby.selectedOpponentUsername = line;
					}
				}
				
			}
			inLobby= false;
			inGame = true;
			FrameworkController.enableGameView(true);
		}
	}
	
	/**
	 * Check for incoming challenges.
	 *
	 * @param line the line
	 */
	public void checkForIncomingChallenges(String line){
		String fullLine = line;
		String temp;
		
		if(line.contains("CHALLENGER")){
			
			temp = fullLine.substring(fullLine.indexOf("GER:")+6);
			String challengingPlayer = temp.substring(0, temp.indexOf("\""));
			
			temp = fullLine.substring(fullLine.indexOf("YPE:")+6);
			String gameType = temp.substring(0,temp.indexOf("\""));
			
			for(int i = 0;i<LobbyView.chooseGame.getItemCount();i++){
				if(LobbyView.chooseGame.getItemAt(i).toString().equalsIgnoreCase(gameType)){
					LobbyView.chooseGame.setSelectedIndex(i);
				}
			}
			
			temp = fullLine.substring(fullLine.indexOf("BER:")+6);
			String challengeNumber = temp.substring(0,temp.indexOf("\""));
			
			int result = JOptionPane.showConfirmDialog(FrameworkView.window,challengingPlayer+" is challenging you for a game of "+gameType+ "\n Do you want to accept the challenge?",null, JOptionPane.YES_NO_OPTION);
		
			if(result == JOptionPane.YES_OPTION) {
				acceptChallenge(challengeNumber);
			}
		}
	}
	
	/**
	 * Check for new players.
	 *
	 * @param line the line
	 */
	public void checkForNewPlayers(String line){
		getPlayerList();
		try
		{
			if(line.contains("PLAYERLIST")){
				if(checkForDifferenceInLobby(line)){
					clearServerLobby();
					addClientToLobby();
					PlayerlistToNames(line);
				}	
				LoginView.ccThread.sleep(1000);
			}
			
			if(line.contains("PLAYERLIST")){
				previousLobby = line;
			}
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(FrameworkView.window,e.toString(),"Error",JOptionPane.ERROR_MESSAGE); e.printStackTrace(); e.printStackTrace();
		}	
	}
	
	/**
	 * Check for difference in lobby.
	 *
	 * @param currentLobby the current lobby
	 * @return true, if successful
	 */
	public boolean checkForDifferenceInLobby(String currentLobby){
		if(currentLobby.equals(previousLobby)){
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * Playerlist to names.
	 *
	 * @param arrayAsString the array as string
	 */
	public void PlayerlistToNames(String arrayAsString){
		String temp = "";
		Socket tempSocket = null;
		
		while(arrayAsString.contains("\""))
		{	
			int beginIndex = arrayAsString.indexOf("\"")+1;
			arrayAsString = arrayAsString.substring(beginIndex);
			
			int secondIndex = arrayAsString.indexOf("\"");
			
			if(arrayAsString.contains("\""))
			temp = arrayAsString.substring(0,secondIndex);  
			
			arrayAsString = arrayAsString.substring(secondIndex+1);
			
			FrameworkMVC.sl.addClientToLobby(tempSocket, temp);
			updatePlayersOnlineLabel();
		}
	}
	
	/**
	 * Adds the client to lobby.
	 */
	public void addClientToLobby(){
		FrameworkMVC.sl.addClientToLobby(socket, userName);
	}
	
	/**
	 * Clear server lobby.
	 */
	public void clearServerLobby(){
		ServerLobby.removeClientsFromLobby();
		ServerLobby.clearUsernames();
		ServerLobby.i = 1;
	}
	
	/**
	 * Login.
	 *
	 * @param userName the user name
	 */
	public void login(String userName){
		out.println("login "+userName);
	}
	
	/**
	 * Subscribe.
	 *
	 * @param game the game
	 */
	public static void subscribe(String game){
		out.println("subscribe "+game);
	}
	
	/**
	 * Logout.
	 */
	public static void logout(){
		out.println("logout");
	}
	
	/**
	 * Gets the game list.
	 *
	 * @return the game list
	 */
	public static void getGameList(){
		out.println("get gamelist");
	}
	
	/**
	 * Gets the player list.
	 *
	 * @return the player list
	 */
	public static void getPlayerList(){
		out.println("get playerlist");
	}
	
	/**
	 * Challenge.
	 *
	 * @param player the player
	 */
	public static void challenge(String player){
		out.println("challenge \""+player+"\" \""+LobbyView.gameChosen+"\"");
	}
	
	/**
	 * Accept challenge.
	 *
	 * @param challengeNumber the challenge number
	 */
	public void acceptChallenge(String challengeNumber){
		out.println("challenge accept "+challengeNumber);
	}
	
	/**
	 * Move.
	 *
	 * @param position the position
	 */
	public static void move(int position){
		out.println("move "+position);
	}
	
	/**
	 * Forfeit.
	 */
	public static void forfeit(){
		out.println("forfeit");
	}
	
	/**
	 * Help.
	 */
	public static void help(){
		out.println("help");
	}
	
	/**
	 * Gets the panel.
	 *
	 * @param gameStatus the game status
	 * @return the panel
	 */
	public static JPanel getPanel(String gameStatus){
		JPanel gifPanel = new JPanel();
		JLabel gif;
		ImageIcon imageIcon = null;
		
		if(gameStatus.equals("DRAW")){
			imageIcon = new ImageIcon(ClientConnection.class.getClassLoader().getResource("server/draw.gif")); 
		}
		if(gameStatus.equals("WIN")){
			imageIcon = new ImageIcon(ClientConnection.class.getClassLoader().getResource("server/win.gif")); 
		}
		if(gameStatus.equals("LOSS")){
			imageIcon = new ImageIcon(ClientConnection.class.getClassLoader().getResource("server/loss.gif")); 
		}
		
		gif = new JLabel(imageIcon);
		gifPanel.add(gif);
		
		return gifPanel;
	}
	
}
