import java.util.Scanner;

public class GenerateSudoku
{
    // command line args (int n, char difficulty (e, m, h) ) 
    public static void main(String[] args)
    {
        //Instructions
        System.out.print("\n*****CS50 SUDOKU GAME*****\n\nInstructions:\n    To play Sudoku, run \"java GenerateSudoku\" in the command line with two command line arguments. The first is an integer, 2-20, which determines the size of the board. The second is a letter, e, m, or h, which determines the difficulty (easy, medium, or hard).\n\nGameplay:\n To fill in a cell, enter the row and column of the empty cell you wish to fill, according to the prompts. Then enter the value you wish to enter into the cell. You will then be asked whether you would like to check your answers. Doing so will return how many of your entered values are correct. Repeat this process until you have finished the board.\n    If you would like to change a value you've already entered into a cell, just enter the row, column, and new value into the prompts as you would for an empty cell. Alternatively, you can enter 0 to make the cell appear blank again.\n    When you think you have solved the puzzle, choose to check your answers at the prompt to see if you are correct. Have fun playing!\n");
        
        // ensure appropriate number of command line arguments
        if (args.length != 2)
        {    
            System.out.println("Please use two command-line arguments: GenerateSudoku (2-20) (e, m, or h)");
            System.exit(0);
        }
        
        // first command line argument: n
        int n = Integer.parseInt(args[0]);
        if (n < 2 || n > 20)
        {
            System.out.println("Please choose a size between 2 and 20");
            System.exit(0);
        }
        int len = n * n;
        
        // second command line argument: difficulty
        char difficulty = args[1].charAt(0);
        
        // create solved Sudoku board
        SudokuBoard NewBoard = new SudokuBoard(n);
        
        // different numbers of cells removed for different difficulties
        int max = 0;
        if (difficulty == 'h')
        {
            max = (int) (len * len * (5.0 / (n + 3)) );
        }
        else if (difficulty == 'm')
        {
            max = (int) (len * len * (0.2 + 1.0 / n) );
        }
        else if (difficulty == 'e')
        {
            max = (int) (len * len * (0.1 + 1.0 / n ) );
        }
        else
        {
            System.out.println("Difficulty must be written as e, m, or h");
            System.exit(0);
        }
        
        // determine elimination order of cells
        int[] elimination_order = NewBoard.randomArray(len * len);   
        
        // attempt to empty each cell  
        for (int i = 0; i < max; i++)
        {
            int xcoord = (elimination_order[i] - 1) % len;
            int ycoord = (elimination_order[i] - 1) / len;
            
            // if cell passes cell check (no other value can be in that cell)
            if (NewBoard.cellCheck(xcoord, ycoord))
            {
                // empty it, update the surrounding cells' boolean arrays
                NewBoard.board[xcoord][ycoord].emptyCell();
                NewBoard.updateBoard(xcoord, ycoord);
            }
            
            // otherwise, if it passes row check (no other cell in that row
            // can contain same value as the cell we want to empty), empty, update
            else if (NewBoard.rowCheck(xcoord, ycoord))
            {
                NewBoard.board[xcoord][ycoord].emptyCell();
                NewBoard.updateBoard(xcoord, ycoord);
            }
            
            // otherwise do same with column check (same as row check with columns)
            else if (NewBoard.columnCheck(xcoord, ycoord))
            {
                NewBoard.board[xcoord][ycoord].emptyCell();
                NewBoard.updateBoard(xcoord, ycoord);
            } 
            
            // otherwise do same with box check (last b/c most computationally expensive)
            else if (NewBoard.boxCheck(xcoord, ycoord))
            {
                NewBoard.board[xcoord][ycoord].emptyCell();
                NewBoard.updateBoard(xcoord, ycoord);
            }   
        }
        
        // print finished board
        System.out.print("\n");
        NewBoard.printBoard();
        
        // prepare for user input
        int vals_added = 0;
        boolean finished = false;
        Scanner inputreader = new Scanner(System.in);
        
        // keep playing until user receives congratulations for winning
        while (!finished)
        {
            // prompt user for row 
            System.out.println("Enter row: ");
            int xcoord = -1;
            do
            {
                // if user entered an integer
                if (inputreader.hasNextInt())
                {
                    // record it 
                    xcoord = inputreader.nextInt() - 1;
                    if (xcoord < 0)
                    {
                        System.out.println("row value too small");
                    }
                    else if (xcoord >= len)
                    {
                        System.out.println("row value too big");
                    }
                }
                
                // else prompt user until she gives an int
                else
                {
                    inputreader.next();
                    System.out.println("please enter a number");
                }
            }
            while (xcoord < 0 || xcoord >= len);
            
            // prompt user for column
            System.out.println("Enter column: ");
            int ycoord = -1;
            do
            {
                if (inputreader.hasNextInt())
                {
                    ycoord = inputreader.nextInt() - 1;
                    if (ycoord < 0)
                    {
                        System.out.println("column value too small");
                    }
                    else if (ycoord >= len)
                    {
                        System.out.println("column value too big");
                    }
                }
                else
                {
                    inputreader.next();
                    System.out.println("please enter a number");
                }
            }
            while (ycoord < 0 || ycoord >= len);

            // make sure cell is user-editable
            if (NewBoard.board[xcoord][ycoord].empty == false)
            {
                 System.out.print("This cell is full! Try a new one\n\n");
            }
            else
            {
                // prompt user for value
                System.out.println("Enter value: ");
                int user_val = -1;
                
                do
                {      
                    // if user has provided an integer
                    if (inputreader.hasNextInt())
                    {
                        // note initial user value and new user value
                        int prev_user_val = NewBoard.board[xcoord][ycoord].user_val;
                        user_val = inputreader.nextInt();
                        
                        // if integer isn't valid, prompt for valid integer
                        if (user_val < 0)
                        {
                            System.out.println("value too small");
                        }
                    
                        else if (user_val > len)
                        {
                            System.out.println("value too big");
                        }
                        
                        // if integer passes validity checks
                        else
                        {
                            // record new user value into board 
                            NewBoard.board[xcoord][ycoord].user_val = user_val;
                        
                            // if blank cell has been filled, increment values added
                            // if filled cell has been blanked, decrement values added
                            if (prev_user_val == 0 && user_val > 0)
                                vals_added++;
                            else if (prev_user_val > 0 && user_val == 0)
                                vals_added--;
                            }
                    }
                    
                    // else prompt user until she gives an int 
                    else
                    {
                        inputreader.next();
                        System.out.println("please enter a number");
                    }
                }
                while (user_val < 0 || user_val > len);
                System.out.print("\n");
            }
            
            // print user's updated board
            NewBoard.printBoard();
            
            // give user option to check their board after every update     
            System.out.println("Check? y/n");
            String input = inputreader.next();
            char c = input.charAt(0);
            if (c == 'y')
                finished = NewBoard.checkBoard(vals_added);
        }
        inputreader.close();     
    }
}

