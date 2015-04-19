package AI;

/**
 * The Class AbstractAI.
 */
public abstract class AbstractAI {

	// het board
	/** The gameboard. */
	public static int[][] gameboard;
	
	// init vars
	/** The game name. */
	private String gameName;
	
	/** The width. */
	private int width;
	
	/** The height. */
	private int height;
	
	// Mogelijkheden Vakje
	/** The Constant EMPTY. */
	protected final static int EMPTY = 0;
	
	/** The Constant P1. */
	protected static final int P1 = 1;
	
	/** The Constant P2. */
	protected static final int P2 = 2;
	
	// start situatie
	/** The current player. */
	protected int currentPlayer = P1;
	
	// status mogelijkheden
	/** The unknown. */
	protected final int UNKNOWN = 0;
	
	/** The win. */
	protected final int WIN = 1;
	
	/** The lose. */
	protected final int LOSE = 2;
	
	/** The draw. */
	protected final int DRAW = 3;
	
	/**
	 * Instantiates a new abstract ai.
	 *
	 * @param width the width
	 * @param height the height
	 * @param gameName the game name
	 */
	public AbstractAI(int width, int height, String gameName){
		this.width = width;
		this.height = height;
		this.gameName = gameName;
		gameboard = new int[width][height];
	}
	
	/**
	 * Sets the current player.
	 *
	 * @param player the new current player
	 */
	public void setCurrentPlayer(int player){
		this.currentPlayer = player;
	}
	
	/**
	 * Switch player.
	 */
	public void switchPlayer(){
		this.currentPlayer = this.getOpponent();
	}
	
	/**
	 * Sets the board.
	 *
	 * @param width the width
	 * @param height the height
	 */
	public void setBoard(int width, int height){
		this.width = width;
		this.height = height;
		gameboard = new int[width][height];
	}
	
	/**
	 * Inits the board.
	 */
	public abstract void initBoard();
	
	/**
	 * Clear board.
	 */
	public void clearBoard(){
		gameboard = new int[width][height];
	}
	
	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth(){
		return this.width;
	}
	
	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight(){
		return this.height;
	}
	
	/**
	 * Gets the game name.
	 *
	 * @return the game name
	 */
	public String getGameName(){
		return this.gameName;
	}
	
	/**
	 * Gets the board.
	 *
	 * @return the board
	 */
	public int[][] getBoard() {
		return this.gameboard;
	}
	
	/**
	 * Gets the current player.
	 *
	 * @return the current player
	 */
	public int getCurrentPlayer(){
		return this.currentPlayer;
	}
	
	/**
	 * Checks if is valid move.
	 *
	 * @param player the player
	 * @param move the move
	 * @return true, if is valid move
	 */
	public abstract boolean isValidMove(int player, int move);

	/**
	 * Gets the status.
	 *
	 * @param player the player
	 * @return the status
	 */
	protected abstract int getStatus(int player);

	/**
	 * Gets the opponent.
	 *
	 * @return the opponent
	 */
	public int getOpponent() {
		
		return getCurrentPlayer() == 1 ? 2 : 1;
	}
	
	/**
	 * Prints the board.
	 *
	 * @param width the width
	 * @param height the height
	 */
	public static void printBoard(int width, int height){
		for(int i = 0;i<height;i++){
			System.out.println();
			for(int j = 0;j<width;j++){
				System.out.print(gameboard[i][j]);
				System.out.print("|");
			}
		}
		System.out.println();
	}

	/**
	 * Gets the best move.
	 *
	 * @param player the player
	 * @return the best move
	 */
	public abstract int getBestMove(int player);
}
