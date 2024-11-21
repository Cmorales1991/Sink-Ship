package View;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameMap {

    public static final int GRID_SIZE = 10;
    public static final int CELL_SIZE = 30;
    private final AnchorPane pane;
    private final Rectangle[][] cells;

    public GameMap() {
        cells = new Rectangle[GRID_SIZE][GRID_SIZE];
        drawGrid();
        pane = new AnchorPane(); // AnchorPane as map
        addCoordinateLabels(pane);

        // Create cells
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
        // Mark Y-axis with letters
        for (int i = 0; i < GRID_SIZE; i++) {
            Label rowLabelLeft = new Label(String.valueOf((char) ('A' + i)));
            rowLabelLeft.setLayoutX(-20); // Placering till vänster om spelbrädet
            rowLabelLeft.setLayoutY(i * CELL_SIZE + CELL_SIZE / 4);

            Label rowLabelRight = new Label(String.valueOf((char) ('A' + i)));
            rowLabelRight.setLayoutX(GRID_SIZE * CELL_SIZE + 5); // placering höger om spelbrädet
            rowLabelRight.setLayoutY(i * CELL_SIZE + CELL_SIZE / 4);
            pane.getChildren().addAll(rowLabelLeft, rowLabelRight);
        }
        // Mark X-axis with numbers
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
            switch (status.toLowerCase()) {
                case "p":
                    cells[x][y].setFill(Color.GREEN); // Ship placed
                    break;
                case "h":
                    cells[x][y].setFill(Color.RED); // Ship hit
                    break;
                case "s":
                    cells[x][y].setFill(Color.BLACK); // Ship sunk
                    break;
                case "m":
                    cells[x][y].setFill(Color.WHITE); // Miss
                    break;
                default:
                    cells[x][y].setFill(Color.LIGHTBLUE); // Empty/default
                    break;
            }
    }

    public AnchorPane getGameMapPane() {
        return pane;
    }
}