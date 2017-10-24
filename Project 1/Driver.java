import java.util.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class Driver
{
    private static final int NUMOFDEPTHS = 35;

    public static void main( String[] args ) throws Exception
    {
        Scanner in = new Scanner( System.in );
        EightBoard[] problemSet;
        System.out.printf( "Welcome to the eight board solver! Please type input type: \"user\",\"file\",\"random\": ");
        switch( in.nextLine() )
        {
            case "user":
                problemSet = UserInput( in );
                break;
            case "file":
                problemSet = TextInput();
                break;
            case "random":
                problemSet = RandomInput( in );
                break;
            default:
                throw new Exception( "Input type not recognized dammit. Don't screw this up again. Restart the program" );
        }

        System.out.printf( "\nProblems loaded. Would you like to print out the solution paths? (y/n): " );
        boolean printTraceBack = false;
        switch( in.nextLine() )
        {
            case "y":
                System.out.printf( "Yes selected. Printing out solution(s)" );
                printTraceBack = true;
                break;
            case "n":
                System.out.printf( "No selected. Not printing solution(s)" );
                break;
            default:
                System.out.printf( "How did you screw this command up? Defaulting to no." );
                printTraceBack = false;
        }

        System.out.printf( "\nSolving problems using misplaced heuristic..." );
        HashMap<Integer,Integer> nodesPerDepthMisplaced = new HashMap<Integer,Integer>();
        HashMap<Integer,Integer> depthCountMisplaced = new HashMap<Integer,Integer>();
        HashMap<Integer,Long> runtimeMisplaced = new HashMap<Integer,Long>();
        for( int index = 0; index <= NUMOFDEPTHS; ++index )
        {
            nodesPerDepthMisplaced.put( index, 0 );
            depthCountMisplaced.put( index, 0 );
            runtimeMisplaced.put( index, new Long( 0 ) );
        }
        for( EightBoard board : problemSet )
        {
            System.out.printf( "\nSolving %s", Arrays.toString( board.GetBoard() ) );
            Long begin = System.currentTimeMillis();
            Solution solution = SolutionFinder.FindSolution( board, "misplaced" );
            int cost = solution.GetCost();
            runtimeMisplaced.put( cost, runtimeMisplaced.get( cost ) + Math.abs( begin - System.currentTimeMillis()) );
            int nodes = solution.GetNodes();
            System.out.printf( "\nSolved! Cost: %d Nodes: %d", cost, nodes );
            if( printTraceBack )
            {
                System.out.printf( CompileTraceBack( solution ) );
            }
            nodesPerDepthMisplaced.put( cost, nodesPerDepthMisplaced.get( cost ) + nodes );
            depthCountMisplaced.put( cost, depthCountMisplaced.get( cost ) + 1 );
        }

        System.out.printf( "\nSolving problems using distance heuristic..." );
        HashMap<Integer,Integer> nodesPerDepthDistance = new HashMap<Integer,Integer>();
        HashMap<Integer,Integer> depthCountDistance = new HashMap<Integer,Integer>();
        HashMap<Integer,Long> runtimeDistance = new HashMap<Integer,Long>();
        for( int index = 0; index <= NUMOFDEPTHS; ++index )
        {
            nodesPerDepthDistance.put( index, 0 );
            depthCountDistance.put( index, 0 );
            runtimeDistance.put( index, new Long( 0 ) );
        }
        for( EightBoard board : problemSet )
        {
            System.out.printf( "\nSolving %s", Arrays.toString( board.GetBoard() ) );
            Long begin = System.currentTimeMillis();
            Solution solution = SolutionFinder.FindSolution( board, "distance" );
            int cost = solution.GetCost();
            runtimeDistance.put( cost, runtimeDistance.get( cost ) + Math.abs( begin - System.currentTimeMillis()) );
            int nodes = solution.GetNodes();
            System.out.printf( "\nSolved! Cost: %d Nodes: %d", cost, nodes );
            if( printTraceBack )
            {
                System.out.printf( CompileTraceBack( solution ) );
            }
            nodesPerDepthDistance.put( cost, nodesPerDepthMisplaced.get( cost ) + nodes );
            depthCountDistance.put( cost, depthCountMisplaced.get( cost ) + 1 );
        }

        System.out.printf( "\nDone solving problems. Displaying results..." );
        System.out.printf( "\nMisplaced" );
        System.out.printf( "\n\tDepth\tNodes\t\tTime(ms)\tNum Solved");
        for( int index = 0; index <= NUMOFDEPTHS; ++index )
        {
            int num = depthCountMisplaced.get( index );
            if( num != 0 )
            {
                System.out.printf( "\n\t%d\t%d\t\t%d\t\t%d", index, nodesPerDepthMisplaced.get( index ) / num, runtimeMisplaced.get( index ) / num, num );
            }
            else
            {
                System.out.printf( "\n\t%d\t%d\t\t%d\t\t%d", index, 0, 0, 0 );
            }
        }
        System.out.printf( "\nDistance" );
        System.out.printf( "\n\tDepth\tNodes\t\tTime(ms)\tNum Solved");
        for( int index = 0; index <= NUMOFDEPTHS; ++index )
        {
            int num = depthCountMisplaced.get( index );
            if( num  != 0 )
            {
                System.out.printf( "\n\t%d\t%d\t\t%d\t\t%d", index, nodesPerDepthDistance.get( index ) / num, runtimeDistance.get( index ) / num, num );
            }
            else
            {
                System.out.printf( "\n\t%d\t%d\t\t%d\t\t%d", index, 0, 0, 0 );
            }
        }
    }

    public static EightBoard[] RandomInput( Scanner in )
    {
        System.out.printf( "User has selected: Randominput. Please input amount of problems to be generated: " );
        int numOfProblems = Integer.parseInt( in.nextLine() );
        EightBoard[] problemSet = new EightBoard[numOfProblems];
        try
        {
            PrintWriter writer = new PrintWriter("randomlyGeneratedData.txt", "UTF-8");
            System.out.printf( "Generating %d problems...", numOfProblems );
            for( int index = 0; index < numOfProblems; ++index )
            {
                problemSet[index] = new EightBoard();
                writer.println( Arrays.toString( problemSet[index].GetBoard() ) );
            }
            writer.close();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        return problemSet;
    }

    public static EightBoard[] UserInput( Scanner in ) throws Exception
    {
        EightBoard[] output = new EightBoard[1];
        System.out.printf( "User has selected: Userinput. Please input board as space-separated values (e.g. \"0 1 2 3 4 5 6 7 8\" )\n" );
        try
        {
            output[0] = new EightBoard( in.nextLine() );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        return output;
    }

    public static EightBoard[] TextInput() throws Exception
    {
        System.out.printf( "User has selected: Fileinput. I hope you got your input.txt ready." );
        ArrayList<EightBoard> output = new ArrayList<EightBoard>();
        try
        {
            FileReader file = new FileReader( "input.txt" );
            BufferedReader br = new BufferedReader( file );
            String curLine = br.readLine();
            for( ; curLine != null; curLine = br.readLine() )
            {
                if( curLine.charAt( 0 ) == '#' || ( curLine.length() != 9 && curLine.length() != 17 ) )
                {
                    continue;
                }
                if( curLine.length() == 9 )
                {
                    StringBuilder badInput = new StringBuilder( curLine );
                    for( int index = 1; index < 16; index += 2 )
                    {
                        badInput.insert( index, " " );
                    }
                    curLine = badInput.toString();
                }
                output.add( new EightBoard( curLine ) );
            }
            br.close();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        return output.toArray( new EightBoard[output.size()] );
    }

    public static String CompileTraceBack( Solution solution )
    {
        ArrayList<int[]> answer = solution.GetTraceback();
        String output = "";
        for( int index = answer.size() - 1; index >= 0; --index )
        {
            output += "\n" + Arrays.toString( answer.get( index ) );
        }
        return output;
    }

}
