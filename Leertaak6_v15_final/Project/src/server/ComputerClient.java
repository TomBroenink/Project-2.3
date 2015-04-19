package server;

import framework.FrameworkController;
import framework.LobbyView;
import game.GameView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class ComputerClient implements Runnable {
	Socket computerSocket;
	BufferedReader in;
	PrintWriter out;
	String userName = "PC_1v1_Client";
	String line;
	boolean mayMove;
	
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
	
	private void moveIfAllowed(String line){
		if(line.contains("YOURTURN") || line.contains("PLAYERTOMOVE: \""+userName)){
			mayMove = true;
			makeAIMove();
			skipLine();
		}
	}
	
	private void skipLine(){
		try{
			in.readLine();
		}
		catch(Exception e){}
	}
	
	private void makeAIMove(){
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
			GameView.updateGameBoard(position, "O");
			mayMove = false;
		}
	}
	
	private boolean isEmpty(int position){
		return GameView.boardPosition.get(position).getText().equals(" ");
	}
	
	private void move(int position){
		out.println("move "+position);
	}
	
	private void closeConnection(){
		out.println("logout");
		
	}
	
	private void subscribe(String game){
		out.println("subscribe "+game);
	}
	
	public void login(String userName){
		out.println("login "+userName);
	}
}
