package ballboy.Entities.EnemyMovigStrategies;

import ballboy.Entities.Entity;

public class keepRight implements MovingStrategy{
    @Override
    public void move(Entity A) {
        A.setXVel(+1);
    }
}
