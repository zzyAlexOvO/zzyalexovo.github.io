package ballboy.Factory;

import ballboy.Entities.Entity;
import org.json.simple.JSONObject;

public interface EntityFactory {
    Entity build(JSONObject configuration);
}
