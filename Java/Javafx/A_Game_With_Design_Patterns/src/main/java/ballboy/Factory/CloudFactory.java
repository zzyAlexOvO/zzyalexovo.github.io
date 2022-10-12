package ballboy.Factory;

import ballboy.Entities.Cloud;
import ballboy.Entities.Enemy;
import ballboy.Entities.Entity;
import ballboy.Entities.NormalEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class CloudFactory implements EntityFactory{
    @Override
    public Entity build(JSONObject cloud) {
        double x = (Double)((JSONObject)cloud).get("x");
        double y = (Double)((JSONObject)cloud).get("y");
        double height = (Double)((JSONObject)cloud).get("height");
        double width = (Double)((JSONObject)cloud).get("width");
        String image =(String) ((JSONObject)cloud).get("image");
        Entity obj = new Cloud(x,y,height,width, Entity.Layer.BACKGROUND,image);
        return obj;
    }
}
