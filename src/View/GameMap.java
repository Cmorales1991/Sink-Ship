//package View;
//
//import javafx.scene.control.Label;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Rectangle;
//
//public class GameMap {
//
//    public static final int GRID_SIZE = 10;
//    public static final int CELL_SIZE = 30;
//    private AnchorPane pane;
//    private boolean isPlayer;
//    public boolean isHorizontal = true; // Standardplacering allstå horisonellt
//    private Rectangle[][] cells;
//
//    public GameMap(boolean isPlayer) {
//        this.isPlayer = isPlayer;
//        pane = new AnchorPane(); // en AnchorPane för mapen
//        cells = new Rectangle[GRID_SIZE][GRID_SIZE];
//        drawGrid();
//    }
//
//    private void drawGrid() {
//        for (int i = 0; i < GRID_SIZE; i++) {
//            for (int j = 0; j < GRID_SIZE; j++) {
//                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
//                cell.setFill(Color.LIGHTBLUE);
//                cell.setStroke(Color.BLACK);
//                cell.setLayoutX(i * CELL_SIZE);
//                cell.setLayoutY(j * CELL_SIZE);
//                pane.getChildren().add(cell);
//                addCoordinateLabels(pane);
//            }
//        }
//    }
//
//    private void addCoordinateLabels(AnchorPane pane) {
//        // bokstavsmarkeringar för y-axeln (A-J)
//        for (int i = 0; i < GRID_SIZE; i++) {
//            Label rowLabelLeft = new Label(String.valueOf((char) ('A' + i)));
//            rowLabelLeft.setLayoutX(-20); // Placering till vänster om spelbrädet
//            rowLabelLeft.setLayoutY(i * CELL_SIZE + CELL_SIZE / 4);
//
//            Label rowLabelRight = new Label(String.valueOf((char) ('A' + i)));
//            rowLabelRight.setLayoutX(GRID_SIZE * CELL_SIZE + 5); // placering höger om spelbrädet
//            rowLabelRight.setLayoutY(i * CELL_SIZE + CELL_SIZE / 4);
//            pane.getChildren().addAll(rowLabelLeft, rowLabelRight);
//        }
//        // siffermarkeringar för x-axeln (0-9)
//        for (int i = 0; i < GRID_SIZE; i++) {
//            Label colLabelTop = new Label(String.valueOf(i));
//            colLabelTop.setLayoutX(i * CELL_SIZE + CELL_SIZE / 3);
//            colLabelTop.setLayoutY(-20); // Placering ovanför spelbrädet
//
//            Label colLabelBottom = new Label(String.valueOf(i));
//            colLabelBottom.setLayoutX(i * CELL_SIZE + CELL_SIZE / 3);
//            colLabelBottom.setLayoutY(GRID_SIZE * CELL_SIZE + 5); // Placering under spelbrädet
//            pane.getChildren().addAll(colLabelTop, colLabelBottom);
//        }
//    }
//
//    // metod för att få in spellogiken och ändring visuellt
//    public void updateMap(int x, int y, String status) {
////        pane.getChildren().removeIf(node -> node instanceof ImageView &&
////                node.getLayoutX() == x * CELL_SIZE &&
////                node.getLayoutY() == y * CELL_SIZE);
////
////        if (status.equalsIgnoreCase("hit")) {
////            // Visa en bild för en träff
////            Image hitImage = new Image(getClass().getResource("/images/explosion.png").toExternalForm());
////            ImageView hitImageView = new ImageView(hitImage);
////            hitImageView.setFitWidth(CELL_SIZE);
////            hitImageView.setFitHeight(CELL_SIZE);
////            hitImageView.setLayoutX(x * CELL_SIZE);
////            hitImageView.setLayoutY(y * CELL_SIZE);
////            pane.getChildren().add(hitImageView);
////            return;
////        } else {
//        // Rensa eventuell befintlig cell vid dessa koordinater
//        pane.getChildren().removeIf(node ->
//                node instanceof Rectangle && node.getLayoutX() == x * CELL_SIZE && node.getLayoutY() == y * CELL_SIZE);
//
//        Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
//        cell.setLayoutX(x * CELL_SIZE);
//        cell.setLayoutY(y * CELL_SIZE);
//
//        switch (status.toLowerCase()) {
//            case "ship":
//                cell.setFill(Color.GRAY); // placering av skepp
//                break;
//            case "hit":
//                cell.setFill(Color.RED); // träff
//                break;
//            case "miss":
//                cell.setFill(Color.WHITE); //miss
//                break;
//            default:
//                cell.setFill(Color.LIGHTBLUE); // standardfärg
//        }
//        cell.setStroke(Color.BLACK);
//        pane.getChildren().add(cell);
//    }
//
//    public AnchorPane getGameMapPane() {
//        return pane;
//    }
//}

