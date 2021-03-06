package AI;

import java.util.Random;

/**
 * The Class TicTacToeAI.
 */
public class TicTacToeAI extends AbstractAI {
	
	/** The Constant BOARD_WIDTH. */
	private final static int BOARD_WIDTH = 3;
	
	/** The Constant BOARD_HEIGHT. */
	private final static int BOARD_HEIGHT = 3;
	
	/** The Constant GAME_NAME. */
	private final static String GAME_NAME = "Tic-Tac-Toe";
	
	/** The random. */
	private Random random=new Random();  
	
	/** The side. */
	private int side=random.nextInt(2); 
	
	/**
	 * Instantiates a new tic tac toe ai.
	 */
	public TicTacToeAI(){
		super(BOARD_WIDTH, BOARD_HEIGHT, GAME_NAME);
	}

	/* (non-Javadoc)
	 * @see AI.AbstractAI#initBoard()
	 */
	public void initBoard(){
		new TicTacToeAI();
	}
	 
 	/* (non-Javadoc)
 	 * @see AI.AbstractAI#getBestMove(int)
 	 */
	public int getBestMove(int player){
			BestMove BestMove = chooseMove(player, 1,2);
			int move = 0;
			
			if(BestMove != null){
				move = (BestMove.getRow() * gameboard.length) + BestMove.getCol();
			}
			
			return move;
	}
	
	/**
	 * Choose move.
	 *
	 * @param side the side
	 * @param hw the hw
	 * @param cw the cw
	 * @return the best move
	 */
	//Find optimal move
	   private BestMove chooseMove(int side, int hw, int cw) {
	       int opp;
	       BestMove reactionMove = new BestMove(1,1,1);
	       int simpleEval;
	       int bestRow = 0;
	       int bestColumn = 0;
	       int value;       
	       
	       if ((simpleEval = getStatus(P2)) != UNKNOWN)
	           return new BestMove(simpleEval);        
	       if (side == P1) {
	           opp = P2;
	           value = hw;
	       } else {
	           opp = P1;
	           value = cw;
	       }        // breakable loop die doorlopen wordt totdat aan bepaalde criteria voldaan wordt
	       RecursiveLoop:
	       //dubbele for-loop om hele bord door te lopen
	       for (int row = 0; row < 3; row++)
	           for (int column = 0; column < 3; column++)
	               //alleen lege plekken meetellen
	               if (squareIsEmpty(row, column)) {
	                   //plaats op lege plek een waarde van speler die aan de beurt is
	            	   placeOnBoard(row, column, side);
	                   //roep recursief de methode aan met nieuwe tegenstander en nieuw bord ook.                    reactionMove = chooseMove(opp, hw, cw);
	                   // na de recursie zorg dat de oude staat van het bord weer wordt hersteld
	                   placeOnBoard(row, column, EMPTY);                    //" -- positive values are good for the maximizing player"
	                   //" -- negative values are good for the minimizing player"
	                   //  P2 is aan zet en de waarde van de zet is de hoogste
	                   // waarde totnutoe. Wijzigen dus.
	                   if (opp == P1 && reactionMove.val > value) {
	                       hw = value = reactionMove.val;
	                       bestRow = row;
	                       bestColumn = column;
	                       // break loop wanneer de beste zet bereikt is
	                       if (hw >= cw)
	                           break RecursiveLoop;
	                   }
	                   // P1 is aan zet. Als value van zet kleiner is dan value is het dus voor
	                   // de speler een slechte zet. Goede zet dus voor de P2.
	                   if (opp == P2 && reactionMove.val < value) {
	                       cw = value = reactionMove.val;
	                       // set ze voor beste zet
	                       bestRow = row;
	                       bestColumn = column;
	                       // break loop wanneer de beste zet bereikt is
	                       if (hw >= cw)
	                           break RecursiveLoop;
	                   }
	               }
	       //return beste zet
	       return new BestMove(value, bestRow, bestColumn);
	   } 
	
	/**
	 * Do move.
	 *
	 * @param player the player
	 * @param move the move
	 */
	public void doMove(int player, int move){
			
		int row = move / gameboard.length;
		int col = move / gameboard.length;
		
		if(isValidMove(player, move)){
			gameboard[row][col] = player;
		}
	}
	
	/* (non-Javadoc)
	 * @see AI.AbstractAI#isValidMove(int, int)
	 */
	@Override
	public boolean isValidMove(int player, int move){
		if(player == super.getCurrentPlayer()) {
            if(move >= 0 && move <= 8 && gameboard[move / gameboard.length][move % gameboard.length] == EMPTY){
                    return true;
            }
    }
    return false;
	}
	
	/**
	 * Place on board.
	 *
	 * @param row the row
	 * @param col the col
	 * @param player the player
	 */
	public static void placeOnBoard(int row, int col, int player){
		gameboard[row][col] = player;
	}
	
	
	
	/* (non-Javadoc)
	 * @see AI.AbstractAI#getStatus(int)
	 */
	@Override
	protected int getStatus(int player){
		if(boardIsFull()){
			return DRAW;
		}else if(isAWin(player)){
			return WIN;
		}else if(isAWin(super.getOpponent())){
			return LOSE;
		}else{
			return UNKNOWN;
		}
	}
	
	/**
	 * Board is full.
	 *
	 * @return true, if successful
	 */
	private boolean boardIsFull(){
		// ga het hele bord bij langs en kijk of er vakjes EMPTY zijn
		for (int row = 0; row < gameboard.length; row++){
			for(int col = 0; col < gameboard.length; col++){
				if(gameboard[row][col] == EMPTY){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Square is empty.
	 *
	 * @param row the row
	 * @param column the column
	 * @return true, if successful
	 */
	private boolean squareIsEmpty( int row, int column )
	{
		System.out.println(gameboard[ row ][ column ] == EMPTY);
		return gameboard[ row ][ column ] == EMPTY;
	}
	
	
	/**
	 * Checks if is a win.
	 *
	 * @param player the player
	 * @return true, if is a win
	 */
	public boolean isAWin(int player)
	  {
	    if ((this.gameboard[0][0] == player) && (this.gameboard[0][1] == player) && (this.gameboard[0][2] == player)) {
	      return true;
	    }
	    if ((this.gameboard[1][0] == player) && (this.gameboard[1][1] == player) && (this.gameboard[1][2] == player)) {
	      return true;
	    }
	    if ((this.gameboard[2][0] == player) && (this.gameboard[2][1] == player) && (this.gameboard[2][2] == player)) {
	      return true;
	    }
	    if ((this.gameboard[0][0] == player) && (this.gameboard[1][0] == player) && (this.gameboard[2][0] == player)) {
	      return true;
	    }
	    if ((this.gameboard[0][1] == player) && (this.gameboard[1][1] == player) && (this.gameboard[2][1] == player)) {
	      return true;
	    }
	    if ((this.gameboard[0][2] == player) && (this.gameboard[1][2] == player) && (this.gameboard[2][2] == player)) {
	      return true;
	    }
	    if ((this.gameboard[0][0] == player) && (this.gameboard[1][1] == player) && (this.gameboard[2][2] == player)) {
	      return true;
	    }
	    if ((this.gameboard[2][0] == player) && (this.gameboard[1][1] == player) && (this.gameboard[0][2] == player)) {
	      return true;
	    }
	    return false;
	  }
}
