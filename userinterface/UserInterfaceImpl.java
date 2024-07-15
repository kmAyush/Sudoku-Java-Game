package sudoku.userinterface;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import sudoku.constants.GameState;
import sudoku.problemdomain.Coordinates;
import sudoku.problemdomain.SudokuGame;
import java.util.HashMap;

public class UserInterfaceImpl implements IUserInterfaceContract.View, EventHandler<KeyEvent> {

    private Stage stage;
    private Group root;

    private HashMap<Coordinates, SudokuTextField> textFieldCoordinates;
    private IUserInterfaceContract.EventListener listener;
    private static final double WINDOW_Y = 732;
    private static final double WINDOW_X = 668;
    private static final double BOARD_PADDING = 50;
    private static final double BOARD_X_AND_Y = 576;

    private static final Color WINDOW_BACKGROUND_COLOR = Color.rgb(0, 150, 136);
    private static final Color BOARD_BACKGROUND_COLOR = Color.rgb(224,242,241);
    private static final String SUDOKU = "Sudoku";

    public UserInterfaceImpl(Stage stage) {
        this.stage = stage;
        this.root = new Group();
        this.textFieldCoordinates = new HashMap<>();
        initializeUserInterface();
    }

    private void initializeUserInterface(){
        drawBackground(root);
        drawTitle(root);
        drawSudokuBoard(root);
        drawTextFields(root);
        drawGridLines(root);
        stage.show();
    }


    private void drawBackground(Group root) {
        Scene scene = new Scene(root, WINDOW_X,WINDOW_Y);
        scene.setFill(WINDOW_BACKGROUND_COLOR);
        stage.setScene(scene);
    }

    private void drawTitle(Group root) {
        Text title = new Text(235,690, SUDOKU);
        title.setFill(Color.WHITE);
        Font titleFont = new Font(43);
        title.setFont(titleFont);
        root.getChildren().add(title);
    }

    private void drawSudokuBoard(Group root) {
        Rectangle board = new Rectangle();
        board.setX(BOARD_PADDING);
        board.setY(BOARD_PADDING);
        board.setWidth(BOARD_X_AND_Y);
        board.setHeight(BOARD_X_AND_Y);
        board.setFill(BOARD_BACKGROUND_COLOR);
        root.getChildren().addAll(board);
    }

    private void drawTextFields(Group root) {
        final int xOrigin = 50;
        final int yOrigin = 50;
        final int xAndYDelta = 64;

        for(int xi=0;xi<9;xi++){
            for(int yi=0;yi<9;yi++){
                int x = xOrigin + xi*xAndYDelta;
                int y = yOrigin + yi*xAndYDelta;
                SudokuTextField tile = new SudokuTextField(xi,yi);
                styleSudokuTile(tile,x,y);
                tile.setOnKeyPressed(this);
                textFieldCoordinates.put(new Coordinates(xi, yi), tile);
                root.getChildren().add(tile);
            }
        }
    }

    private void styleSudokuTile(SudokuTextField tile, double x, double y) {
        Font numFont = new Font(32);
        tile.setFont(numFont);
        tile.setAlignment(Pos.CENTER);
        tile.setLayoutX(x);
        tile.setLayoutY(y);
        tile.setPrefHeight(64);
        tile.setPrefWidth(64);


    }

    private void drawGridLines(Group root) {
        int XandY = 114;
        int index = 0;
        while (index < 8){
            int thickness = (index==2||index==5)? 3:2;
            Rectangle verticalLine = getLine( XandY + 64*index,
                    BOARD_PADDING, BOARD_X_AND_Y, thickness);
            Rectangle horizontalLine = getLine(BOARD_PADDING, XandY + 64*index,
                    thickness, BOARD_X_AND_Y);
            root.getChildren().addAll(
                    verticalLine, horizontalLine
            );
            index++;
        }
    }

    private Rectangle getLine(double x, double y, double height, double width){
        Rectangle line = new Rectangle();
        line.setX(x);
        line.setY(y);
        line.setHeight(height);
        line.setWidth(width);
        line.setFill(Color.BLACK);
        return line;
    }


    @Override
    public void handle(KeyEvent event) {
        if(event.getEventType() == KeyEvent.KEY_PRESSED){
            if(event.getText().matches("[0-9]")) {
                int value = Integer.parseInt(event.getText());
                handleInput(value, event.getSource());
            }
            else if(event.getCode() == KeyCode.BACK_SPACE)
                handleInput(0,event.getSource());
            else
                ((TextField) event.getSource()).setText("");
        }
        event.consume();
    }

    private void handleInput(int i, Object source) {
        listener.onSudokuInput(
                ((SudokuTextField) source).getX(),
                ((SudokuTextField) source).getY(),
                i
        );
    }

    @Override
    public void setListener(IUserInterfaceContract.EventListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateSquare(int x, int y, int input) {
        SudokuTextField tile = textFieldCoordinates.get(new Coordinates(x, y));
        String value = Integer.toString(input);
        if(value.equals("0")) value="";
        tile.textProperty().setValue(value);
    }

    @Override
    public void updateBoard(SudokuGame game) {
        for(int xi=0;xi<9;xi++){
            for(int yi=0;yi<9;yi++){
                TextField tile = textFieldCoordinates.get(new Coordinates(xi, yi));
                String value = Integer.toString(game.getCopyOfGridState()[xi][yi]);
                if(value.equals("0")) value="";
                tile.setText(value);
                if(game.getGameState() == GameState.NEW){
                    if(value.isEmpty()) {
                        tile.setStyle("-fx-opacity:1;");
                        tile.setDisable(false);
                    } else {
                        tile.setStyle("-fx-opacity:0.8;");
                        tile.setDisable(false);
                    }
                }
            }

        }
    }

    @Override
    public void showDialog(String message) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        dialog.showAndWait();

        if(dialog.getResult() == ButtonType.OK)
            listener.onDialogClick();
    }

    @Override
    public void showError(String message) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        dialog.showAndWait();
    }
}
