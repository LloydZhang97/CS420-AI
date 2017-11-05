import java.util.*;

public class SolutionFinder{
    private static final double CROSSOVERPOINT = 0.50;
    private static final double MUTATIONCHANCE = 0.2;
    private static final int MAXSIZEOFPOP = 10000;

    public static int SteepestHill( QueenBoard board ){
        int cost = 0;
        while( true ){
            int[] nextBoard = board.NextState();
            if( nextBoard == null ){
                break;
            }
            board = new QueenBoard( nextBoard );
            cost++;
            try{
                //Thread.sleep( 250 );
            } catch (Exception e){}
            //System.out.println( Arrays.toString( board.GetBoard() ));
        }
        if( board.GetPairs() == 0 ){
            System.out.printf( "\n%s ", Arrays.toString( board.GetBoard() ));
            return cost;
        }
        else{
            return -1;
        }
    }

    public static int GeneticAlg( QueenBoard adam ){
        ArrayList<QueenBoard> population = new ArrayList<QueenBoard>(); //Maintains list of pops, sorts after every full iteration
        HashSet<Integer> existingPops = new HashSet<Integer>(); //Hash set stores current pop and dead pops, don't want to repeat pops.

        // Add the initial two pops to begin population
        population.add( adam );
        existingPops.add( (Integer)adam.hashCode() );
        int[] eveBoard =  Arrays.copyOf( adam.GetBoard(), adam.GetBoard().length );
        QueenBoard eve = new QueenBoard( Mutate( eveBoard ) );
        population.add( eve );
        existingPops.add((Integer) eve.hashCode() );

        // Begin the pop simulation
        while( true ){
            ArrayList<QueenBoard> newPops = new ArrayList<QueenBoard>();
            for( int index = 0; index < MAXSIZEOFPOP; ++index ){
                //Selection
                QueenBoard father = population.get( (int) ( Math.random() * population.size() ) );
                QueenBoard mother = population.get( (int) ( Math.random() * population.size() ) );
                //Crossover
                int[] childBoard = HaveSex( father, mother );
                //Mutation
                if( Math.random() < MUTATIONCHANCE ){
                    childBoard = Mutate( childBoard );
                }
                QueenBoard child = new QueenBoard( childBoard );
                if( !existingPops.contains( child.hashCode() ) ){
                    newPops.add( child );
                    existingPops.add( (Integer)child.hashCode() );
                }
            }

            //Add new pop to old pop, cull if necessary.
            //System.out.println( "Population" );
            population.addAll( newPops );
            population.sort( stateComparator );
            // for( QueenBoard board: population ){
            //     System.out.println( Arrays.toString( board.GetBoard() ) + "\t" + board.GetPairs() );
            // }
            if( population.size() > MAXSIZEOFPOP ){
                // for( int index = 1; index < population.size(); ++index ){
                //     population.remove( index );
                // }
                population.subList( MAXSIZEOFPOP, population.size() ).clear();
            }
            // try{
            //     Thread.sleep( 250 );
            // } catch (Exception e){}
            //System.out.println( Arrays.toString( population.get( 0 ).GetBoard() ));
            QueenBoard possibleAnswer = population.get( 0 );
            if( possibleAnswer.GetPairs() == 0 ){ //WE HAVE THE MIRICLE CHILD
                System.out.printf( "\n%s ", Arrays.toString( possibleAnswer.GetBoard() ) );
                return existingPops.size();
            }
        }
    }

    // Method to do the intercourse. Does not modify parameters.
    private static int[] HaveSex( QueenBoard father, QueenBoard mother ){
        int[] fatherBoard = father.GetBoard();
        int[] motherBoard = mother.GetBoard();
        int[] babyBoard = new int[fatherBoard.length];
        System.arraycopy( fatherBoard, 0, babyBoard, 0, (int) ( CROSSOVERPOINT * babyBoard.length ) );
        System.arraycopy( motherBoard, (int)( CROSSOVERPOINT * babyBoard.length ) , babyBoard, (int)( CROSSOVERPOINT * babyBoard.length ), motherBoard.length - (int)( CROSSOVERPOINT * babyBoard.length ) );
        return babyBoard;
    }

    // Method to mutate. Be aware, any board passed WILL be modified
    private static int[] Mutate( int[] board ){
        board[(int)( Math.random() * board.length )] = (int)( Math.random() * board.length );
        return board;
    }

    public static Comparator<QueenBoard> stateComparator = new Comparator<QueenBoard>() { //this should really be at the top
        @Override
        public int compare(QueenBoard s1, QueenBoard s2) {
           int result = s1.GetPairs() - s2.GetPairs();
           return result;
        }
    };
}
