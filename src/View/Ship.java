package View;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private String name;
    private int size;
    private List<int[]> positions;
    //private Image image;


    public Ship(String name, int size, boolean isHorizontal) {
        this.name = name;
        this.size = size;
        // Ladda bild från imagePath
        //this.image = new Image(getClass().getResourceAsStream(imagePath));
        // Initialisera positionslistan
        this.positions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public List<int[]> getPositions() {
        return positions;
    }

//    public Image getImage() {
//        return image;
//    }

}
