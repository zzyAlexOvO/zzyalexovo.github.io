package ballboy.model;

import ballboy.model.memento.CareTaker;
import ballboy.model.memento.Memento;
import ballboy.model.observer.LevelOb;
import ballboy.model.observer.Observer;
import ballboy.model.observer.TotalOb;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the GameEngine interface.
 * This provides a common interface for the entire game.
 */
public class GameEngineImpl implements GameEngine {
    private List<Level> levels;
    private Level currentLevel;
    private int levelIndex;
    private CareTaker saver;

    private TotalOb totalScoreOb;

    public GameEngineImpl(List<Level> levels, int levelIndex, TotalOb ob) {
        this.levels = levels;
        this.levelIndex = levelIndex;
        this.totalScoreOb = ob;
        this.saver = new CareTaker();
        startLevel();
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void startLevel() {
        // TODO: Handle when multiple levels has been implemented
        this.currentLevel = levels.get(levelIndex);
        currentLevel.notifies();
        return;
    }

    public boolean boostHeight() {
        return currentLevel.boostHeight();
    }

    public boolean dropHeight() {
        return currentLevel.dropHeight();
    }

    public boolean moveLeft() {
        return currentLevel.moveLeft();
    }

    public boolean moveRight() {
        return currentLevel.moveRight();
    }

    public void safeMode(){
        this.currentLevel.safeMode();
    }

    public void tick() {
        if(currentLevel.isWin()){
            levelIndex++;
            if(levelIndex >= levels.size()){
                System.exit(0);
            }else {
                startLevel();
            }
        }
        currentLevel.update();
    }

    public void save(){
        this.saver.save(new Memento(this.levels,levelIndex));
    }

    public void load(){
        Memento mem = this.saver.load();
        this.levels = mem.getLevels();
        this.levelIndex = mem.getLevelIdx();
        currentLevel = levels.get(levelIndex);
        List<LevelOb> levelObs = new ArrayList<>();
        for (Level level:levels){
            for (LevelOb levelob:level.getObs()){
                levelObs.add(levelob);
            }
        }
        this.totalScoreOb = this.totalScoreOb.copy(levelObs);
        currentLevel.notifies();
    }
}