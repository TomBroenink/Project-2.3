package AI;

public class BestMove {
int row;
int column;
int val;

public BestMove( int v )
{ 
    this( v, 0, 0 ); 
}

public BestMove( int v, int r, int c )
 { 
    val = v; 
    row = r; 
    column = c; 
}

public int getRow(){
    return row;
}

public int getCol(){
    return column;
}
}
