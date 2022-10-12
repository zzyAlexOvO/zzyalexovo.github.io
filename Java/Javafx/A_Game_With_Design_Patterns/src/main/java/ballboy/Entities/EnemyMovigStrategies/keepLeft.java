package ballboy.Entities.EnemyMovigStrategies;

import ballboy.Entities.Entity;

public class keepLeft implements MovingStrategy{
    @Override
    public void move(Entity A) {
        A.setXVel(-1);
    }
}
