package ballboy.Factory;

import ballboy.Entities.Cloud;
import ballboy.Entities.Entity;
import ballboy.Entities.LandScape;
import org.json.simple.JSONObject;

public class LandScapeFactory implements EntityFactory{
    @Override
    public Entity build(JSONObject landScape) {
        double x = 0.0;
        double y = 0.0;
        double height = (Double)((JSONObject)landScape).get("height");
        double width = (Double)((JSONObject)landScape).get("width");
        String image =(String) ((JSONObject)landScape).get("image");
        Entity obj = new LandScape(x,y,height,width, Entity.Layer.BACKGROUND,image);
        return obj;
    }
}
