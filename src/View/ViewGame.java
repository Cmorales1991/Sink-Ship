package View;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static View.GameMap.CELL_SIZE;

public class ViewGame extends View
{
    private GameMap serverMap, clientMap;
    private AnchorPane serverPane, clientPane;
    private Label serverLabel, clientLabel, statusLabel;
    private boolean isServer;

    public ViewGame(int height, int width, boolean isServer) {
        super(height, width);
        this.isServer = isServer;
        init(isServer);  // Initiera spelet
    }

    @Override
    protected void init(boolean isServer)
    {
        // Skapa spelbräden för server och klient
        serverMap = new GameMap(true);
        clientMap = new GameMap(false);

        // Hämta spelplanerna från GameMap
        serverPane = serverMap.getGameMapPane();
        clientPane = clientMap.getGameMapPane();

        // Positionera spelplanerna på skärmen
        serverPane.setLayoutX(50);
        serverPane.setLayoutY(50);
        clientPane.setLayoutX(GameMap.GRID_SIZE * CELL_SIZE + 150);
        clientPane.setLayoutY(50);

        // Skapa och positionera etiketter för spelplanerna
        serverLabel = new Label("Serverns spelplan");
        serverLabel.setLayoutX(150);
        serverLabel.setLayoutY(10);
        serverLabel.setTextFill(Color.RED);

        clientLabel = new Label("Klientens spelplan");
        clientLabel.setLayoutX(GameMap.GRID_SIZE * CELL_SIZE + 250);
        clientLabel.setLayoutY(10);
        clientLabel.setTextFill(Color.GREEN);

        // Statusetikett för att visa spelets tillstånd
        statusLabel = new Label("Placera dina skepp!");
        statusLabel.setLayoutX(30);
        statusLabel.setLayoutY(GameMap.GRID_SIZE * CELL_SIZE + 110);

        // Lägg till alla element på skärmen
        pane.getChildren().addAll(serverPane, clientPane, serverLabel, clientLabel, statusLabel);
    }

    // Uppdatera spelbrädet med statusen för ett skott på specifika koordinater
    public void updateMap(int x, int y, String status, boolean isServer) {
        // Hämta rätt spelbräde baserat på om det är klientens eller serverns bräde
        AnchorPane gameMap = (this.isServer? clientMap : serverMap).getGameMapPane();

        // Rensa eventuell befintlig cell vid dessa koordinater
        gameMap.getChildren().removeIf(node ->
                node instanceof Rectangle && node.getLayoutX() == x * CELL_SIZE && node.getLayoutY() == y * CELL_SIZE);

        // Skapa en ny rektangel baserat på den nya statusen
        Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
        cell.setLayoutX(x * CELL_SIZE);
        cell.setLayoutY(y * CELL_SIZE);

        switch (status.toLowerCase()) {
            case "ship":
                cell.setFill(Color.GRAY);  // Placera ett skepp (grå cell)
                break;
            case "hit":
                cell.setFill(Color.RED);   // Färga röd vid träff
                break;
            case "miss":
                cell.setFill(Color.WHITE); // Färga vit vid miss
                break;
            default:
                cell.setFill(Color.LIGHTBLUE);  // Standardfärg
        }

        // Sätt en svart kantlinje runt cellen
        cell.setStroke(Color.BLACK);

        // Lägg till cellen i spelbrädet
        gameMap.getChildren().add(cell);
    }



    // Hjälpmetod för att hitta rätt Rectangle (cell) baserat på koordinaterna
    private Rectangle getRectangleFromAnchorPane(AnchorPane pane, int x, int y) {
        for (Node node : pane.getChildren()) {
            if (node instanceof Rectangle) {
                Rectangle rect = (Rectangle) node;
                // Kontrollera om rektangelns koordinater matchar
                if (AnchorPane.getLeftAnchor(rect) == x * CELL_SIZE && AnchorPane.getTopAnchor(rect) == y * CELL_SIZE) {
                    return rect;  // Returnera rätt cell
                }
            }
        }
        return null;  // Returnera null om ingen cell hittades
    }

    // Metod som kan användas för att uppdatera spelets status (om nödvändigt)
    @Override
    public View update()
    {
        return this;  // Returnera aktuell vy (kan implementeras för ytterligare funktionalitet)
    }
}
