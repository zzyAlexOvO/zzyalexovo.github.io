package ballboy.model.memento;

import ballboy.model.Level;

import java.util.ArrayList;
import java.util.List;

public class Memento {
    private final List<Level> levels = new ArrayList<>();
    private final int levelIdx;

    public Memento(List<Level> levels, int levelIndex){
        for (Level level: levels){
            this.levels.add(level.save());
        }
        this.levelIdx = levelIndex;
    }

    public List<Level> getLevels(){
        return levels;
    }

    public int getLevelIdx(){
        return this.levelIdx;
    }
}
