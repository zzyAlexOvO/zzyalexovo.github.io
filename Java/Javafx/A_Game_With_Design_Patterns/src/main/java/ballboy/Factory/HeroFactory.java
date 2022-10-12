package ballboy.Factory;


import ballboy.Entities.Entity;
import ballboy.Entities.Hero;
import org.json.simple.JSONObject;

public class HeroFactory implements EntityFactory {

    @Override
    public Entity build(JSONObject hero) {
        String size = (String)hero.get("size");
        double x = (Double)hero.get("x");
        double y = (Double)hero.get("y");
        String imagePath = (String)hero.get("image");
        System.out.println(imagePath);
        double height;
        double width;
        if (size == "small"){
            height = 50;
            width = 50;
        }else if (size =="large"){
            height = 150;
            width = 150;
        }else{
            height = 100;
            width = 100;
        }
        Entity obj = new Hero(x,y,height,width, Entity.Layer.FOREGROUND,imagePath);
        return obj;
    }
}
