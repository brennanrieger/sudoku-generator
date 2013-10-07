import java.util.Random;

public class SudokuBoard
{
    // fields
    private int n;
    public int len;
    public Cell[][] board;
    private int[][] blueprint;
    public int emptycount;
    
    // methods
    
    // SudokuBoard constructor
    public SudokuBoard(int init_n)
    {
        // initialize new board 
        n = init_n;
        len = n * n;
        board = new Cell[len][len];
        emptycount = 0;
        
        // fill and scramble int array and use it to fill board 
        loadBlueprint();
        scrambleBlueprint();
        makeBoard();
    }
    
    private void loadBlueprint()
    {
        // make array of diagonals
        int[][] diagonals = new int[len][len];
        for (int i = 0; i < len; i++)
        {
            for (int j = 0; j < len; j++)
            {
                diagonals[i][j] = (i + j) % len + 1;
            }
        }
        
        // rearrange rows so boxes are valid
        blueprint = new int[len][len];
        for (int i = 0; i < len; i++)
        {
            for (int j = 0; j < len; j++)
            {
                blueprint[i][j] = diagonals[(i % n) * n + (i / n)][j];
            }
        }
    
    }
    
    // scrambles blueprint sudoku to get different starting permutation
    private void scrambleBlueprint()
    {
        
        // exchange numbers 1 through n randomly
        int[] mapping = randomArray(len);
        for (int i = 0; i < len; i++)
        {
            for (int j = 0; j < len; j++)
            {
                blueprint[i][j] = mapping[blueprint[i][j] - 1];
            }
        }
         
        // randomly switch rows within same box blocks
        int[][] temp = new int[len][len];
        for (int i =0; i< len; i++)
        {
            for (int j = 0; j < len; j++)
            {
                temp[i][j] = blueprint[i][j];
            }

        }
       
        for (int k = 0; k < n; k++)
        {
            int[] rowmapping = randomArray(n);
                for (int i = n * k; i < n * (k + 1); i++)
                {
                    blueprint[i] = temp[rowmapping[i - (n * k)] + (n * k) - 1];
                }
         
        }
        
        
        // randomly switch columns within same box blocks
        int[][] temp2 = new int[len][len];
        for (int i =0; i< len; i++)
        {
            for (int j =0; j<len; j++)
            {
                temp2[i][j] = blueprint[i][j];
            }
        }

        for (int k = 0; k < n; k++)
        {
            int[] colmapping = randomArray(n);
            for (int i = 0; i < len; i++)
            {
                for (int j = n * k; j < n * (k + 1); j++)
                {
                    blueprint[i][j] = temp2[i][colmapping[j - (n * k)] + (n * k) - 1];
                }
            }
        }
    }
    

    private void makeBoard()
    {
        // fill initial board using scrambled blueprint array
        Cell[][] init_board = new Cell[len][len];
        for (int i = 0; i < len; i++)
        {
            for (int j = 0; j < len; j++)
            {
                init_board[i][j] = new Cell(blueprint[i][j], n);
            }
        }
        
        // set board
        board = init_board;
    }    
    
    // randomly distributes numbers 1 through m in an array of length m
    public int[] randomArray(int m)
    {
        int[] randarr = new int[m];
        
        // add numbers 1 through n to array
        for (int i = 0; i < m; i++)
        {
            randarr[i] = i + 1;
        }
        
        // random number generator
        Random generator = new Random();
        
        // scramble array through random swaps
        for (int i = m; i > 1; i--)
        {
            int r = Math.abs(generator.nextInt() % i);
            int temp = randarr[i - 1];
            randarr[i - 1] = randarr[r];
            randarr[r] = temp;
        }
                
        return randarr;
    }
    
    // checks if cell we want to empty could hold any other value 
    public boolean cellCheck(int xcoord, int ycoord)
    {
        // iterates through cell's internal boolean arrays
        for (int i = 0; i < len; i++)
        {
            // checks if there is any value that all three arrays indicate is allowed
            // besides the current value in the cell
            if (board[xcoord][ycoord].box[i] == true && 
                board[xcoord][ycoord].row[i] == true &&
                board[xcoord][ycoord].column[i] == true &&
                i != (board[xcoord][ycoord].val) - 1)
                
                // if so, do not allow cell to be emptied
                return false;
        }
        
        // otherwise allow cell to be emptied
        return true;
        
    }
    
