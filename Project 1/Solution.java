public class Solution
{
    private int cost;
    private int nodesGen;

    public Solution( int cost, int nodesGen )
    {
        this.cost = cost;
        this.nodesGen = nodesGen;
    }

    public int GetCost()
    {
        return cost;
    }

    public int GetNodes()
    {
        return nodesGen;
    }
}
