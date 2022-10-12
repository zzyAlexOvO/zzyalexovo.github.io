package ballboy.model.observer;

import ballboy.model.Level;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TotalOb implements Observer {
    private final Text text;
    private Map<String, Integer> scores;
    private String header;
    private List<LevelOb> obs;
    public TotalOb(Text text,List<LevelOb> obs){
        this.text = text;
        this.header = text.getText();
        this.obs = obs;
        scores = new HashMap<>();
    }
    private TotalOb(Text text,List<LevelOb> obs,String header, Map<String,Integer> scores){
        this.text = text;
        this.header = header;
        this.obs = obs;
        for (LevelOb ob:obs){
            ob.attach(this);
        }
        this.scores = new HashMap<>(scores);
    }
    @Override
    public void update(){
        for (String i:scores.keySet()) {
            int score = 0;
            for (LevelOb ob : obs) {
                score += ob.getScores().get(i);
            }
            this.scores.put(i,score);
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(header);
        for(String i:scores.keySet()){
            buffer.append(i);
            buffer.append(": ");
            buffer.append(scores.get(i));
            buffer.append("; ");
        }
        this.text.setText(buffer.toString());
    }

    @Override
    public void addColour(String colour){
        scores.put(colour,0);
    }

    public TotalOb copy(List<LevelOb> obs) {
        return new TotalOb(text,obs,header,scores);
    }
}
