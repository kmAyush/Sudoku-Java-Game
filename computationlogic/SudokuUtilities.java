package sudoku.computationlogic;

import sudoku.problemdomain.SudokuGame;

public class SudokuUtilities {
    public static void copySudokuArrayValues(int[][] old_ar, int[][] new_ar){
        for(int xi = 0; xi<SudokuGame.GRID_BOUNDARY;xi++){
            for(int yi = 0;yi<SudokuGame.GRID_BOUNDARY;yi++){
                new_ar[xi][yi] = old_ar[xi][yi];
            }
        }
    }
    public static int[][] copyToNewArray(int[][] ar){
        int n = SudokuGame.GRID_BOUNDARY;
        int [][] new_ar = new int[n][n];

        for(int x=0;x<n;x++){
            for(int y=0;y<n;y++){
                new_ar[x][y]= ar[x][y];
            }
        }
        return new_ar;
    }
}
