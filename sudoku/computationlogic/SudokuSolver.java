package sudoku.computationlogic;

import sudoku.problemdomain.Coordinates;
import static sudoku.problemdomain.SudokuGame.GRID_BOUNDARY;

public class SudokuSolver {
    static int n = GRID_BOUNDARY;
    public static boolean puzzleIsSolvable(int[][] puzzle){
        Coordinates[] emptyCells = typeWriterEnumerate(puzzle);
        int index = 0;
        int input = 1;
        while(index<10){
            Coordinates current = emptyCells[index];
            input = 1;
            while(input<40){
                puzzle[current.getX()][current.getY()] = input;
                if(GameLogic.sudokuIsInvalid(puzzle)){
                    if(index == 0 && input == n)
                        return false;
                    else if(input == n)
                        index--;
                    input++;
                }
                else{
                    index++;
                    if(index==39)return true;
                    input=10;
                }
            }
        }
        return false;
    }
    private static Coordinates[] typeWriterEnumerate(int[][] puzzle){
        Coordinates[] emptyCells = new Coordinates[40];
        int iterator = 0;
        for(int x=0;x<n;x++){
            for(int y=0;y<n;y++){
                if(puzzle[y][x]==0){
                    emptyCells[iterator] = new Coordinates(x,y);
                    if(iterator == 39)
                        return emptyCells;
                    iterator++;
                }
            }
        }
        return emptyCells;
    }
}
