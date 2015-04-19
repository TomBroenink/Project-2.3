package AI;

import AI.AbstractAI;

/**
 * The Class OthelloAI.
 */
public class OthelloAI extends AbstractAI {
	
	/** The Constant BOARD_WIDTH. */
	private final static int BOARD_WIDTH = 8;
	
	/** The Constant BOARD_HEIGHT. */
	private final static int BOARD_HEIGHT = 8;
	
	/** The Constant GAME_NAME. */
	private final static String GAME_NAME = "Othello";
	
	/** The Constant DIRECTIONS. */
	private static final int[][] DIRECTIONS = new int[][]{
		{-1,-1}, // N-W
		{0, -1}, // N
		{1, -1}, // N-E
		{1,0}, // E
		{1,1}, // S-E
		{0,1}, // S
		{-1,1}, // S-W
		{-1,0} // W
	};
	
	/** The Constant heuristics. */
	private static final int[][] heuristics = { 	
        {99, -8, 8, 6, 6, 8, -8, 99}, 			
        {-8, -24, -4, -3, -3, -4, -24, -8}, 			
        {8, -4, 7, 4, 4, 7, -4, 8}, 			
        {6, -3, 4, 0, 0, 4, -3, 6}, 			
        {6, -3, 4, 0, 0, 4, -3, 6}, 			
        {8, -4, 7, 4, 4, 7, -4, 8}, 			
        {-8, -24, -4, -3, -3, -4, -24, -8}, 			
        {99, -8, 8, 6, 6, 8, -8, 99}}; 			
	
	/**
	 * Instantiates a new othello ai.
	 */
	public OthelloAI(){
		super(BOARD_WIDTH, BOARD_HEIGHT, GAME_NAME);
		this.initBoard();
	}	        
	
