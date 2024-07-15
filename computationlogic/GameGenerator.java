package sudoku.computationlogic;

import javafx.scene.Parent;
import sudoku.problemdomain.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static sudoku.problemdomain.SudokuGame.GRID_BOUNDARY;
public class GameGenerator {
    static int n = GRID_BOUNDARY;
    
    public static int[][] getNewGameGrid(){
        return unsolveGame(getSolvedGame());
    }

    private static int[][] unsolveGame(int[][] solvedGame) {
        Random random = new Random(System.currentTimeMillis());
        boolean solvable = false;
        int[][] solvableArray = new int[n][n];
        while(!solvable){
            SudokuUtilities.copySudokuArrayValues(solvedGame, solvableArray);
            int index=0;
            while(index<40){
                int x = random.nextInt(n);
                int y = random.nextInt(n);

                if(solvableArray[x][y] != 0){
                    solvableArray[x][y] = 0;
                    index++;
                }
            }
            int[][] tobeSolved = new int[n][n];
            SudokuUtilities.copySudokuArrayValues(solvableArray, tobeSolved);
            solvable = SudokuSolver.puzzleIsSolvable(tobeSolved);
        }
        return solvableArray;
    }

    private static int[][] getSolvedGame(){
        Random random  =  new Random(System.currentTimeMillis());
        int[][] newGrid = new int[n][n];

        for(int v=1;v<n;v++){
            int allocations = 0;
            int interrupt = 0;
            List<Coordinates> allocTracker = new ArrayList<>();
            int attempts =0;
            while(allocations<n){
                if(interrupt>200){
                    allocTracker.forEach(coord ->{
                        newGrid[coord.getX()][coord.getY()] = 0;
                    });
                    interrupt = 0;
                    allocations = 0;
                    allocTracker.clear();
                    attempts++;

                    if(attempts > 500){
                        clearArray(newGrid);
                        attempts = 0;
                        v = 1;

                    }
                }
                int x = random.nextInt(n);
                int y = random.nextInt(n);
                if(newGrid[x][y]==0){
                    newGrid[x][y]=v;
                    if(GameLogic.sudokuIsInvalid(newGrid)){
                        newGrid[x][y] = 0;
                        interrupt++;
                    } else {
                        allocTracker.add(new Coordinates(x,y));
                        allocations++;
                    }
                }
            }
        }
        return newGrid;
    }
    private static void clearArray(int[][] newGrid){
        for(int x=0;x<n;x++){
            for(int y=0;y<n;y++){
                newGrid[x][y]=0;
            }
        }
    }
}
