package AI;

public abstract class AbstractAI {

	// het board
	public static int[][] gameboard;
	
	// init vars
	private String gameName;
	private int width;
	private int height;
	
	// Mogelijkheden Vakje
	protected final static int EMPTY = 0;
	protected static final int P1 = 1;
	protected static final int P2 = 2;
	
	// start situatie
	protected int currentPlayer = P1;
	
	// status mogelijkheden
	protected final int UNKNOWN = 0;
	protected final int WIN = 1;
	protected final int LOSE = 2;
	protected final int DRAW = 3;
	
	public AbstractAI(int width, int height, String gameName){
		this.width = width;
		this.height = height;
		this.gameName = gameName;
		gameboard = new int[width][height];
	}
	
	public void setCurrentPlayer(int player){
		this.currentPlayer = player;
	}
	
	public void switchPlayer(){
		this.currentPlayer = this.getOpponent();
	}
	
	public void setBoard(int width, int height){
		this.width = width;
		this.height = height;
		gameboard = new int[width][height];
	}
	
	public abstract void initBoard();
	
	public void clearBoard(){
		gameboard = new int[width][height];
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public int getHeight(){
		return this.height;
	}
	
	public String getGameName(){
		return this.gameName;
	}
	
	public int[][] getBoard() {
		return this.gameboard;
	}
	
	public int getCurrentPlayer(){
		return this.currentPlayer;
	}
	
	public abstract boolean isValidMove(int player, int move);

	protected abstract int getStatus(int player);

	public int getOpponent() {
		
		return getCurrentPlayer() == 1 ? 2 : 1;
	}
	
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

	public abstract int getBestMove(int player);
}