package View;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameMap {

    public static final int GRID_SIZE = 10;  // Storlek på spelbrädet
    public static final int CELL_SIZE = 30;  // Storlek på varje cell (rutnätscell)
    private AnchorPane pane;                 // Pane för att hålla alla celler
    private boolean isPlayer;                // Om spelaren kontrollerar kartan
    public boolean isHorizontal = true;      // Standard placering (horisontell för skepp)
    private Rectangle[][] cells;             // För att hålla cellerna i en tvådimensionell array

    // Konstruktor som tar om det är spelarens karta eller inte
    public GameMap(boolean isPlayer) {
        this.isPlayer = isPlayer;
        pane = new AnchorPane();             // Skapa en AnchorPane för att lägga till celler
        cells = new Rectangle[GRID_SIZE][GRID_SIZE];  // Initialisera cellarrayen
        drawGrid();  // Rita spelbrädet
    }

    // Rita griden som en 10x10 uppsättning av rektanglar
    private void drawGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setFill(Color.LIGHTBLUE);  // Standardfärg för celler
                cell.setStroke(Color.BLACK);    // Svart kantlinje för cellerna
                cell.setLayoutX(i * CELL_SIZE); // X-position för cellen
                cell.setLayoutY(j * CELL_SIZE); // Y-position för cellen
                pane.getChildren().add(cell);  // Lägg till cellen på brädet
            }
        }
        addCoordinateLabels(pane);  // Lägg till koordinatetiketter
    }

    // Lägg till koordinatetiketter (bokstäver för y-axeln och siffror för x-axeln)
    private void addCoordinateLabels(AnchorPane pane) {
        // Y-axel (bokstäver A-J)
        for (int i = 0; i < GRID_SIZE; i++) {
            Label rowLabelLeft = new Label(String.valueOf((char) ('A' + i)));
            rowLabelLeft.setLayoutX(-20);  // Placering till vänster om griden
            rowLabelLeft.setLayoutY(i * CELL_SIZE + CELL_SIZE / 4);

            Label rowLabelRight = new Label(String.valueOf((char) ('A' + i)));
            rowLabelRight.setLayoutX(GRID_SIZE * CELL_SIZE + 5);  // Placering höger om griden
            rowLabelRight.setLayoutY(i * CELL_SIZE + CELL_SIZE / 4);
            pane.getChildren().addAll(rowLabelLeft, rowLabelRight);
        }
        // X-axel (siffror 0-9)
        for (int i = 0; i < GRID_SIZE; i++) {
            Label colLabelTop = new Label(String.valueOf(i));
            colLabelTop.setLayoutX(i * CELL_SIZE + CELL_SIZE / 3);
            colLabelTop.setLayoutY(-20);  // Placering ovanför griden

            Label colLabelBottom = new Label(String.valueOf(i));
            colLabelBottom.setLayoutX(i * CELL_SIZE + CELL_SIZE / 3);
            colLabelBottom.setLayoutY(GRID_SIZE * CELL_SIZE + 5);  // Placering under griden
            pane.getChildren().addAll(colLabelTop, colLabelBottom);
        }
    }

    // Metod för att uppdatera kartan visuellt när ett skott träffar eller missar
//    public void updateMap(int x, int y, String status) {
//        // Rensa eventuell befintlig cell vid dessa koordinater
//        pane.getChildren().removeIf(node ->
//                node instanceof Rectangle && node.getLayoutX() == x * CELL_SIZE && node.getLayoutY() == y * CELL_SIZE);
//
//        // Skapa en ny rektangel baserat på den nya statusen
//        Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
//        cell.setLayoutX(x * CELL_SIZE);
//        cell.setLayoutY(y * CELL_SIZE);
//
//        switch (status.toLowerCase()) {
//            case "ship":
//                cell.setFill(Color.GRAY);  // Placera ett skepp (grå cell)
//                break;
//            case "hit":
//                cell.setFill(Color.RED);   // Färga röd vid träff
//                break;
//            case "miss":
//                cell.setFill(Color.WHITE); // Färga vit vid miss
//                break;
//            default:
//                cell.setFill(Color.LIGHTBLUE);  // Standardfärg
//        }
//
//        // Sätt en svart kantlinje runt cellen
//        cell.setStroke(Color.BLACK);
//
//        // Lägg till cellen i brädet
//        pane.getChildren().add(cell);
//    }

    // Getter för AnchorPane som innehåller kartan
    public AnchorPane getGameMapPane() {
        return pane;
    }
}
