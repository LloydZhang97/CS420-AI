import java.util.*;

public class SolutionFinder{
    //Consider using a table of Location
    //@TODO CHECK TO SEE IF US OR THE OTHER SIDE WON SUDDENLY. WE DO NOT SEE 4 IN A ROWS IN THE UTILITY FUNCTION
    private long time;
    private long ttl;
    private Location[][] locationTable;
    private char mySymbol;
    private char theirSymbol;
    private int BOARD_SIZE = 8;
    private int SEARCH_DEPTH = -1;
    private static char[][] board = {{'-', '-', '-', '-', '-', '-', '-', '-'},
                                     {'-', '-', '-', '-', '-', '-', '-', '-'},
                                     {'-', '-', '-', '-', '-', '-', '-', '-'},
                                     {'-', '-', '-', '-', '-', '-', '-', '-'},
                                     {'-', '-', '-', '-', 'X', '-', '-', '-'},
                                     {'-', '-', '-', '-', '-', '-', '-', '-'},
                                     {'-', '-', '-', '-', '-', '-', '-', '-'},
                                     {'-', '-', '-', '-', '-', '-', '-', '-'}};

    public SolutionFinder( char mySymbol, char theirSymbol, long ttl , int searchDepth ){
        this.mySymbol = mySymbol;
        this.theirSymbol = theirSymbol;
        this.ttl = ttl * 1000;
        this.SEARCH_DEPTH = searchDepth;
        locationTable = new Location[BOARD_SIZE][BOARD_SIZE];
        for( int i = 0; i < BOARD_SIZE; i++ ){
            for( int j = 0; j < BOARD_SIZE; j++ ){
                locationTable[i][j] = new Location( i, j );
            }
        }
    }

    public static void main( String[] args ){
        SolutionFinder sf = new SolutionFinder( 'O', 'X', 30, 5 );
        System.out.println( sf.AlphaBetaSearch( board, new Location( 4, 4 ) ) );
    }

    public Location AlphaBetaSearch( char[][] state, Location lastMove ){
        time = System.currentTimeMillis();
        Location newLoc = FinishIt( state, mySymbol );
        if( newLoc != null ){
            return newLoc;
        }
        return InitialMaxxy( state, Integer.MIN_VALUE, Integer.MAX_VALUE, SEARCH_DEPTH, lastMove );
    }

    public Location InitialMaxxy( char[][] state, int alpha, int beta, int remainingDepth, Location lastMove ){
        //Do a terminal check (whether kill move or won state)
        // for( int i = 0; i < state.length; i++ ){
        //    System.out.println( Arrays.toString( state[i] ) );
        // }
        int v = Integer.MIN_VALUE;
        ArrayList<Location> successors = GetSuccessors( state, lastMove );
        // System.out.println( Arrays.toString( successors.toArray() ) + " " + lastMove );
        Location prevLoc = new Location( successors.get( 0 ) );
        Location returnLoc = new Location( successors.get( 0 ) );
        // System.out.println( "Successor List" );
        for( Location successor: successors ){
            if( System.currentTimeMillis() - time > ttl ){
                if( v == Integer.MIN_VALUE ){
                    v = Integer.MAX_VALUE;
                }
                System.out.println( "Timeout" );
                SEARCH_DEPTH -= 2;
                break;
            }
            //Apply symbol to location change
            state[prevLoc.row][prevLoc.col] = '-';
            state[successor.row][successor.col] = mySymbol;
            prevLoc.row = successor.row;
            prevLoc.col = successor.col;
            int minReturn =  Minny( state, alpha, beta, remainingDepth - 1, successor );
            if( minReturn > v ){
                returnLoc.row = successor.row;
                returnLoc.col = successor.col;
                v = minReturn;
            }
            alpha = Math.max( alpha, v );
        }
        state[prevLoc.row][prevLoc.col] = '-';
        //Check for sneaky wins by opponent
        for( Location successor: successors ){
            state[prevLoc.row][prevLoc.col] = '-';
            state[successor.row][successor.col] = theirSymbol;
            prevLoc.row = successor.row;
            prevLoc.col = successor.col;
            if( fourRowChecker( state, theirSymbol ) ){
                returnLoc.row = successor.row;
                returnLoc.col = successor.col;
                v = 0;
                break;
            }
        }
        state[prevLoc.row][prevLoc.col] = '-';
        System.out.println( "Utility: " + v );
        if( v >= 20 ){
            System.out.println( "I have probably won, or there is buggy code" );
        }
        else if( v <= -20 ){
            System.out.println( "I have probably lost. I bring dishonor to my family." );
            //System.exit(0);
        }
        return returnLoc;
    }

