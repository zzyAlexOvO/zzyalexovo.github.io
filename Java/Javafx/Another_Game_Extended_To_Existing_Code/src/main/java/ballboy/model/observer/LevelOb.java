package ballboy.model.observer;

import ballboy.model.Level;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

public class LevelOb implements Observer {
    private final Level level;
    private final Text text;
    private Map<String, Integer> scores;
    private String header;

    private Observer observer;
    public LevelOb(Level level, Text text){
        this.level = level;
        this.text = text;
        this.header = text.getText();
        scores = new HashMap<>();
    }
    private LevelOb(Level level, Text text, String header,Map<String,Integer> scores){
        this.level = level;
        this.text = text;
        this.header = header;
        this.scores = new HashMap<>(scores);
    }
    @Override
    public void update(){
        StringBuffer buffer = new StringBuffer();
        buffer.append(header);
        for(String i:scores.keySet()){
            scores.put(i,level.getScores().get(i));
            buffer.append(i);
            buffer.append(": ");
            buffer.append(scores.get(i));
            buffer.append("; ");
        }
        this.text.setText(buffer.toString());
        this.notifies();
    }

    @Override
    public void addColour(String colour){
        scores.put(colour,0);
    }

    public LevelOb copy(Level level) {
        LevelOb copy = new LevelOb(level,this.text,this.header,this.scores);
        return copy;
    }

    public void attach(Observer ob){
        this.observer = ob;
    }

    public void notifies(){
        this.observer.update();
    }

    public Map<String,Integer> getScores(){
        return this.scores;
    }
}
