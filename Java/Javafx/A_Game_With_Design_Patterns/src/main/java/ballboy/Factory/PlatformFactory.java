package ballboy.Factory;

import ballboy.Entities.Enemy;
import ballboy.Entities.Entity;
import ballboy.Entities.Platform;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class PlatformFactory implements EntityFactory{
    @Override
    public Entity build(JSONObject platform) {
        double x = (Double)((JSONObject)platform).get("x");
        double y = (Double)((JSONObject)platform).get("y");
        double height = (Double)((JSONObject)platform).get("height");
        double width = (Double)((JSONObject)platform).get("width");
        String image =(String) ((JSONObject)platform).get("image");
        Entity obj = new Platform(x,y,height,width, Entity.Layer.FOREGROUND,image);
        return obj;
    }
}
