import java.util.*;

public class Driver
{
    private static final int NUMOFPROBLEMS = 101;

    public static void main( String[] args ) throws Exception
    {
        System.out.printf( "Welcome to the eight board solver!");
        EightBoard[] problemSet = new EightBoard[NUMOFPROBLEMS];
        System.out.printf( "\nGenerating %d problems...", NUMOFPROBLEMS );
        for( int index = 0; index < NUMOFPROBLEMS; ++index )
        {
            problemSet[index] = new EightBoard();
        }

        // String testArray = "4 3 1 5 0 2 6 7 8";
        // EightBoard testProblem = new EightBoard( testArray );
        // SolutionFinder.FindSolution( testProblem, "misplaced" );
        // System.out.println( "Test Problem Solved" );

        System.out.printf( "\nProblems generated, solving problems using misplaced heuristic..." );
        HashMap<Integer,Integer> nodesPerDepthMisplaced = new HashMap<Integer,Integer>();
        HashMap<Integer,Integer> depthCountMisplaced = new HashMap<Integer,Integer>();
        for( int index = 0; index <= 30; ++index )
        {
            nodesPerDepthMisplaced.put( index, 0 );
            depthCountMisplaced.put( index, 0 );
        }
        for( EightBoard board : problemSet )
        {
            System.out.printf( "\nSolving %s", Arrays.toString( board.GetBoard() ) );
            Solution solution = SolutionFinder.FindSolution( board, "misplaced" );
            int cost = solution.GetCost();
            int nodes = solution.GetNodes();
            System.out.printf( "\nSolved! Cost: %d Nodes: %d", cost, nodes );
            nodesPerDepthMisplaced.put( cost, nodesPerDepthMisplaced.get( cost ) + nodes );
            depthCountMisplaced.put( cost, depthCountMisplaced.get( cost ) + 1 );
        }

        System.out.printf( "\nSolving problems using distance heuristic..." );
        HashMap<Integer,Integer> nodesPerDepthDistance = new HashMap<Integer,Integer>();
        HashMap<Integer,Integer> depthCountDistance = new HashMap<Integer,Integer>();
        for( int index = 0; index <= 30; ++index )
        {
            nodesPerDepthDistance.put( index, 0 );
            depthCountDistance.put( index, 0 );
        }
        for( EightBoard board : problemSet )
        {
            System.out.printf( "\nSolving %s", Arrays.toString( board.GetBoard() ) );
            Solution solution = SolutionFinder.FindSolution( board, "distance" );
            int cost = solution.GetCost();
            int nodes = solution.GetNodes();
            System.out.printf( "\nSolved! Cost: %d Nodes: %d", cost, nodes );
            nodesPerDepthDistance.put( cost, nodesPerDepthMisplaced.get( cost ) + nodes );
            depthCountDistance.put( cost, depthCountMisplaced.get( cost ) + 1 );
        }

        System.out.printf( "\nDone solving problems. Displaying results..." );
        System.out.printf( "\nMisplaced" );
        for( int index = 0; index <= 30; ++index )
        {
            if( depthCountMisplaced.get( index )  != 0 )
            {
                System.out.printf( "\n\t%d\t%f", index, (double) ( nodesPerDepthMisplaced.get( index ) / depthCountMisplaced.get( index ) ) );
            }
            else
            {
                System.out.printf( "\n\t%d\t%f", index, 0.0 );
            }
        }
        System.out.printf( "\nDistance" );
        for( int index = 0; index <= 30; ++index )
        {
            if( depthCountDistance.get( index )  != 0 )
            {
                System.out.printf( "\n\t%d\t%f", index, (double) ( nodesPerDepthDistance.get( index ) / depthCountDistance.get( index ) ) );
            }
            else
            {
                System.out.printf( "\n\t%d\t%f", index, 0.0 );
            }
        }
    }

}
