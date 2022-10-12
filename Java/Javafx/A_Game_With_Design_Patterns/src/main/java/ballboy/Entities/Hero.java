package ballboy.Entities;

import java.util.ArrayList;

public class Hero extends NormalEntity{
    public Hero(double x, double y, double height, double width, Layer layer, ArrayList<String> imagePath) {
        super(x, y, height, width, layer, imagePath);
    }
    public Hero(double x, double y, double height, double width, Layer layer, String imagePath) {
        super(x, y, height, width, layer, imagePath);
    }
}
