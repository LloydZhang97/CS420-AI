import java.util.*;

public class Driver{

    public static final Location STARTING_MOVE = new Location( 4, 4 );
    public static final int SEARCH_DEPTH = 6;

    public static void main( String[] args ){
        Scanner in = new Scanner( System.in );
        Location inputMove = null, outputMove = null;
        char mySymbol = 'A';
        char theirSymbol = 'B';

        char[][] board = {{'-','-','-','-','-','-','-','-'},
                          {'-','-','-','-','-','-','-','-'},
                          {'-','-','-','-','-','-','-','-'},
                          {'-','-','-','-','-','-','-','-'},
                          {'-','-','-','-','-','-','-','-'},
                          {'-','-','-','-','-','-','-','-'},
                          {'-','-','-','-','-','-','-','-'},
                          {'-','-','-','-','-','-','-','-'}};
        String history = "Player vs. Opponent\n";
        int playerHistoryTrack = 1;
        String strBoard = "  1 2 3 4 5 6 7 8\nA - - - - - - - -\nB - - - - - - - -\nC - - - - - - - -\nD - - - - - - - -\nE - - - - - - - -\nF - - - - - - - -\nG - - - - - - - -\nH - - - - - - - -";
        System.out.println( "Welcome to the connect 4 not connect 4 game winner" );
        System.out.print( "How much time are you giving me? (sec): " );
        long time = (long)Integer.parseInt( in.nextLine() );
        System.out.print( "Am I going first?(y/n): " );
        String input = in.nextLine();
        SolutionFinder sf = null;
        if( input.equals( "y" ) ){
            mySymbol = 'X'; theirSymbol = 'O';
            board[STARTING_MOVE.row][STARTING_MOVE.col] = 'X';
            strBoard = UpdateBoard( strBoard, STARTING_MOVE.row, STARTING_MOVE.col, 'X' );
            history += playerHistoryTrack + ". " + (char)(97 + STARTING_MOVE.row) + (STARTING_MOVE.col + 1) + " ";
            System.out.println( strBoard );
            System.out.println( history );
            sf = new SolutionFinder( 'X', 'O', time, SEARCH_DEPTH );
        }
        else if( input.equals( "n" ) ){
            mySymbol = 'O'; theirSymbol = 'X';
            sf = new SolutionFinder( 'O', 'X', time, SEARCH_DEPTH + 1 );
            history += playerHistoryTrack + ". - ";
        }
        while( true ){
            System.out.println( "Whats the move?" );
            input = in.nextLine();
            inputMove = ParseMove( input );
            //System.out.println( board[inputMove.row][inputMove.col] );
            if( board[inputMove.row][inputMove.col] != '-' ){
                System.out.println( "Please enter a valid move idiot" );
                continue;
            }
            board[inputMove.row][inputMove.col] = theirSymbol;
            strBoard = UpdateBoard( strBoard, inputMove.row, inputMove.col, theirSymbol );
            history += input + "\n";
            char[][] inputBoard =  new char[8][8];
            for( int i = 0; i < inputBoard.length; i++ ){
                System.arraycopy( board[i], 0, inputBoard[i], 0, board[i].length );
            }
            outputMove = sf.AlphaBetaSearch( inputBoard, inputMove );
            Location newLoc = sf.FinishIt( board, mySymbol );
            boolean win = false;
            if( newLoc != null ){
                win = true;
            }
            board[outputMove.row][outputMove.col] = mySymbol;
            strBoard = UpdateBoard( strBoard, outputMove.row, outputMove.col, mySymbol );
            playerHistoryTrack++;
            history += playerHistoryTrack + ". " + (char)(97 + outputMove.row) + (outputMove.col + 1) + " ";
            if( win ){
                history += " won";
            }
            System.out.println( strBoard );
            System.out.println( history );
        }
    }

    public static String UpdateBoard( String str, int row, int col, char symbol ){
        return str.substring( 0, 17 + ( 2 * ( row + 1 ) ) + ( 16 * row ) + ( 2 * col + 1 ) ) + symbol + str.substring( 17 + ( 2 * ( row + 1 ) ) + ( 16 * row ) + ( 2 * col + 1 ) + 1 );
    }

    public static Location ParseMove( String str ){
        return new Location( str.charAt(0) - 97, str.charAt( 1 ) - 49 );
    }
}
