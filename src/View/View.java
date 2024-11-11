package View;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public abstract class View {
    protected AnchorPane pane;
    protected Scene scene;

    protected int height, width;

    public View(int height, int width) {
        this.height = height;
        this.width = width;

        pane = new AnchorPane();
        pane.setPrefSize(width, height);
        scene = new Scene(pane, width, height);
    }

    protected abstract void init(boolean isServer);

    public Scene getScene() {
        return scene;
    }

    public abstract View update();
}
