import java.util.*;

public class EightBoard
{
    private int[] board;

    public EightBoard()
    {
        do
        {
            ArrayList<Integer> originalBoard = new ArrayList<Integer>( Arrays.asList( 0, 1, 2, 3, 4, 5, 6, 7, 8 ) );
            board = new int[9];
            for( int index = 0; index < board.length; ++index )
            {
                board[index] = originalBoard.remove( (int)( Math.random() * originalBoard.size() ) );
            }
        }
        while( !IsValid() );
    }

    public EightBoard( String initialBoard ) throws Exception
    {
        String[] input = initialBoard.split( " " );
        board = new int[9];
        for( int index = 0; index < board.length; ++index )
        {
            board[index] = Integer.parseInt( input[index] );
        }
        if( !IsValid() )
        {
            throw new Exception( "[Error]: Board is not valid" );
        }
    }

    public EightBoard( int[] initialBoard )
    {
        this.board = initialBoard;
    }

    // Copy Constructor
    public EightBoard( EightBoard board )
    {
        this.board = Arrays.copyOf( board.board, board.board.length );
    }

    public boolean IsValid()
    {
        int inversions = 0;
        for( int index = 0; index < board.length - 1; ++index )
        {
            for( int jndex = index + 1; jndex < board.length; ++jndex )
            {
                if( board[index] > board[jndex] && board[index] != 0 && board[jndex] != 0 )
                {
                    ++inversions;
                }
            }
        }
        return inversions % 2 == 0;
    }



    public ArrayList<int[]> GetSuccessors()
    {
        //To expand, treat 0 like a tile, perform a switch with adjacent tiles
        //A tile is adjacent if the distance between it is 1
        ArrayList<int[]> possibleStates = new ArrayList<int[]>();
        //System.out.printf( "\n%s", Arrays.toString( board ) );
        int indexOfZero;
        for( indexOfZero = 0; board[indexOfZero] != 0 ; ++indexOfZero );
        int zeroCol = indexOfZero % 3;
        int zeroRow = indexOfZero / 3;
        for( int index = 0; index < board.length; ++index )
        {
            int curCol = index % 3;
            int curRow = index / 3;
            if( Math.abs( curCol - zeroCol ) + Math.abs( curRow - zeroRow ) == 1 )
            {
                int[] newState = Arrays.copyOf( board, 9 );
                int temp = newState[index];
                newState[index] = newState[indexOfZero];
                newState[indexOfZero] = temp;
                possibleStates.add( newState );
            }
        }
        return possibleStates;
    }

    public int[] GetBoard()
    {
        return board;
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode(board);
    }

    @Override
    public boolean equals( Object obj )
    {
        return Arrays.equals(this.board, ( (EightBoard)obj ).board);
    }

}