    public int Maxxy( char[][] state, int alpha, int beta, int remainingDepth, Location lastMove ){

        //Do a terminal check (whether kill move or won state)
        int myUtility = Utility.UtilityFunction( state, mySymbol );
        int theirUtility = -Utility.UtilityFunction( state, theirSymbol );
        // try{
        //     Thread.sleep( 100 );
        // }
        //catch( Exception e ){}
        if( theirUtility <= -20 ){
            // System.out.println( "Leaf Max: " + ( theirUtility + myUtility ) );
            // for( int i = 0; i < state.length; i++ ){
            //     System.out.println( Arrays.toString( state[i] ) );
            // }
            return theirUtility;
        }
        if( remainingDepth == 0 ){
            //System.out.println( "Leaf: " + ( myUtility + theirUtility ) );
            // System.out.println( "Leaf Max: " + ( theirUtility + myUtility ) );
            // for( int i = 0; i < state.length; i++ ){
            //     System.out.println( Arrays.toString( state[i] ) );
            // }
            return myUtility + theirUtility;
        }
        int v = Integer.MIN_VALUE;
        ArrayList<Location> successors = GetSuccessors( state, lastMove );
        Location prevLoc = new Location( successors.get( 0 ) );
        for( Location successor: successors ){
            if( System.currentTimeMillis() - time > ttl ){
                if( v == Integer.MIN_VALUE ){
                    v = Integer.MAX_VALUE;
                }
                break;
            }
            //Apply symbol to location change
            state[prevLoc.row][prevLoc.col] = '-';
            state[successor.row][successor.col] = mySymbol;
            prevLoc.row = successor.row;
            prevLoc.col = successor.col;
            v = Math.max( v, Minny( state, alpha, beta, remainingDepth - 1, successor ) );
            if( v >= beta ){
                state[prevLoc.row][prevLoc.col] = '-';
                //System.out.println( "[Prune] Remaining Depth Max: " + remainingDepth + " v: " + v );
                return v;
            }
            alpha = Math.max( alpha, v );
        }
        //System.out.println( "Remaining Depth Max: " + remainingDepth + " v: " + v );
        state[prevLoc.row][prevLoc.col] = '-';
        return v;
    }

    public int Minny( char[][] state, int alpha, int beta, int remainingDepth, Location lastMove ){
        int myUtility = Utility.UtilityFunction( state, mySymbol );
        int theirUtility = -Utility.UtilityFunction( state, theirSymbol );
        // for( int i = 0; i < state.length; i++ ){
        //     System.out.println( Arrays.toString( state[i] ) );
        // }
        // try{
        //     Thread.sleep( 100 );
        // }
        //catch( Exception e ){}
        if( myUtility >= 20 ){
            // System.out.println( "Leaf Max: " + ( theirUtility + myUtility ) );
            // for( int i = 0; i < state.length; i++ ){
            //     System.out.println( Arrays.toString( state[i] ) );
            // }
            return myUtility;
        }
        if( remainingDepth == 0 ){
            //System.out.println( "Leaf: " + ( myUtility + theirUtility ) );
            // System.out.println( "Leaf Min: " + ( theirUtility + myUtility ) );
            // for( int i = 0; i < state.length; i++ ){
            //     System.out.println( Arrays.toString( state[i] ) );
            // }
            return myUtility + theirUtility;
        }
        int v = Integer.MAX_VALUE;
        ArrayList<Location> successors = GetSuccessors( state, lastMove );
        Location prevLoc = new Location( successors.get( 0 ) );
        for( Location successor: successors ){
            if( System.currentTimeMillis() - time > ttl ){
                if( v == Integer.MIN_VALUE ){
                    v = Integer.MAX_VALUE;
                }
                break;
            }
            //Apply symbol to location change
            state[prevLoc.row][prevLoc.col] = '-';
            state[successor.row][successor.col] = theirSymbol;
            prevLoc.row = successor.row;
            prevLoc.col = successor.col;
            v = Math.min( v, Maxxy( state, alpha, beta, remainingDepth - 1, successor ) );
            //System.out.println( v );
            if( v <= alpha ){
                //System.out.println( "[Prune] Remaining Depth Min: " + remainingDepth + " v: " + v );
                state[prevLoc.row][prevLoc.col] = '-';
                return v;
            }
            beta = Math.min( beta, v );
        }
        //System.out.println( "Remaining Depth Min: " + remainingDepth + " v: " + v );
        state[prevLoc.row][prevLoc.col] = '-';
        return v;
    }

