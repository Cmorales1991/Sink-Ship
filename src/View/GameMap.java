package View;

import Model.Ship;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class GameMap {

    public static final int GRID_SIZE = 10;
    public static final int CELL_SIZE = 30;
    private AnchorPane pane;
    private boolean isPlayer;
    public boolean isHorizontal = true; // Standardplacering allstå horisonellt
    private Rectangle[][] cells;

    public GameMap(boolean isPlayer) {
        this.isPlayer = isPlayer;
        pane = new AnchorPane(); // en AnchorPane för mapen
        cells = new Rectangle[GRID_SIZE][GRID_SIZE];
        drawGrid();
    }

    private void drawGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setFill(Color.LIGHTBLUE);
                cell.setStroke(Color.BLACK);
                cell.setLayoutX(i * CELL_SIZE);
                cell.setLayoutY(j * CELL_SIZE);
                pane.getChildren().add(cell);
                addCoordinateLabels(pane);
            }
        }
    }

    private void addCoordinateLabels(AnchorPane pane) {
        // bokstavsmarkeringar för y-axeln (A-J)
        for (int i = 0; i < GRID_SIZE; i++) {
            Label rowLabelLeft = new Label(String.valueOf((char) ('A' + i)));
            rowLabelLeft.setLayoutX(-20); // Placering till vänster om spelbrädet
            rowLabelLeft.setLayoutY(i * CELL_SIZE + CELL_SIZE / 4);

            Label rowLabelRight = new Label(String.valueOf((char) ('A' + i)));
            rowLabelRight.setLayoutX(GRID_SIZE * CELL_SIZE + 5); // placering höger om spelbrädet
            rowLabelRight.setLayoutY(i * CELL_SIZE + CELL_SIZE / 4);
            pane.getChildren().addAll(rowLabelLeft, rowLabelRight);
        }
        // siffermarkeringar för x-axeln (0-9)
        for (int i = 0; i < GRID_SIZE; i++) {
            Label colLabelTop = new Label(String.valueOf(i));
            colLabelTop.setLayoutX(i * CELL_SIZE + CELL_SIZE / 3);
            colLabelTop.setLayoutY(-20); // Placering ovanför spelbrädet

            Label colLabelBottom = new Label(String.valueOf(i));
            colLabelBottom.setLayoutX(i * CELL_SIZE + CELL_SIZE / 3);
            colLabelBottom.setLayoutY(GRID_SIZE * CELL_SIZE + 5); // Placering under spelbrädet
            pane.getChildren().addAll(colLabelTop, colLabelBottom);
        }
    }

   // metod för att få in spellogiken och ändring visuellt
    public void updateMap(int x, int y, String status) {

        Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
        cell.setLayoutX(x * CELL_SIZE);
        cell.setLayoutY(y * CELL_SIZE);

        switch (status.toLowerCase()) {
            case "ship":
                cell.setFill(Color.GRAY); // placering av skepp
                break;
            case "hit":
                cell.setFill(Color.RED); // Träff
                break;
            case "miss":
                cell.setFill(Color.WHITE); // Miss
                break;
            default:
                cell.setFill(Color.LIGHTBLUE); // Standardfärg
        }
        cell.setStroke(Color.BLACK);
        pane.getChildren().add(cell);
    }

    public AnchorPane getGameMapPane() {
        return pane;
    }
}