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
    private List<Ship> placedShips;

    public GameMap(boolean isPlayer) {
        this.isPlayer = isPlayer;
        this.placedShips = new ArrayList<>();
        pane = new AnchorPane(); // en AnchorPane för mapen
        changeButton(isPlayer);
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

    private void changeButton(boolean isPlayer) {
        // en knapp för att växla placering mellan horisontell och vertikal
        if (this.isPlayer) {
            Button toggleButton = new Button("Växla placering");
            toggleButton.setLayoutX(100);
            toggleButton.setLayoutY(GRID_SIZE * CELL_SIZE + 30);
            toggleButton.setOnAction(event -> {
                isHorizontal = !isHorizontal; // Växla placering mellan horiontell o vetrikal
            });
            pane.getChildren().add(toggleButton);
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

    public AnchorPane getGameMapPane() {
        return pane;
    }


}