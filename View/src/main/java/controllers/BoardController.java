package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import jdk.internal.org.objectweb.asm.tree.IntInsnNode;
import sudoku.FileSudokuBoardDao;
import sudoku.SudokuBoard;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

//import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static sudoku.SudokuBoardDaoFactory.getSudokuBoardDaoFactory;

public class BoardController {

    private MainController mainController;
    private SudokuBoard sudokuBoard;

    @FXML
    private GridPane gridPane;

    @FXML
    private Button checkButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button readButton;

    @FXML
    private Label fileLabel;

    @FXML
    private TextField fileField;


    @FXML
    public void initialize() {
    }


    public void prepare(MainController mainController) {
        setMainController(mainController);
        //SudokuBoard sudokuBoard = mainController.getSudokuBoard();
        this.sudokuBoard = mainController.getSudokuBoard();
        showBoard();
    }

    private void showBoard(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                TextField textField = new TextField(Integer.toString(sudokuBoard.get(i, j)));
                //TextField textField = new TextField();
                if (textField.getText().equals("0")) {
                    textField.setText("");
                }
                else {
                    textField.setEditable(false);
                }
                gridPane.add(textField, i, j);
            }
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void fillBoard(){
        List<Node> fields = gridPane.getChildren();
        TextField test;
        int row, col, val;
        for(Node i : fields) {
            test = (TextField) i;
            row = gridPane.getRowIndex(i);
            col = gridPane.getColumnIndex(i);
            try{
                val = Integer.parseInt(test.getText());
            }
            catch (NumberFormatException e){
                val = 0 ; // tutaj nie do konca fancy ale zawsze cos :D
                System.out.println("Bledny format danych");
            }
            this.sudokuBoard.set(row,col,val);
        }
    }

    @FXML
    public void onActionButton(){
        fillBoard();
        if(!sudokuBoard.checkBoard()) System.out.println("Podane sudoku nie jest prawidłowe");
        else System.out.println("Wygrales !");
    }

    @FXML
    public void saveOnAction(){
        fillBoard();
        try(FileSudokuBoardDao files = getSudokuBoardDaoFactory(fileField.getText())){
            files.write(this.sudokuBoard);
            this.sudokuBoard = files.read();
            showBoard();
        }
        catch (IOException e ){
            System.out.println("Nie znaleziono pliku123");
        }
        System.out.println(this.sudokuBoard.get(3,4));
    }

    @FXML
    public void readOnAction(){
        try(FileSudokuBoardDao files = getSudokuBoardDaoFactory("lol.bin")){
            this.sudokuBoard = files.read();
            showBoard();
        }
        catch (IOException e ){
            System.out.println("Nie znaleziono pliku123");
        }
    }
}
