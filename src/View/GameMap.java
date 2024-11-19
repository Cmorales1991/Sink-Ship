package View;

import Model.Coordinate;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
        cells = new Rectangle[GRID_SIZE][GRID_SIZE];
        drawGrid();
        pane = new AnchorPane(); // en AnchorPane för mapen
        addCoordinateLabels(pane);

        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                pane.getChildren().add(cells[x][y]);
            }
        }
    }

    private void drawGrid() {
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setFill(Color.LIGHTBLUE);
                cell.setStroke(Color.BLACK);
                cell.setLayoutX(x * CELL_SIZE);
                cell.setLayoutY(y * CELL_SIZE);

                cells[x][y] = cell; // Save cell
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

    public void updateMap(int x, int y, String status) {
        Platform.runLater(() -> {
            switch (status.toLowerCase()) {
                case "p":
                    cells[x][y].setFill(Color.GREEN); // place ship
                    break;
                case "h":
                    cells[x][y].setFill(Color.RED); // hit
                    break;
                case "s":
                    cells[x][y].setFill(Color.YELLOW); // sink ship
                    break;
                case "m":
                    cells[x][y].setFill(Color.WHITE); // miss
                    break;
                default:
                    cells[x][y].setFill(Color.LIGHTBLUE); // empty
                    break;
            }
        });
    }

    public AnchorPane getGameMapPane() {
        return pane;
    }
}