    // checks if cells in same row as the cell we want to empty 
    // could hold the value in the cell we want to empty
    public boolean rowCheck(int xcoord, int ycoord)
    {
        int index = board[xcoord][ycoord].val - 1; 
        
        // determines column coordinates for the start of the box
        // that contains the cell we want to empty
        int ystart = ycoord / n;
        ystart *= n;
        
        // iterate through cells in the row 
        for (int i = 0; i < len; i++)
        {
            // for empty cells in row, checks if they are allowed to hold the value
            // of the cell we want to remove, based on their box and column boolean arrays
            // unless they are in same box as our cell, in which case box array is useless
            if (board[xcoord][i].empty == true &&
               (i-ystart >= 0 && i-ystart < n || board[xcoord][i].box[index] == true) && 
                board[xcoord][i].column[index] == true)
                    
                    // if so, don't allow our cell to be emptied
                    return false;
                
        }
        
        // otherwise allow our cell to be emptied
        return true;
    }
    
    // checks if cell can be removed based on cells in same column
    // operates the same way as rowCheck, but with columns and rows switched
    public boolean columnCheck(int xcoord, int ycoord)
    {
        int index = board[xcoord][ycoord].val - 1; 
        
        int xstart = xcoord / n;
        xstart *= n;
        
        for (int i = 0; i < len; i++)
        {
            if (board[i][ycoord].empty == true &&
               (i-xstart >= 0 && i-xstart < n  || board[i][ycoord].box[index] == true) && 
                board[i][ycoord].row[index] == true)     
                    return false;
                
        }
        
        return true;
        
    }
    
