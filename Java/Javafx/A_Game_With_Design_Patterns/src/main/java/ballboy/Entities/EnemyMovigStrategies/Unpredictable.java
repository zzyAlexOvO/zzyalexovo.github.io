package ballboy.Entities.EnemyMovigStrategies;

import ballboy.Entities.Entity;


public class Unpredictable implements MovingStrategy{
    private int loopCount = 0;
    @Override
    public void move(Entity A) {
        if (loopCount % 50 == 0) {
            double rand = Math.random();
            if (rand <= 0.25) {
                A.setXVel(-1);
            } else if (rand >= 0.25 && rand <=0.5){
                A.setXVel(1);
            }else if (rand >= 0.5 && rand <= 0.75){
                A.setYVel(-5);
            }else{
                A.setYVel(0);
                A.setXVel(0);
            }
        }
        if (loopCount >= 200){
            loopCount = 0;
        }else{
            loopCount++;
        }
    }
}
