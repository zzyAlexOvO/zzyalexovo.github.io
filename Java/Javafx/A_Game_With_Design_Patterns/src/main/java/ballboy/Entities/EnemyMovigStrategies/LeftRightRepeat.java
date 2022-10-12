package ballboy.Entities.EnemyMovigStrategies;

import ballboy.Entities.Entity;

public class LeftRightRepeat implements MovingStrategy{
    private int loopCount =0;
    @Override
    public void move(Entity A) {
        if (loopCount <= 100) {
            A.setXVel(-1);
        }else{
            A.setXVel(+1);
        }
        if (loopCount >= 200){
            loopCount = 0;
        }else{
            loopCount ++;
        }
    }
}