    // checks if cell can be removed based on cells in same box
    public boolean boxCheck(int xcoord, int ycoord)
    {
        int index = board[xcoord][ycoord].val - 1; 
        
        int xstart = xcoord / n;
        xstart *= n;
        
        int ystart = ycoord / n;
        ystart *= n;
        
        // iterates through cells in box
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                 // if a cell in the box is empty and allowed to contain the value in our cell
                 // based on row and column arrays, or only column array if it's in the same row,
                 // or only row if it's in the same column
                 if (board[xstart+i][ystart+j].empty == true &&
                    (xstart+i == xcoord || board[xstart+i][ystart+j].row[index] == true) &&
                    (ystart+j == ycoord || board[xstart+i][ystart+j].column[index] == true))
                        
                        // don't allow our cell to be emptied
                        return false;
            }
                
        }
        
        // allow our cell to be emptied
        return true;
        
    }
    
    // after a cell has been emptied, updates boolean arrays in surrounding cells
    public void updateBoard(int xcoord, int ycoord)
    {
        int index = board[xcoord][ycoord].val - 1;
        
        // determines starting coordinates of the box containing emptied cell
        int xstart = xcoord / n;
        xstart *= n;
        int ystart = ycoord / n;
        ystart *= n;
        
        // for each cell in the same box as emptied cell
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                // update box array to show that based on the box, it is now allowed
                // to contain the value of our emptied cell
                board[xstart+i][ystart+j].box[index] = true;
            }
        }
        
        // for each cell in the same row or column as emptied cell   
        for (int i = 0; i < len; i++)
        {
            // update row or column boolean array to show that based on row or column,
            // it is now allowed to contain the value of our emptied cell
            board[i][ycoord].column[index] = true;    
            board[xcoord][i].row[index] = true;    
        }
        
        // update count of cells removed
        emptycount++;
        
    }
    
    // print finished board
    public void printBoard()
    {
        if (n < 4)
        {
            // print top border 
            for (int j = 0; j < 2 * len + 2 * n + 1; j++)
                {
                    System.out.print("-");
                }
                System.out.print("\n");
            
            // iterate through rows      
            for (int i = 0; i < len; i++)
            {
                // print side border
                System.out.print("|");
                
                // iterate through columns 
                for (int j = 0; j < len; j++)
                {
                    int val = board[i][j].val;
                    
                    // if cell is empty print user-entered value if available, else space 
                    if (board[i][j].empty == true)
                    {
                        int user_val = board[i][j].user_val;
                        
                        if (user_val != 0)
                        {
                            System.out.print(" "+user_val); 
                        }
                        else
                            System.out.print("  ");
                    }
                    
                    // else print value of cell
                    else
                    {
                        System.out.print(" "+val);
                    }    
                    
                    // interior side border 
                    if (n - 1 == j % n)
                    {
                        System.out.print(" |");
                    }
                }
                System.out.print("\n");
                
                // print middle and bottom borders
                if (n - 1 == i % n)
                {
                    for (int j = 0; j < 2 * len + 2 * n + 1; j++)
                    {
                        System.out.print("-");
                    }
                    System.out.print("\n");
                }
            }
            System.out.print("\n");
        }
        else if (n < 10)
        {
            // print top border 
            for (int j = 0; j < 3 * len + 2 * n + 1; j++)
                {
                    System.out.print("-");
                }
                System.out.print("\n");
            
            // iterate through rows      
            for (int i = 0; i < len; i++)
            {
                // print side border
                System.out.print("|");
                
                // iterate through columns 
                for (int j = 0; j < len; j++)
                {
                    int val = board[i][j].val;
                    
                    // if cell is empty print user-entered value if available, else space 
                    if (board[i][j].empty == true)
                    {
                        int user_val = board[i][j].user_val;
                        
                        if (user_val != 0)
                        {
                            if (user_val > 9)
                                System.out.print(" "+user_val);
                            else
                                System.out.print("  "+user_val); 
                        }
                        else
                            System.out.print("   ");
                    }
                    
                    // else print value of cell
                    else
                    {
                        if (val > 9)
                            System.out.print(" "+val);
                        else
                            System.out.print("  "+val);
                    }    
                    
                    // interior side border 
                    if (n - 1 == j % n)
                    {
                        System.out.print(" |");
                    }
                }
                System.out.print("\n");
                
                // print middle and bottom borders
                if (n - 1 == i % n)
                {
                    for (int j = 0; j < 3 * len + 2 * n + 1; j++)
                    {
                        System.out.print("-");
                    }
                    System.out.print("\n");
                }
            }
            System.out.print("\n");
        }
        // n > 10
        else
        {
            // print top border 
            for (int j = 0; j < 4 * len + 2 * n + 1; j++)
                {
                    System.out.print("-");
                }
                System.out.print("\n");
            
            // iterate through rows      
            for (int i = 0; i < len; i++)
            {
                // print side border
                System.out.print("|");
                
                // iterate through columns 
                for (int j = 0; j < len; j++)
                {
                    int val = board[i][j].val;
                    
                    // if cell is empty print user-entered value if available, else space 
                    if (board[i][j].empty == true)
                    {
                        int user_val = board[i][j].user_val;
                        
                        if (user_val != 0)
                        {
                            if (user_val > 99)
                                System.out.print(" "+user_val);
                            else if (user_val > 9)
                                System.out.print("  "+user_val); 
                            else
                                System.out.print("   "+user_val);
                        }
                        else
                            System.out.print("    ");
                    }
                    
                    // else print value of cell
                    else
                    {
                        if (val > 99)
                            System.out.print(" "+val);
                        else if (val > 9)
                            System.out.print("  "+val);
                        else
                            System.out.print("   "+val);
                    }    
                    
                    // interior side border 
                    if (n - 1 == j % n)
                    {
                        System.out.print(" |");
                    }
                }
                System.out.print("\n");
                
                // print middle and bottom borders
                if (n - 1 == i % n)
                {
                    for (int j = 0; j < 4 * len + 2 * n + 1; j++)
                    {
                        System.out.print("-");
                    }
                    System.out.print("\n");
                }
            }
            System.out.print("\n");
        }
    } 
    
    public boolean checkBoard(int vals_added)
    {
        int added_total = vals_added;
        int added_correct = 0;
        
        // for each user value entered that equals real value, increment counter 
        for (int i = 0; i < len; i++)
        {
            for (int j = 0; j < len; j++)
            {
                if (board[i][j].val == board[i][j].user_val)
                    added_correct++;
            }
        }
        
        // if counter is equal to total empty cells, congratulate user and 
        // return true to cease prompting for new values 
        if (added_correct == emptycount)
        {
            System.out.println("Congratulations! You win!!!\nThis is CS50.\n");
            return true;
        }
        
        // else update user on progress 
        else
        {
            System.out.println("You have entered "+added_correct+" correctly out of "+added_total);
        }
        
        return false;
    }
}