    public ArrayList<Location> GetSuccessors( char[][] state, Location lastMove ){
        //Start from last move, spiral out
        // for( int i = 0; i < state.length; i++ ){
        //     System.out.println( Arrays.toString( state[i] ) );
        // }

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
                if( state[rowPos][colPos] == '-' && AdjacentChecker( state, rowPos, colPos ) ){
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
                if( state[rowPos][colPos] == '-' && AdjacentChecker( state, rowPos, colPos ) ){
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
                if( state[rowPos][colPos] == '-' && AdjacentChecker( state, rowPos, colPos ) ){
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
                if( state[rowPos][colPos] == '-' && AdjacentChecker( state, rowPos, colPos ) ){
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
        //System.out.println( Arrays.toString( successors.toArray() ) );
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
        //A board that is in killmove or our win gets +20. return
        //A board that in their killmove or their win gets -20. return
        //ELSE
        //A board gets +1 for pair w/ one option
        //+2 for a pairs w/ 2 options
        //+3 for a triple w/ 1 option
        //REMEMBER, DIAGONALS DO NOT COUNT
        /*
            Certain known killmoves (states where a win is inevitable)
            A triple that is unblocked/w/ 2 options (either side has nothing)
        */
        //This function also acts as our terminal state checker, if -20 or 20, its a terminal state
        return -100;
    }

    public Location FinishIt( char[][] state, char symbol ){
        for( int i = 0; i < BOARD_SIZE; i++ ){
            for( int j = 0; j < BOARD_SIZE; j++ ){
                int count = 3;
                while( j < BOARD_SIZE && count > 0 && state[i][j] == symbol ){
                    j++;
                    count--;
                }
                if( count == 0 ){
                    if( j < BOARD_SIZE && state[i][j] == '-' ){
                        return locationTable[i][j];
                    }
                    if( j - 4 >= 0 && state[i][j - 4] == '-' ){
                        return locationTable[i][j - 4];
                    }
                }
            }
        }
        for( int i = 0; i < BOARD_SIZE; i++ ){
            for( int j = 0; j < BOARD_SIZE; j++ ){
                int count = 3;
                while( j < BOARD_SIZE && count > 0 && state[j][i] == symbol ){
                    j++;
                    count--;
                }
                if( count == 0 ){
                    if( j < BOARD_SIZE && state[j][i] == '-' ){
                        return locationTable[j][i];
                    }
                    if( j - 4 >= 0 && state[j - 4][i] == '-' ){
                        return locationTable[j - 4][i];
                    }
                }
            }
        }
        return null;
    }

    public boolean fourRowChecker( char[][] state, char symbol ){
        for( int i = 0; i < BOARD_SIZE; i++ ){
            for( int j = 0; j < BOARD_SIZE; j++ ){
                int count = 4;
                while( j < BOARD_SIZE && count > 0 && state[i][j] == symbol ){
                    j++;
                    count--;
                }
                if( count == 0 ){
                    return true;
                }
            }
        }
        for( int i = 0; i < BOARD_SIZE; i++ ){
            for( int j = 0; j < BOARD_SIZE; j++ ){
                int count = 4;
                while( j < BOARD_SIZE && count > 0 && state[j][i] == symbol ){
                    j++;
                    count--;
                }
                if( count == 0 ){
                    return true;
                }
            }
        }
        return false;
    }
}
