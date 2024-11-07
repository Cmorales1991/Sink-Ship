package View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import Controller.Controller;

public abstract class View
{
    protected AnchorPane pane;
    protected Scene scene;
    protected Stage stage;

    protected int height, width;

    protected Controller controller;

    public View(int height, int width)
    {
        this.height = height;
        this.width = width;

        pane = new AnchorPane();
        pane.setPrefSize(width, height);
        scene = new Scene(pane, width, height);

        stage = new Stage();
        stage.setScene(scene);
    }

    protected abstract void init();

    public abstract View update();

    public Stage getStage()
    {
        return stage;
    }
}
