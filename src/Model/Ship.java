package Model;

import javafx.scene.image.Image;

public class Ship {
    private String name;
    private int size;
    private boolean horizontal;
    private Image image;

    public Ship(String name, int size, boolean isHorizontal, String imagePath) {
        this.name = name;
        this.size = size;
        this.horizontal = isHorizontal; //om den ska placeras veritkalt eller horizontellt
        this.image = new Image(getClass().getResourceAsStream(imagePath));
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public int getSize() {
        return size;
    }

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
