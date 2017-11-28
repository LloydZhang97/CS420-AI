public class Location{
    public int row;
    public int col;

    public Location( int row, int col ){
        this.row = row;
        this.col = col;
    }

    public Location( Location loc ){
        this.row = loc.row;
        this.col = loc.col;
    }

    public String toString(){
        return "row: " + row + " col: " + col;
    }
}
