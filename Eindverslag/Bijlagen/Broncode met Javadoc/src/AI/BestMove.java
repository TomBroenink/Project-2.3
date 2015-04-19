package AI;

/**
 * The Class BestMove.
 */
public class BestMove {

/** The row. */
int row;

/** The column. */
int column;

/** The val. */
int val;

/**
 * Instantiates a new best move.
 *
 * @param v the v
 */
public BestMove( int v )
{ 
    this( v, 0, 0 ); 
}

/**
 * Instantiates a new best move.
 *
 * @param v the v
 * @param r the r
 * @param c the c
 */
public BestMove( int v, int r, int c )
 { 
    val = v; 
    row = r; 
    column = c; 
}

/**
 * Gets the row.
 *
 * @return the row
 */
public int getRow(){
    return row;
}

/**
 * Gets the col.
 *
 * @return the col
 */
public int getCol(){
    return column;
}
}
