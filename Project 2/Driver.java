import java.util.*;

public class Driver{
    private static final int NUMOFPROBLEMS = 1000;
    private static final int SIZEOFBOARDS = 21;

    public static void main( String[] args ){
        System.out.println( "Welcome to the n queens solver!" );
        System.out.printf( "Generating %d problems...", NUMOFPROBLEMS );
        QueenBoard[] problems = new QueenBoard[NUMOFPROBLEMS];
        for( int index = 0; index < NUMOFPROBLEMS; ++index ){
            problems[index] = new QueenBoard( SIZEOFBOARDS );
        }
        System.out.printf( "\nProblems generated. Solving problems using steepest hill climb" );
        int problemsSolved = 0;
        int totalCost = 0;
        long totalTime = 0;
        for( QueenBoard board: problems ){
            long begin = System.currentTimeMillis();
            int cost = SolutionFinder.SteepestHill( board );
            long time = Math.abs( begin - System.currentTimeMillis() );
            if( cost > 0 ){
                totalCost += cost;
                totalTime += time;
                problemsSolved++;
                System.out.printf( "Solved!: Moves Taken: %d Time(ms): %d ", cost, time );
            }
            else{
                //System.out.println( "Not solved...");
            }
        }
        if( problemsSolved != 0 ){
            System.out.printf( "\nProblems Solved: %d/%d\tAvgCost: %d\tAvgTime: %d", problemsSolved, NUMOFPROBLEMS, (int)totalCost / problemsSolved, (int)totalTime / problemsSolved );
        }
        else{
            System.out.printf( "\nIn %d problems, none were solved. Consider increasing the number of problems.", NUMOFPROBLEMS );
        }
        System.out.printf( "\nSolving problems using genetic alg." );
        problemsSolved = 0;
        totalCost = 0;
        totalTime = 0;
        for( QueenBoard board: problems ){
            long begin = System.currentTimeMillis();
            int cost = SolutionFinder.GeneticAlg( board );
            long time = Math.abs( begin - System.currentTimeMillis() );
            if( cost > 0 ){
                totalCost += cost;
                totalTime += time;
                problemsSolved++;
                System.out.printf( "Solved!: Children Generated: %d Time(ms): %d", cost, time );
            }
            else{
                //System.out.println( "Not solved...");
            }
        }
        System.out.printf( "\nProblems Solved: %d/%d\tAvgCost: %d\t\tAvgTime: %d", problemsSolved, NUMOFPROBLEMS, (int)totalCost / problemsSolved, (int)totalTime / problemsSolved );
    }
}
