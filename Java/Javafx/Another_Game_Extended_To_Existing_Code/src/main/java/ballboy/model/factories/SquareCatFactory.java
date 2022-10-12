package ballboy.model.factories;

import ballboy.ConfigurationParseException;
import ballboy.model.Entity;
import ballboy.model.Level;
import ballboy.model.entities.DynamicEntityImpl;
import ballboy.model.entities.behaviour.PassiveEntityBehaviourStrategy;
import ballboy.model.entities.behaviour.SquareCatBehaviourStrategy;
import ballboy.model.entities.collision.BallboyCollisionStrategy;
import ballboy.model.entities.collision.PassiveCollisionStrategy;
import ballboy.model.entities.utilities.*;
import javafx.scene.image.Image;
import org.json.simple.JSONObject;

public class SquareCatFactory implements EntityFactory{
    @Override
    public Entity createEntity(Level level, JSONObject config) {
        try {
            String imageName = (String) config.getOrDefault("image", "squareCat.png");

            Image image = new Image(imageName);
            // preserve image ratio
            double height = 20;
            double width = height * image.getWidth() / image.getHeight();

            double startX = ((Number) config.get("startX")).doubleValue();
            double startY = ((Number) config.get("startY")).doubleValue();

            Vector2D startingPosition = new Vector2D(startX, startY);

            KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                    .setPosition(startingPosition)
                    .build();

            AxisAlignedBoundingBox volume = new AxisAlignedBoundingBoxImpl(
                    startingPosition,
                    height,
                    width
            );

            return new DynamicEntityImpl(
                    kinematicState,
                    volume,
                    Entity.Layer.FOREGROUND,
                    new Image(imageName),
                    new PassiveCollisionStrategy(),
                    new SquareCatBehaviourStrategy(level)
            );

        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid ballboy entity configuration | %s | %s", config, e));
        }
    }
}
