package ballboy.Factory;

import ballboy.Entities.Enemy;
import ballboy.Entities.EnemyMovigStrategies.*;
import ballboy.Entities.Entity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class EnemyFactory implements EntityFactory{

    @Override
    public Entity build(JSONObject enemy) {
        double x = (Double)((JSONObject)enemy).get("x");
        double y = (Double)((JSONObject)enemy).get("y");
        double height = (Double)((JSONObject)enemy).get("height");
        double width = (Double)((JSONObject)enemy).get("width");
        JSONArray images =(JSONArray)((JSONObject)enemy).get("images");
        ArrayList<String> imagePaths = new ArrayList<>();
        for(Object i: images){
            imagePaths.add(i.toString());
        }
        Entity obj = new Enemy(x,y,height,width, Entity.Layer.FOREGROUND,images, generateRandomStrategy());
        return obj;
    }

    private MovingStrategy generateRandomStrategy(){
        double rand = Math.random();
        if (rand <= 0.25){
            return new keepLeft();
        }else if(rand >= 0.25 && rand <=0.5){
            return new keepRight();
        }else if (rand >= 0.5 && rand <= 0.75){
            return new LeftRightRepeat();
        }else{
            return new Unpredictable();
        }
    }
}
