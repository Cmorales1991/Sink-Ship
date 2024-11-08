package View;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class ViewGame extends View
{
    private GameMap serverMap, clientMap;

    private AnchorPane serverPane, clientPane;

    private Label serverLabel, clientLabel, statusLabel;

    public ViewGame(int height, int width, boolean isServer) {
        super(height, width);
        if (isServer) {
            System.out.println("Startar spelet som Server");
        } else {
            System.out.println("Startar spelet som Klient");
        }
        init(isServer);
    }

    @Override
    protected void init(boolean isServer)
    {
        // spelbräda för server o klient
        serverMap = new GameMap(true);
        clientMap = new GameMap(false);

        // positionering spelplan för serverplan och client
        serverPane = serverMap.getGameMapPane();
        serverPane.setLayoutX(50);
        serverPane.setLayoutY(50);

        clientPane = clientMap.getGameMapPane();
        clientPane.setLayoutX(GameMap.GRID_SIZE * GameMap.CELL_SIZE + 150);
        clientPane.setLayoutY(50);

        // underrubrik för spelplanerna
        serverLabel = new Label("Serverns spelplan");
        serverLabel.setLayoutX(150);
        serverLabel.setLayoutY(10);
        serverLabel.setTextFill(Color.RED);

        clientLabel = new Label("Klientens spelplan");
        clientLabel.setLayoutX(GameMap.GRID_SIZE * GameMap.CELL_SIZE + 250);
        clientLabel.setLayoutY(10);
        clientLabel.setTextFill(Color.GREEN);

        statusLabel = new Label("Placera dina skepp!");
        statusLabel.setLayoutX(30);
        statusLabel.setLayoutY(GameMap.GRID_SIZE * GameMap.CELL_SIZE + 110);

        // växla knappen visas efter val av spelplan (server eller klient)
        if (isServer) {
            changeButton(serverPane);
        } else {
            changeButton(clientPane);
        }

        pane.getChildren().addAll(serverPane, clientPane, serverLabel, clientLabel, statusLabel);

    }
    // knapp för att växla på placering av skepp
    private void changeButton(AnchorPane pane) {
        Button toggleButton = new Button("Växla placering");
        toggleButton.setLayoutX(100);
        toggleButton.setLayoutY(GameMap.GRID_SIZE * GameMap.CELL_SIZE + 30);
        toggleButton.setOnAction(event -> {
            boolean isHorizontal = !serverMap.isHorizontal;
            toggleButton.setText(isHorizontal ? "Placera horisontellt" : "Placera vertikalt");
        });
        pane.getChildren().add(toggleButton);
    }

    @Override
    public View update()
    {
        return this;
    }
}
