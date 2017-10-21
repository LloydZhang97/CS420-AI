import java.util.*;

public class SolutionFinder
{
    public static Solution FindSolution( EightBoard aBoard, String heuristic ) throws Exception
    {
        EightBoard board = new EightBoard( aBoard );
        if( !heuristic.equals( "misplaced" ) && !heuristic.equals( "distance" ) )
        {
            throw new Exception( "[Error]: Unknown param for heuristic algorithm found" );
        }

        //The tree set should always be organized, with least cost being in beginning
        TreeSet<Node> frontierPool = new TreeSet<Node>( stateComparator );
        HashSet<int[]> exploredMap = new HashSet<int[]>();
        int numNodesGenerated = 0;

        if( heuristic.equals( "misplaced" ) )
        {
            frontierPool.add( new Node( board, MisplacedHeuristic( board ), 0, 0 ) );
        }
        else if( heuristic.equals( "distance") )
        {
            frontierPool.add( new Node( board, DistanceHeuristic( board ), 0, 0 ) );
        }

        while( true )
        {
            // System.out.printf( "\nCurrent Frontier Pool: ");
            // for( Node temp : frontierPool )
            // {
            //     System.out.printf( "\n%s gn: %d hn: %d", Arrays.toString( temp.board.GetBoard() ), temp.gn, temp.hn );
            // }
            //Pop first index of set
            Node expandingNode = frontierPool.pollFirst();
            //Thread.sleep( 50 );
            //System.out.printf( "\nExpanding: %s %d %d %d", Arrays.toString( expandingNode.board.GetBoard() ), expandingNode.gn, expandingNode.hn, exploredMap.size() );
            //Check if final state
            if( expandingNode.hn == 0 ) //If this doesn't work, use expandingNode.board.IsSolved()
            {
                return new Solution( expandingNode.gn, numNodesGenerated );
            }
            ArrayList<int[]> successors = expandingNode.board.GetSuccessors();
            numNodesGenerated += successors.size();
            //Expand loop
            for( int[] successor : successors )
            {
                //Make sure we don't go back on ourselves
                if( !Contains( exploredMap, successor ) )
                {
                    EightBoard newBoard = new EightBoard( successor );
                    if( heuristic.equals( "misplaced" ) )
                    {
                        Node newNode = new Node( newBoard, MisplacedHeuristic( newBoard ), expandingNode.gn + 1, expandingNode.depth + 1 );
                        frontierPool.add( newNode );
                        //System.out.printf( "\nAdding board: %s", Arrays.toString( successor ) );
                    }
                    else if( heuristic.equals( "distance") )
                    {
                        Node newNode = new Node( newBoard, DistanceHeuristic( newBoard ), expandingNode.gn + 1, expandingNode.depth + 1 );
                        frontierPool.add( newNode );
                    }
                }
            }
            //The expansion state should be initialized w/ the new board, h(n), and g(n) to calculate...
            //h(n) is calculated on the spot using one of the 2 heuristics
            //g(n) is the current expanding node's g(n) + 1
            //Add current node to explored set
            exploredMap.add( expandingNode.board.GetBoard() );
            // System.out.println( "Explored Set: ");
            // for( int[] temp : exploredMap )
            // {
            //     System.out.println( Arrays.toString( temp ) );
            // }
        }
        //return -1;
    }

    public static int MisplacedHeuristic( EightBoard eightBoard )
    {
        int numMisplaced = 0;
        int[] board = eightBoard.GetBoard();
        for( int index = 0; index < board.length; ++index )
        {
            if( index != board[index] )
            {
                ++numMisplaced;
            }
        }
        return numMisplaced;
    }

    //Please double check this heuristic alg.
    public static int DistanceHeuristic( EightBoard eightBoard )
    {
        int numDistanced = 0; //1 2 4 0 5 6 8 3 7
        int[] board = eightBoard.GetBoard();
        for( int index = 0; index < board.length; ++index )
        {
            int tile = board[index];
            int verticalDis = Math.abs( index % 3 - tile % 3 ); //tile is dest, index is source
            int horizontalDis = Math.abs( index / 3 - tile / 3 );
            numDistanced += ( verticalDis + horizontalDis );
        }
        return numDistanced;
    }

    private static boolean Contains( HashSet<int[]> list, int[] target )
    {
        for( int[] cur : list )
        {
            if( Arrays.equals( target, cur ) )
                return true;
        }
        return false;
    }

    private static class Node
    {
        private EightBoard board;
        private int hn;
        private int gn;
        private int depth;

        public Node( EightBoard board, int hn, int gn, int depth )
        {
            this.board = board;
            this.hn = hn;
            this.gn = gn;
            this.depth = depth;
        }
    }

    public static Comparator<Node> stateComparator = new Comparator<Node>() { //this should really be at the top

        @Override
        public int compare(Node s1, Node s2) {
           int state1 = s1.hn + s1.gn;
           int state2 = s2.hn + s2.gn;
           int result = state1 - state2;
           return result == 0 ? -1 : result;
        }
    };
}
