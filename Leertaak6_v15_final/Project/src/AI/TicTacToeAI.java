package AI;

import java.util.Random;

public class TicTacToeAI extends AbstractAI {
	
	private final static int BOARD_WIDTH = 3;
	private final static int BOARD_HEIGHT = 3;
	private final static String GAME_NAME = "Tic-Tac-Toe";
	private Random random=new Random();  
	private int side=random.nextInt(2); 
	
	public TicTacToeAI(){
		super(BOARD_WIDTH, BOARD_HEIGHT, GAME_NAME);
	}

	public void initBoard(){
		new TicTacToeAI();
	}
	 /**
     * krijg de beste move door minimax methode
     * deze move moet geparsed worden naar 1 integer aangezien
     * deze move naar de server gestuurd moet worden
     * @param player
     * @return Integer move
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
	 * Bereken beste move
	 * @param player
	 * @return BestMove
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
	 * maak de move
	 * @param player
	 * @param move
	 */
	public void doMove(int player, int move){
			
		int row = move / gameboard.length;
		int col = move / gameboard.length;
		
		if(isValidMove(player, move)){
			gameboard[row][col] = player;
		}
	}
	
	/**
	 *  kijk of de move juist is
	 *  @return true als juist
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
	 * @param row
	 * @param col
	 * @param player
	 */
	public static void placeOnBoard(int row, int col, int player){
		gameboard[row][col] = player;
	}
	
	
	
	/**
	 * get gameStatus van de huidige match
	 * @return int met status van huidge speler
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
	 *  check of het bord vol is
	 *  @return true als het bord vol is
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
	
	private boolean squareIsEmpty( int row, int column )
	{
		System.out.println(gameboard[ row ][ column ] == EMPTY);
		return gameboard[ row ][ column ] == EMPTY;
	}
	
	
	/**
	 * check of er gewonnen is
	 * @param player
	 * @return
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
