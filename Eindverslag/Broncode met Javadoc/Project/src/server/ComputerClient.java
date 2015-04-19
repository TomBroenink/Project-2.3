package server;

import framework.FrameworkController;
import framework.LobbyView;
import game.GameView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

/**
 * The Class ComputerClient.
 */
public class ComputerClient implements Runnable {
	
	/** The computer socket. */
	Socket computerSocket;
	
	/** The in. */
	BufferedReader in;
	
	/** The out. */
	PrintWriter out;
	
	/** The user name. */
	String userName = "PC_1v1_Client";
	
	/** The line. */
	String line;
	
	/** The may move. */
	boolean mayMove;
	
	/**
	 * Instantiates a new computer client.
	 *
	 * @param ipAddress the ip address
	 * @param portNumber the port number
	 */
	public ComputerClient(String ipAddress, int portNumber){
		try {
			computerSocket = new Socket(ipAddress, portNumber);
									
			in = new BufferedReader(new InputStreamReader(computerSocket.getInputStream())); 
		  	out = new PrintWriter(computerSocket.getOutputStream(), true);	
		  			  	
		  	login(userName);
		  	subscribe(LobbyView.gameChosen);
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 		
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try{
			while(true){
				line = in.readLine();
				if(!line.equalsIgnoreCase("OK")){
					checkGameStatus(line);
					moveIfAllowed(line);
				}
			}
		}
		catch(Exception e){
			
		}
	}
	
	/**
	 * Check game status.
	 *
	 * @param line the line
	 */
	private void checkGameStatus(String line){
		if(line.contains("LOSS")){
			closeConnection();
		}
		else if(line.contains("WIN")){
			closeConnection();
		}
		else if(line.contains("DRAW")){
			closeConnection();
		}	
	}
	
	/**
	 * Move if allowed.
	 *
	 * @param line the line
	 */
	private void moveIfAllowed(String line){
		if(line.contains("YOURTURN") || line.contains("PLAYERTOMOVE: \""+userName)){
			mayMove = true;
			makeAIMove();
			skipLine();
		}
	}
	
	/**
	 * Skip line.
	 */
	private void skipLine(){
		try{
			in.readLine();
		}
		catch(Exception e){}
	}
	
	/**
	 * Make ai move.
	 */
	private void makeAIMove(){
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
			GameView.updateGameBoard(position, "O");
			mayMove = false;
		}
	}
	
	/**
	 * Checks if is empty.
	 *
	 * @param position the position
	 * @return true, if is empty
	 */
	private boolean isEmpty(int position){
		return GameView.boardPosition.get(position).getText().equals(" ");
	}
	
	/**
	 * Move.
	 *
	 * @param position the position
	 */
	private void move(int position){
		out.println("move "+position);
	}
	
	/**
	 * Close connection.
	 */
	private void closeConnection(){
		out.println("logout");
		
	}
	
	/**
	 * Subscribe.
	 *
	 * @param game the game
	 */
	private void subscribe(String game){
		out.println("subscribe "+game);
	}
	
	/**
	 * Login.
	 *
	 * @param userName the user name
	 */
	public void login(String userName){
		out.println("login "+userName);
	}
}
