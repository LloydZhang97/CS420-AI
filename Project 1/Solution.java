import java.util.ArrayList;

public class Solution
{
    private int cost;
    private int nodesGen;
    private ArrayList<int[]> traceback;

    public Solution( int cost, int nodesGen, ArrayList<int[]> traceback )
    {
        this.cost = cost;
        this.nodesGen = nodesGen;
        this.traceback = traceback;
    }

    public int GetCost()
    {
        return cost;
    }

    public int GetNodes()
    {
        return nodesGen;
    }

    public ArrayList<int[]> GetTraceback()
    {
        return traceback;
    }
}