	/* (non-Javadoc)
	 * @see AI.AbstractAI#initBoard()
	 */
	@Override
	public void initBoard(){
		// start positie
		//reset eerst
		gameboard = new int[8][8];
		
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				gameboard[i][j] = 0;
			}
		}
		
		gameboard[4][3] = 1;
		gameboard[3][4] = 1;
		gameboard[3][3] = 2;
		gameboard[4][4] = 2;	
	};
	
	/* (non-Javadoc)
	 * @see AI.AbstractAI#getBestMove(int)
	 */
	@Override 			
	public int getBestMove(int player) { 			
		return this.getMoveHighestScore(player); 			
	} 			
		        
	/**
	 * Gets the square value.
	 *
	 * @param move the move
	 * @return the square value
	 */
	public int getSquareValue( int move){ 			
		int x = getMoveX(move); 			
		int y = getMoveY(move); 			
		
		return heuristics[x][y]; 			       			
	} 

	/**
	 * Gets the move highest score.
	 *
	 * @param player the player
	 * @return the move highest score
	 */
	public Integer getMoveHighestScore(int player){
        int move = 0, score = 0;
        for(int i = 0; i < getWidth()*getHeight();i++){ // check hele board
            if(this.isValidMove(player,i)) { // check voor correcte move
                int cScore = this.getTotalPoints(player, i, this.gameboard); // pak de gene met meeste punten                
                if(score < cScore ) {
                    score = cScore;
                    move = i;
                };
            };
        }
        return move;
    } 

	/**
	 * Place.
	 *
	 * @param col the col
	 * @param row the row
	 * @param player the player
	 */
	
	public static void place(int col, int row, int player){
		gameboard[col][row] = player;
	}
	
	/**
	 * Place on board.
	 *
	 * @param move the move
	 * @param player the player
	 */
	public static void placeOnBoard( int move, int player){
		
		int col = move % BOARD_WIDTH;
		int row = move / BOARD_HEIGHT;
		
		gameboard[row][col] = player;
		
		gameboard = flipAllPieces(player, move);
	}
	
	/* (non-Javadoc)
	 * @see AI.AbstractAI#isValidMove(int, int)
	 */
	@Override
	 public boolean isValidMove(int player, int move) {
         
         int row = move / BOARD_HEIGHT;
         int col = move % BOARD_WIDTH;
         
         if(player == this.getCurrentPlayer()){
                 if((row >= 0 && row < BOARD_HEIGHT) && (col >=0 && col < BOARD_WIDTH)){
                         if(gameboard[row][col] == EMPTY){
                                 int points = getTotalPoints(player, move, gameboard);
                                 if(points > 0) {
                                         return true;
                                 }
                         }
                 }
         }
         return false;
	}
 
	 /**
 	 * Checks if is valid move.
 	 *
 	 * @param player the player
 	 * @param move the move
 	 * @param gameBoard the game board
 	 * @return true, if is valid move
 	 */
 	public boolean isValidMove(int player, int move, int[][]gameBoard){
	         
	         int col = move % BOARD_WIDTH;
	         int row = move / BOARD_HEIGHT;
	         
	         if(player == this.getCurrentPlayer()){
	                 if((row >= 0 && row < BOARD_HEIGHT) && (col >=0 && col < BOARD_WIDTH)){
	                         if(gameBoard[row][col] == EMPTY){
	                                 if(getTotalPoints(player, move, gameBoard) > 0){
	                                         return true;
	                                 }
	                         }
	                 }
	         }
	         return false;
	 }
	 
	 /**
 	 * Gets the points in direction.
 	 *
 	 * @param player the player
 	 * @param startX the start x
 	 * @param startY the start y
 	 * @param offsetX the offset x
 	 * @param offsetY the offset y
 	 * @param gameBoard the game board
 	 * @return the points in direction
 	 */
 	private static int getPointsInDirection(int player, int startX, int startY, int offsetX, int offsetY,int[][] gameBoard){
	     
	     int x = startX+offsetX;
	     int y = startY+offsetY;
	     
	     int points = 0;
	     while(x < 8 && x >= 0 && y < 8 && y >= 0){
	             if(gameBoard[y][x] == player){
	                     return points;
	             }else if(gameBoard[y][x] == 0){
	                     return 0;
	             }else{
	                     points++;
	                     x+=offsetX;
	                     y+=offsetY;
	             }
	     }
	     return 0;
	}
	
	 /**
 	 * Flip game board pieces.
 	 *
 	 * @param player the player
 	 * @param startX the start x
 	 * @param startY the start y
 	 * @param offsetX the offset x
 	 * @param offsetY the offset y
 	 * @param gameBoard the game board
 	 * @return the int[][]
 	 */
	 private static int[][] flipGameBoardPieces(int player, int startX, int startY, int offsetX, int offsetY,int[][] gameBoard){
	 	 	int opp;
	 	 	
	 	     if(player == 1){
		 		 opp = 2;
		 	 }
		 	 else{
		 		 opp = 1;
		 	 }
		 
	         int x = startX+offsetX;
	         int y = startY+offsetY;
	         
	         int[][] game = gameBoard;
	         
	         if(player == EMPTY){
	                 player = P1;
	         }
	         
	         while(x < 8 && x >= 0 && y < 8 && y >= 0 && game[y][x] != player){
	                 if(game[y][x] == opp){
	                         game[y][x] = player;
	                 }
	                 x+=offsetX;
	                 y+=offsetY;
	         }
	         return game;
	 }
	 
	 /**
 	 * Gets the total points.
 	 *
 	 * @param player the player
 	 * @param move the move
 	 * @param gameBoard the game board
 	 * @return the total points
 	 */
	 private int getTotalPoints(int player,int move, int[][]gameBoard){
	         
	         int col = move % BOARD_WIDTH;
	         int row = move / BOARD_HEIGHT;
	         
	         int points = 0;
	         
	         for(int[] direction : OthelloAI.DIRECTIONS) {
	                 points += getPointsInDirection(player,col,row,direction[0],direction[1], gameBoard);
	         };
	         
	         return points;
	 }
	 
	 /**
 	 * Flip all pieces.
 	 *
 	 * @param player the player
 	 * @param move the move
 	 * @return the int[][]
 	 */
	 private static int[][] flipAllPieces(int player,int move){
		 int col = move % BOARD_WIDTH;
	     int row = move / BOARD_HEIGHT;
	        
	     int[][] board = gameboard;
	         
	     for(int[] direction : OthelloAI.DIRECTIONS) {
	             if(getPointsInDirection(player,col,row,direction[0],direction[1],gameboard) > 0) {
	                     board = flipGameBoardPieces(player,col,row,direction[0],direction[1], gameboard);
	             };
	     };
	         
	     return board;
	 }
	 
	 /**
 	 * Gets the current score.
 	 *
 	 * @param player the player
 	 * @return the current score
 	 */
 	private int getCurrentScore(int player){
	         int score = 0;
	         for(int i = 0; i > getHeight()*getWidth(); i++){
	                 if(gameboard[i%getWidth()][i/getWidth()] == player){
	                         score++;
	                 }
	         }
	         return score;
	 }
	 
	 /**
 	 * Game over.
 	 *
 	 * @return true, if successful
 	 */
	 private boolean gameOver(){
	         for(int i = 0; i > getHeight()*getWidth(); i++){
	                 if(gameboard[i%getWidth()][i/getWidth()] == EMPTY){
	                         if(isValidMove(P1, i) || isValidMove(P2, i)){
	                                 return false;
	                         }
	                 }
	         }
	         return true;
	 }
	
	 /* (non-Javadoc)
 	 * @see AI.AbstractAI#getStatus(int)
 	 */
 	@Override
	 protected int getStatus(int player) {
	         if(gameOver()){
	                 int scoreP1 = getCurrentScore(P1);
	                 int scoreP2 = getCurrentScore(P2);
	                 if(scoreP1 == scoreP2){
	                         return DRAW;
	                 }else if(scoreP1 > scoreP2){
	                         return player == P1 ? WIN : LOSE;
	                 }else{
	                         return player == P2 ? WIN : LOSE;
	                 }
	        }
	        return UNKNOWN;
	}
	 
	/**
	 * Gets the board value.
	 *
	 * @param gameboard the gameboard
	 * @return the board value
	 */ 			
	public int getBoardValue(int[][] gameboard){ 			
		int playerV = 0; 			
		int opponentV = 0; 			
		            
		for(int i=0; i<8; i++){ 	
			for(int j=0; j<8; j++){ 			
				if(gameboard[i][j] == P1) 			
					playerV += heuristics[i][j]; 			
				else if(gameboard[i][j] == P2) 			
					opponentV += heuristics[i][j]; 			
			} 			
		} 			
		return playerV-opponentV; 			
	} 			
     			
	/**
	 * Gets the move x.
	 *
	 * @param move the move
	 * @return the move x
	 */
	public static int getMoveX(int move) { 			
		return move%BOARD_WIDTH; 			
	} 			
	
	/**
	 * Gets the move y.
	 *
	 * @param move the move
	 * @return the move y
	 */
	public static int getMoveY(int move) { 			
		return move/BOARD_HEIGHT; 			
	} 			
			  			
				
	/**
	 * The Class Move.
	 */
	private class Move implements Comparable<Move> { 			
					
		/** The move. */
		private int move; 			
		
		/** The value. */
		private int value; 			
				
		/**
		 * Instantiates a new move.
		 *
		 * @param move the move
		 * @param value the value
		 */
		public Move(int move, int value) { 			
			this.move=move; 			
			this.value=value; 			
		} 			
			
		/**
		 * Gets the move.
		 *
		 * @return the move
		 */
		public int getMove() { 			
			return move; 			
		} 	
			
		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override 			
		public int compareTo(Move m) { 			
			return m.value-this.value; // omgekeerd omdat we de hoogste waarde aan de head van de queue willen 			
		} 								
	} 
}
