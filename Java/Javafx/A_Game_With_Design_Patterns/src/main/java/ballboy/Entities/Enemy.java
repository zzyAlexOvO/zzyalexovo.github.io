package ballboy.Entities;

import ballboy.Entities.EnemyMovigStrategies.MovingStrategy;

import java.util.ArrayList;

public class Enemy extends NormalEntity{
    private MovingStrategy strategy;
    public Enemy(double x, double y, double height, double width, Layer layer, ArrayList<String> imagePath, MovingStrategy strategy) {
        super(x, y, height, width, layer, imagePath);
        this.strategy = strategy;
    }
    public Enemy(double x, double y, double height, double width, Layer layer, String imagePath, MovingStrategy strategy) {
        super(x, y, height, width, layer, imagePath);
        this.strategy = strategy;
    }

    @Override
    public void think(){
        strategy.move(this);
        this.move();
    }
}
