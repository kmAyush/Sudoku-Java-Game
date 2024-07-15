package sudoku.persistance;

import sudoku.problemdomain.IStorage;
import sudoku.problemdomain.SudokuGame;

import java.io.*;

public class LocalStorageImpl implements IStorage {
    private static File GAME_DATA = new File(System.getProperty("user.home"),"gamedata.txt");

    @Override
    public void updateGameData(SudokuGame game) throws IOException {
        try {
            FileOutputStream fos = new FileOutputStream(GAME_DATA);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(game);
            oos.close();
        } catch(IOException e){
            throw new IOException("Unable to access game data");
        }
    }

    @Override
    public SudokuGame getGameData() throws IOException {
        FileInputStream fin = new FileInputStream(GAME_DATA);
        ObjectInputStream oin = new ObjectInputStream(fin);
        try{
            SudokuGame gameState = (SudokuGame) oin.readObject();
            oin.close();
            return gameState;
        } catch(ClassNotFoundException e){
            throw new IOException("File not found");
        }
    }
}
