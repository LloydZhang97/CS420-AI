import java.util.*;

public class QueenBoard{

    private int[] board;
    private int pairs;

    public QueenBoard( int boardSize ){
        this.board = new int[boardSize];
        for( int index = 0; index < boardSize; ++index ){
            board[index] = (int)( Math.random() * boardSize );
        }
        this.pairs = CountPairs();
    }

    public QueenBoard( int[] board ){
        this.board = board;
        this.pairs = CountPairs();
    }

    // Calculates number of attacking pairs given a board. n^2
    public int CountPairs(){
        int numPairs = 0;
        for( int index = 0; index < board.length; ++index ){
            numPairs += CountPairsForQueen( index, board[index] );
        }
        return numPairs;
    }

    // Returns the lowest attacking pairs next state. Returns null if no better state. Used for steepest hill climb. PLEASE DOUBLE CHECK THIS FOR GOSH SAKES.
    public int[] NextState(){
        int pairsDiff = 0;
        int columnToBeat = -1;
        int rowToBeat = -1;
        for( int index = 0; index < board.length; ++index ){
            int pairs = CountPairsForQueen( index, board[index] );
            for( int jndex = 0; jndex < board.length; ++jndex ){
                if( jndex != board[index] ){
                    int newPairs = CountPairsForQueen( index, jndex );
                    if( newPairs < pairs && pairsDiff < ( pairs - newPairs ) ){
                        pairsDiff = pairs - newPairs;
                        columnToBeat = index;
                        rowToBeat = jndex;
                    }
                }
            }
        }
        if( columnToBeat < 0 )
            return null;
        else{
            int[] betterBoard = Arrays.copyOf( board, board.length );
            betterBoard[columnToBeat] = rowToBeat;
            return betterBoard;
        }
    }

    // Given a column (index) and row (value), how many pairs is that queen in? O(n)
    private int CountPairsForQueen( int column, int row ){
        int numPairs = 0;
        for( int index = column + 1; index < board.length; ++index ){
            if( row == board[index] ) //They're on the same row!
                numPairs++;
            else if( Math.abs( row - board[index] ) == Math.abs( column - index ) ) //They're on the same diagonal!
                numPairs++;
        }
        return numPairs;
    }

    public int GetPairs(){
        return pairs;
    }

    public int[] GetBoard(){
        return board;
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode( board );
    }

    @Override
    public boolean equals( Object obj )
    {
        return Arrays.equals(this.board, ((QueenBoard)obj).board);
    }
}
