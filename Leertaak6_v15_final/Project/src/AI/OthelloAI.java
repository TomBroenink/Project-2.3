package AI;

import AI.AbstractAI;

public class OthelloAI extends AbstractAI {
	
	private final static int BOARD_WIDTH = 8;
	private final static int BOARD_HEIGHT = 8;
	private final static String GAME_NAME = "Othello";
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
	private static final int[][] heuristics = { 	
        {99, -8, 8, 6, 6, 8, -8, 99}, 			
        {-8, -24, -4, -3, -3, -4, -24, -8}, 			
        {8, -4, 7, 4, 4, 7, -4, 8}, 			
        {6, -3, 4, 0, 0, 4, -3, 6}, 			
        {6, -3, 4, 0, 0, 4, -3, 6}, 			
        {8, -4, 7, 4, 4, 7, -4, 8}, 			
        {-8, -24, -4, -3, -3, -4, -24, -8}, 			
        {99, -8, 8, 6, 6, 8, -8, 99}}; 			
	
	public OthelloAI(){
		super(BOARD_WIDTH, BOARD_HEIGHT, GAME_NAME);
		this.initBoard();
	}	        
	
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
	
	@Override 			
	public int getBestMove(int player) { 			
		return this.getMoveHighestScore(player); 			
	} 			
		        
	public int getSquareValue( int move){ 			
		int x = getMoveX(move); 			
		int y = getMoveY(move); 			
		
		return heuristics[x][y]; 			       			
	} 

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
	 * get the move with the highest score 
	 * @param player
	 * @return
	 
	public Integer getMoveHighestScore(int player){
		int move = 0, score = 0;
		int bestMove = 0;
		int highestTotalValue = 0;
		for(int i = 0; i < getWidth()*getHeight();i++){ // check hele board
			if(this.isValidMove(player,i)) { // check voor correcte move
				int cScore = this.getTotalPoints(player, i, this.gameboard); // pak de gene met meeste punten
				int squareValue = getSquareValue(i);
				
				int temphighestTotalValue  = cScore + squareValue;
			
				if(score < cScore + squareValue) {
					score = cScore;
					move = i;
				};	
				
				if(highestTotalValue < temphighestTotalValue){
					highestTotalValue = temphighestTotalValue;
					bestMove = move;
				}
			};
		}
		return bestMove;
	}
	*/
	
	public static void place(int col, int row, int player){
		gameboard[col][row] = player;
	}
	
	public static void placeOnBoard( int move, int player){
		
		int col = move % BOARD_WIDTH;
		int row = move / BOARD_HEIGHT;
		
		gameboard[row][col] = player;
		
		gameboard = flipAllPieces(player, move);
	}
	
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
	  * flip pieces ai board
	  * @param player
	  * @param startX
	  * @param startY
	  * @param offsetX
	  * @param offsetY
	  * @param gameBoard
	  * @return
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
	  * Check the maximum amount of point the player can collect with this move
	  * @param player
	  * @param x
	  * @param y
	  * @return
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
	  * Check the maximum amount of point the player can collect with this move
	  * @param player
	  * @param x
	  * @param y
	  * @return
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
	  * Check if both or one player still can make a move
	  * @return
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
	* 			
	* @param gameboard 			
	* @return player value 			
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
     			
	public static int getMoveX(int move) { 			
		return move%BOARD_WIDTH; 			
	} 			
	
	public static int getMoveY(int move) { 			
		return move/BOARD_HEIGHT; 			
	} 			
			  			
				
	private class Move implements Comparable<Move> { 			
					
		private int move; 			
		private int value; 			
				
		public Move(int move, int value) { 			
			this.move=move; 			
			this.value=value; 			
		} 			
			
		public int getMove() { 			
			return move; 			
		} 	
			
		@Override 			
		public int compareTo(Move m) { 			
			return m.value-this.value; // omgekeerd omdat we de hoogste waarde aan de head van de queue willen 			
		} 								
	} 
}
