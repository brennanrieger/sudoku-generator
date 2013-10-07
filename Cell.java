public class Cell
{
    // fields
    public int val;    
    public boolean empty; // empty means user can no longer see cell's value 
    public boolean[] row;
    public boolean[] column;
    public boolean[] box;
    private int n;
    private int len;
    public int user_val;
    
    // methods
    
    // Cell constructor
    public Cell(int init_val, int init_n)
    {
        // basic Cell info 
        val = init_val;
        n = init_n;
        len = n * n;
        empty = false;
        user_val = 0;
        
        // calculate index of Cell's value        
        int index = val - 1;
        
        // make three arrays of booleans, initialized as all false except at index
        row = new boolean[len];
        row[index] = true;
        
        column = new boolean[len]; 
        column[index] = true;
                
        box = new boolean[len];
        box[index] = true;
    }
    
    public void emptyCell()
    {
        empty = true;
    }
    
}