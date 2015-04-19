package framework;

import server.ClientConnection;

public class GameProtocol {

	public GameProtocol(){
		
	}
	
	public static void challenge(String player){
		ClientConnection.challenge(player);
	}
	
	public static void move(int position){
		ClientConnection.move(position);
	}
	
	public static void forfeit(){
		ClientConnection.forfeit();
	}
}
