sudoku-generator
================

To use this program run GenerateSudoku from the command line followed by two arguments:

- n: an integer between 2 and 20. The edge length of the board will be n^2. Thus n=3 would produce the standard 9x9 Sudoku board
- difficulty: either "e" for easy, "m" for medium, or "h" for hard. Determines the difficulty of the Sudoku board

A Sudoku board will appear in the command line is ASCII. From here, there is an interactive game to fill in the Sudoku board. To play, type in the row and column (1-indexed) of the cell you would like to fill in according to the promts. Then enter the value you would like to place in that cell. You will then be asked whether you would like to check your answers. Doing so will return how many of your entered values are correct. Repeat this process until you have finished the board. 
If you would like to change a value you've already entered into a cell, just enter the row, column, and new value into the prompts as you would for an empty cell. Alternatively, you can enter 0 to make the cell appear blank again. When you think you have solved the puzzle, choose to check your answers at the prompt to see if you are correct. Have fun playing!
