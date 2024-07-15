package sudoku.computationlogic;

import sudoku.constants.GameState;
import sudoku.constants.Rows;
import sudoku.problemdomain.SudokuGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static sudoku.problemdomain.SudokuGame.GRID_BOUNDARY;

public class GameLogic {

    static int n = GRID_BOUNDARY;
    public static SudokuGame getNewGame(){
        return new SudokuGame(
                GameState.NEW,
                GameGenerator.getNewGameGrid()
        );
    }
    public static GameState checkForCompletion(int[][] grid){
        if(sudokuIsInvalid(grid)) return GameState.ACTIVE;
        if(tilesAreNotFilled(grid)) return GameState.ACTIVE;
        return GameState.COMPLETE;
    }
    private static boolean tilesAreNotFilled(int[][] grid){
        for(int x=0;x<n;x++){
            for(int y=0;y<n;y++){
                if(grid[x][y] == 0) return true;
            }
        }
        return false;
    }
    public static boolean sudokuIsInvalid(int[][] grid){
        if(rowsAreInvalid(grid)) return true;
        if(columnsAreInvalid(grid)) return true;
        return squaresAreInvalid(grid);
    }
    private static boolean rowsAreInvalid(int[][] grid){
        for(int y=0;y<n;y++){
            List<Integer> row = new ArrayList<>();
            for(int x=0;x<n;x++){
                row.add(grid[x][y]);
            }
            if(collectionHasRepeats(row)) return true;
        }
        return false;
    }
    private static boolean columnsAreInvalid(int[][] grid){
        for(int x=0;x<n;x++){
            List<Integer> row = new ArrayList<>();
            for(int y=0;y<n;y++){
                row.add(grid[x][y]);
            }
            if(collectionHasRepeats(row)) return true;
        }
        return false;
    }
    private static boolean squaresAreInvalid(int[][] grid){
        if(rowOfSquaresIsInvalid(Rows.TOP, grid)) return true;
        if(rowOfSquaresIsInvalid(Rows.MIDDLE, grid)) return true;
        return(rowOfSquaresIsInvalid(Rows.BOTTOM, grid));
    }
    private static boolean rowOfSquaresIsInvalid(Rows value, int[][] grid){
        switch(value){
            case TOP:
                if (squareIsInvalid(0,0,grid))return true;
                if (squareIsInvalid(0,3,grid))return true;
                return squareIsInvalid(0,6,grid);
            case MIDDLE:
                if (squareIsInvalid(3,0,grid))return true;
                if (squareIsInvalid(3,3,grid))return true;
                return squareIsInvalid(3,6,grid);
            case BOTTOM:
                if (squareIsInvalid(6,0,grid))return true;
                if (squareIsInvalid(6,3,grid))return true;
                return squareIsInvalid(6,6,grid);
            default:
                return false;
        }
    }
    private static boolean squareIsInvalid(int x, int y, int[][] grid){
        int yl = y+3;
        int xl = x+3;
        List<Integer> square = new ArrayList<>();
        while(y<yl){
            while(x<xl){
                square.add(grid[x][y]);
                x++;
            }
            x-=3;
            y++;
        }
        return collectionHasRepeats(square);
    }
    private static boolean collectionHasRepeats(List<Integer> l){
        for(int x=1;x<=n;x++)
            if(Collections.frequency(l,x)>1) return true;
        return false;
    }
}
