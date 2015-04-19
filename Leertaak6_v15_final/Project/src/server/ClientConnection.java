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

public class ClientConnection implements Runnable
{
	public static String userName;
	public static boolean inLobby = false;
	public static boolean inGame = false;
	public static boolean mayMove = false;
	public static Socket socket;
	public static String lastError;
	public static BufferedReader in = null;
	public static PrintWriter out = null;
	public static Thread turnTimer;
	public static ArrayList<String> gameList = new ArrayList<String>();
	public static String ipAddress;
	public static String playerOne = "";
	public static String playerChar;
	String previousLobby;
	boolean runOnce;
	boolean gameListEmpty = true;
	public static int portNumber;
	
	public ClientConnection(String ipAddress, int portNumber, String userName)
	{
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;
		this.userName = userName;
	}
	
	
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
	
	public static void makeAIMove(){
		int position = 23;
		if(LobbyView.chooseGame.getSelectedItem().toString().equalsIgnoreCase("Reversi")){
			position = FrameworkController.AIModule.getBestMove(FrameworkController.AIModule.getCurrentPlayer());
		}
		if(LobbyView.chooseGame.getSelectedItem().toString().equals("Tic-tac-toe")){
			position = 4;
			Random random=new Random();  
			while(!isEmpty(position)){
				position =random.nextInt(9);
			}
		}
		
		if(mayMove){
			move(position);
			GameView.updateGameBoard(position, playerChar);
			mayMove = false;
		}
		GameView.boardPanel.repaint();
	}
	
	private static boolean isEmpty(int position){
		return GameView.boardPosition.get(position).getText().equals(" ");
	}
	
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
	
	public void updateGameList(String line){
		if(line.contains("GAMELIST")){
			checkForGames(line);
			LobbyView.updateGameComboBox(gameList);
			if(LobbyView.chooseGame.getItemAt(0) != null){
				LobbyView.chooseGame.setSelectedIndex(0);
			}
		}
	}
	
	public void updatePlayersOnlineLabel(){
		int i = ServerLobby.userNames.size();
		LobbyView.updatePlayersInLobby(i);
	}

	//checks for games and adds them to e dropdown menu
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
	
	public void backToLobby(){
		FrameworkController.enableGameView(false);
		GameView.clearBoard();
		inGame = false;
		inLobby = true;
		out.println("get playerlist");
	}
	
	public boolean isItYourTurn(String line){
		if(line.contains("YOURTURN") || line.contains("PLAYERTOMOVE: \""+userName)){
			return true;
		}
		else{
			return false;
		}
	}
	
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
	
	public boolean checkForDifferenceInLobby(String currentLobby){
		if(currentLobby.equals(previousLobby)){
			return false;
		}
		else{
			return true;
		}
	}
	
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
	
	public void addClientToLobby(){
		FrameworkMVC.sl.addClientToLobby(socket, userName);
	}
	
	public void clearServerLobby(){
		ServerLobby.removeClientsFromLobby();
		ServerLobby.clearUsernames();
		ServerLobby.i = 1;
	}
	
	public void login(String userName){
		out.println("login "+userName);
	}
	
	public static void subscribe(String game){
		out.println("subscribe "+game);
	}
	
	public static void logout(){
		out.println("logout");
	}
	
	public static void getGameList(){
		out.println("get gamelist");
	}
	
	public static void getPlayerList(){
		out.println("get playerlist");
	}
	
	public static void challenge(String player){
		out.println("challenge \""+player+"\" \""+LobbyView.gameChosen+"\"");
	}
	
	public void acceptChallenge(String challengeNumber){
		out.println("challenge accept "+challengeNumber);
	}
	
	public static void move(int position){
		out.println("move "+position);
	}
	
	public static void forfeit(){
		out.println("forfeit");
	}
	
	public static void help(){
		out.println("help");
	}
	
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
