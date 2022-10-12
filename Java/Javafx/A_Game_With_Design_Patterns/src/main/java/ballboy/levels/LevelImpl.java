package ballboy.levels;

import ballboy.Entities.*;
import org.junit.experimental.theories.Theories;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class LevelImpl implements Level {
    private List<Entity> entities;
    private int levelHeight;
    private int levelWidth;
    private double floorHeight;
    private Entity hero;
    private double startX;
    private double startY;
    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private double gravity;
    private double max_speed;
    private double accelerateRate;

    public LevelImpl(int levelHeight, int levelWidth, double floorHeight, Entity hero,double gravity,double max_speed,double accelerateRate) {
        this.levelHeight = levelHeight;
        this.levelWidth = levelWidth;
        this.floorHeight = floorHeight;
        this.hero = hero;
        this.startX = hero.getXPos();
        this.startY = hero.getYPos();
        this.entities = new ArrayList<>();
        this.gravity = gravity;
        this.max_speed = max_speed;
        this.accelerateRate = accelerateRate;
    }

    @Override
    public List<Entity> getEntities() {
        return this.entities;
    }

    @Override
    public double getLevelHeight() {
        return this.levelHeight;
    }

    @Override
    public double getLevelWidth() {
        return this.levelWidth;
    }

    @Override
    public void tick() {
        applyGravity();
        judgeInteraction();
        for (Entity i : entities) {
            i.think();
        }
        heroMove();
    }

    @Override
    public double getFloorHeight() {
        return this.floorHeight;
    }

    @Override
    public double getHeroX() {
        return this.hero.getXPos();
    }

    @Override
    public double getHeroY() {
        return this.hero.getYPos();
    }

    @Override
    public boolean boostHeight() {
        up = true;
        return true;
    }

    @Override
    public boolean dropHeight() {
        up = false;
        return true;
    }

    @Override
    public boolean moveLeft() {
        if (this.right){
            this.right = false;
            return false;
        }else {
            this.left = true;
            return true;
        }
    }

    @Override
    public boolean moveRight() {
        if (this.left){
            this.left = false;
            return false;
        }else {
            this.right = true;
            return true;
        }
    }

    @Override
    public boolean stopLeft() {
        this.left = false;
        return true;
    }

    @Override
    public boolean stopRight() {
        this.right = false;
        return true;
    }

    private void applyGravity() {
        for (Entity i : entities) {
            if (i.isBackground() || i.getClass() == Platform.class){
                continue;
            }
            System.out.println(i.getClass());
            if (i.getYPos() + i.getHeight() >= floorHeight && i.getYVel() >= 0) {
                if (i.getClass() == hero.getClass()) {
                    i.setYPos(floorHeight - i.getHeight());
                    i.setYVel(-i.getYVel());
                }else{
                    i.setYPos(floorHeight - i.getHeight());
                    i.setYVel(0);
                }
            } else {
                i.setYVel(i.getYVel() + gravity);
            }
        }
    }

    private void heroMove() {
        if (left) {
            if (hero.getXVel() <= -max_speed) {
            } else {
                hero.setXVel(hero.getXVel() - accelerateRate);
            }
        } else if (right){
            if (hero.getXVel() >= max_speed) {
            } else {
                hero.setXVel(hero.getXVel() + accelerateRate);
            }
        }else{
            hero.setXVel(0);
        }
        if(up){
            hero.setYVel(hero.getYVel() - (3.0/4.0)*accelerateRate);
        }
    }

    private List<Entity> getMovingEntities() {
        List<Entity> movingEntities = new ArrayList<>();
        for (Entity ent: entities) {
            if (ent == hero || ent.getClass() == Cloud.class) continue;
            if (ent.getXVel() != 0 || ent.getYVel() != 0) {
                movingEntities.add(ent);
            }
        }

        movingEntities.add(hero);
        return movingEntities;
    }

    public boolean intersects(Entity A, Entity B) {
        return (A.getXPos() < (B.getXPos() + B.getWidth())) &&
                (B.getXPos() < (A.getXPos() + A.getWidth())) &&
                (A.getYPos() < (B.getYPos() + B.getHeight())) &&
                (B.getYPos() < (A.getYPos() + A.getHeight()));
    }

    private boolean heroIntersectsWithEnemy(Entity A, Entity B){
        if ((A.getClass() == Hero.class && B.getClass() == Enemy.class)
        ||(A.getClass() == Enemy.class && B.getClass() == Hero.class)){
            hero.setYVel(0);
            hero.setXVel(0);
            hero.setXPos(startX);
            hero.setYPos(startY);
            return true;
        }else{
            return false;
        }
    }

    private boolean heroIntersectsWithFlag(Entity A, Entity B){
        if ((A.getClass() == Hero.class && B.getClass() == Flag.class)
                ||(A.getClass() == Flag.class && B.getClass() == Hero.class)){
            exit(0);
            return true;
        }else{
            return false;
        }
    }

    private void judgeInteraction(){
        for (Entity A : getMovingEntities()) {
            for (Entity B : entities) {
                if (B.getLayer() == Entity.Layer.BACKGROUND || A.equals(B)){
                    continue;
                }
                if (intersects(A,B)) {
                    if (heroIntersectsWithEnemy(A, B) ||
                            heroIntersectsWithFlag(A, B)) {
                        continue;
                    }

                    boolean fromLeft;
                    boolean fromTop;
                    double xOverlap;
                    double yOverlap;

                    if (A.getXPos() < B.getXPos()) {
                        xOverlap = Math.abs(A.getXPos() + A.getWidth() - B.getXPos());
                        fromLeft = true;
                    } else {
                        xOverlap = Math.abs(B.getXPos() + B.getWidth() - A.getXPos());
                        fromLeft = false;
                    }

                    if (A.getYPos() < B.getYPos()) {
                        yOverlap = Math.abs(A.getYPos() + A.getHeight() - B.getYPos());
                        fromTop = true;
                    } else {
                        yOverlap = Math.abs(B.getYPos() + B.getHeight() - A.getYPos());
                        fromTop = false;
                    }

                    if (xOverlap < yOverlap) {
                        if (fromLeft) {
                            A.setXPos(B.getXPos() - A.getWidth());
                        }

                        else {
                            A.setXPos(B.getXPos() + B.getWidth());
                        }

                        if (A != hero) {
                            A.setXVel(A.getXVel() * -1);
                        }

                    } else {
                        if (fromTop) {
                            A.setYPos(B.getYPos() - A.getHeight());
                            if (A == hero){
                                hero.setYVel(-hero.getYVel());
                            }
                        }

                        else {
                            A.setYPos(B.getYPos() + B.getHeight());
                        }

                    }
                }
            }
            if (A.getXPos() <=0){
                A.setXPos(0);
            }else if (A.getXPos() + A.getWidth()>=levelWidth){
                A.setXPos(levelWidth - A.getWidth());
            }
            if (A.getYPos()<=0){
                A.setYPos(0);
            }
        }
    }
}
