package ballboy.model.entities.behaviour;

import ballboy.model.Level;
import ballboy.model.entities.DynamicEntity;

public class SquareCatBehaviourStrategy implements BehaviourStrategy{
    private final Level level;
    private final double height;
    private final double width;
    private int loopCnt = 0;

    public SquareCatBehaviourStrategy(Level level){
        this.level = level;
        this.height = level.getHeroHeight() + 60;
        this.width = level.getHeroWidth() + 60;
    }

    private SquareCatBehaviourStrategy(Level level,int loopCnt){
        this.level = level;
        this.height = level.getHeroHeight() + 60;
        this.width = level.getHeroWidth() + 60;
        this.loopCnt = loopCnt;
    }
    @Override
    public void behave(
            DynamicEntity cat,
            double frameDurationMilli) {
        if (loopCnt < height){
            cat.setPosition(cat.getPosition().setX(level.getHeroX()-40));
            cat.setPosition(cat.getPosition().setY(level.getHeroY()-40+loopCnt));
        }else if(loopCnt >= height && loopCnt < width + height){
            cat.setPosition(cat.getPosition().setX(level.getHeroX()-40 + loopCnt - height));
            cat.setPosition(cat.getPosition().setY(level.getHeroY() + level.getHeroHeight() + 20));
        }else if(loopCnt >= width + height && loopCnt < 2*height + width){
            cat.setPosition(cat.getPosition().setX(level.getHeroX() + level.getHeroWidth() +20));
            cat.setPosition(cat.getPosition().setY(level.getHeroY() + level.getHeroHeight() + 20 - loopCnt + width + height));
        }else if(loopCnt >= 2*height + width && loopCnt < 2*(width+height)){
            cat.setPosition(cat.getPosition().setX(level.getHeroX() + level.getHeroWidth() +20 - loopCnt + 2* height + width));
            cat.setPosition(cat.getPosition().setY(level.getHeroY()-40));
        }else{
            loopCnt = 0;
        }
        loopCnt++;
        cat.setVelocity(cat.getVelocity().setY(0));
    }

    @Override
    public BehaviourStrategy copy(Level level) {
        return new SquareCatBehaviourStrategy(level,loopCnt);
    }
}
