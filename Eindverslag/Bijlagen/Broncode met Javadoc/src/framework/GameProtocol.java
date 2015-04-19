package framework;

import server.ClientConnection;

/**
 * The Class GameProtocol.
 */
public class GameProtocol {

	/**
	 * Instantiates a new game protocol.
	 */
	public GameProtocol(){
		
	}
	
	/**
	 * Challenge.
	 *
	 * @param player the player
	 */
	public static void challenge(String player){
		ClientConnection.challenge(player);
	}
	
	/**
	 * Move.
	 *
	 * @param position the position
	 */
	public static void move(int position){
		ClientConnection.move(position);
	}
	
	/**
	 * Forfeit.
	 */
	public static void forfeit(){
		ClientConnection.forfeit();
	}
}
