import java.util.*;

public class SolutionFinder{
    //Consider using a table of Location
    private Location[][] locationTable;
    private char mySymbol;
    private char theirSymbol;
    private static final int BOARD_SIZE = 8;
    private static final int SEARCH_DEPTH = 5;

    public SolutionFinder( char mySymbol, char theirSymbol ){
        this.mySymbol = mySymbol;
        this.theirSymbol = theirSymbol;
        locationTable = new Location[BOARD_SIZE][BOARD_SIZE];
        for( int i = 0; i < BOARD_SIZE; i++ ){
            for( int j = 0; j < BOARD_SIZE; j++ ){
                locationTable[i][j] = new Location( i, j );
            }
        }
    }

    public Location AlphaBetaSearch( char[][] state, Location lastMove ){
        return InitialMaxxy( state, Integer.MIN_VALUE, Integer.MAX_VALUE, SEARCH_DEPTH, lastMove );
    }

    public Location InitialMaxxy( char[][] state, int alpha, int beta, int remainingDepth, Location lastMove ){
        //Do a terminal check (whether kill move or won state)
        int v = Integer.MIN_VALUE;
        ArrayList<Location> successors = GetSuccessors( state, lastMove );
        Location prevLoc = successors.get( 0 );
        Location returnLoc = successors.get( 0 );
        for( Location successor: successors ){
            //Apply symbol to location change
            state[prevLoc.row][prevLoc.col] = '-';
            state[successor.row][successor.col] = mySymbol;
            prevLoc.row = successor.row;
            prevLoc.col = successor.col;
            int minReturn =  Minny( state, alpha, beta, remainingDepth - 1, successor );
            if( minReturn > v ){
                returnLoc = successor;
                v = minReturn;
            }
            alpha = Math.max( alpha, v );
        }
        return returnLoc;
    }

    public int Maxxy( char[][] state, int alpha, int beta, int remainingDepth, Location lastMove ){
        //Do a terminal check (whether kill move or won state)
        int terminalState = Utility.UtilityFunction( state, mySymbol );
        if( terminalState == 10 || remainingDepth == 0 ){
            return terminalState;
        }
        int v = Integer.MIN_VALUE;
        ArrayList<Location> successors = GetSuccessors( state, lastMove );
        Location prevLoc = successors.get( 0 );
        for( Location successor: successors ){
            //Apply symbol to location change
            state[prevLoc.row][prevLoc.col] = '-';
            state[successor.row][successor.col] = mySymbol;
            prevLoc.row = successor.row;
            prevLoc.col = successor.col;
            v = Math.max( v, Minny( state, alpha, beta, remainingDepth - 1, successor ) );
            if( v >= beta ){
                return v;
            }
            alpha = Math.max( alpha, v );
        }
        return v;
    }

    public int Minny( char[][] state, int alpha, int beta, int remainingDepth, Location lastMove ){
        int terminalState = Utility.UtilityFunction( state, theirSymbol );
        if( terminalState == 10 || remainingDepth == 0 ){
            return -terminalState;
        }
        int v = Integer.MAX_VALUE;
        ArrayList<Location> successors = GetSuccessors( state, lastMove );
        Location prevLoc = successors.get( 0 );
        for( Location successor: successors ){
            //Apply symbol to location change
            state[prevLoc.row][prevLoc.col] = '-';
            state[successor.row][successor.col] = theirSymbol;
            prevLoc.row = successor.row;
            prevLoc.col = successor.col;
            v = Math.min( v, Maxxy( state, alpha, beta, remainingDepth - 1, successor ) );
            if( v <= alpha ){
                return v;
            }
            beta = Math.min( beta, v );
        }
        return v;
    }

    public ArrayList<Location> GetSuccessors( char[][] state, Location lastMove ){
        //Start from last move, spiral out
        ArrayList<Location> successors = new ArrayList<Location>();
        int rightCol, leftCol, topRow, botRow;
        int rowPos, colPos;
        boolean a,b,c,d;
        a = false; b = false; c = false; d = false;
        if( lastMove.col != 0 ){
            leftCol = lastMove.col - 1;
        }
        else{
            leftCol = lastMove.col;
        }
        if( lastMove.col != 7 ){
            rightCol = lastMove.col + 1;
        }
        else{
            rightCol = lastMove.col;
        }
        if( lastMove.row != 0 ){
            topRow = lastMove.row - 1;
        }
        else{
            topRow = lastMove.row;
        }
        if( lastMove.row != 7 ){
            botRow = lastMove.row + 1;
        }
        else
        {
            botRow = lastMove.row;
        }
        //System.out.printf( "leftCol: %d rigthCol: %d botRow: %d topRow: %d\n", leftCol, rightCol, botRow, topRow );
        rowPos = lastMove.row; colPos = lastMove.col;
        while( true ){
            for( ; colPos < rightCol; ++colPos ){
                //System.out.println( "right" );
                if( AdjacentChecker( state, rowPos, colPos ) ){
                    successors.add( locationTable[rowPos][colPos] );
                }
            }
            if( rightCol < BOARD_SIZE - 1 ){
                rightCol++;
            }
            else{
                a = true;
            }
            for( ; rowPos > topRow; rowPos-- ){
                //System.out.println( "up" );
                if( AdjacentChecker( state, rowPos, colPos ) ){
                    successors.add( locationTable[rowPos][colPos] );
                }
            }
            if( topRow > 0 ){
                topRow--;
            }
            else{
                b = true;
            }
            for( ; colPos > leftCol; colPos-- ){
                //System.out.println( "left" );
                if( AdjacentChecker( state, rowPos, colPos ) ){
                    successors.add( locationTable[rowPos][colPos] );
                }
            }
            if( leftCol > 0 ){
                leftCol--;
            }
            else{
                c = true;
            }
            for( ; rowPos < botRow; rowPos++ ){
                //System.out.println( "down" );
                if( AdjacentChecker( state, rowPos, colPos ) ){
                    successors.add( locationTable[rowPos][colPos] );
                }
            }
            if( botRow < BOARD_SIZE - 1 ){
                botRow++;
            }
            else{
                d = true;
            }
            if( a && b && c && d ){
                break;
            }
        }
        return successors;
    }

    public boolean AdjacentChecker( char[][] state, int rowPos, int colPos ){
        //System.out.println( rowPos + " " + colPos );
        if( rowPos - 1 >= 0 && state[rowPos - 1][colPos] != '-' ){
            return true;
        }
        if( colPos - 1 >= 0 && state[rowPos][colPos - 1] != '-' ){
            return true;
        }
        if( rowPos + 1 <= BOARD_SIZE - 1 && state[rowPos + 1][colPos] != '-' ){
            return true;
        }
        if( colPos + 1 <= BOARD_SIZE - 1 && state[rowPos][colPos + 1] != '-' ){
            return true;
        }
        return false;
    }

    public int UtilityFunction( char[][] state ){
        //Express the board as "how good it is". Should only be applied to terminal states ie. iterative deepening has reached max or kill move/win state
        //A board that is in killmove or our win gets +10. return
        //A board that in their killmove or their win gets -10. return
        //ELSE
        //A board gets +1 for pair w/ one option
        //+2 for a pairs w/ 2 options
        //+3 for a triple w/ 1 option
        //REMEMBER, DIAGONALS DO NOT COUNT
        /*
            Certain known killmoves (states where a win is inevitable)
            A triple that is unblocked/w/ 2 options (either side has nothing)
        */
        //This function also acts as our terminal state checker, if -10 or 10, its a terminal state
        return -100;
    }
}
