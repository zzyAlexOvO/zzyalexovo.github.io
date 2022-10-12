package ballboy.Factory;

import ballboy.Entities.Entity;
import ballboy.Entities.Flag;
import org.json.simple.JSONObject;

public class FlagFactory implements EntityFactory{
    @Override
    public Entity build(JSONObject flag) {
        double x = (Double)((JSONObject)flag).get("x");
        double y = (Double)((JSONObject)flag).get("y");
        double height = (Double)((JSONObject)flag).get("height");
        double width = (Double)((JSONObject)flag).get("width");
        String image =(String) ((JSONObject)flag).get("image");
        Entity obj = new Flag(x,y,height,width, Entity.Layer.FOREGROUND,image);
        return obj;
    }
}
