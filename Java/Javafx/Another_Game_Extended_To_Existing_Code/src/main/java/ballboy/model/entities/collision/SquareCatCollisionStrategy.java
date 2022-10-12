package ballboy.model.entities.collision;

import ballboy.model.Entity;
import ballboy.model.Level;

public class SquareCatCollisionStrategy implements CollisionStrategy{
    private final Level level;

    public SquareCatCollisionStrategy(Level level) {
        this.level = level;
    }

    @Override
    public void collideWith(Entity currentEntity, Entity hitEntity) {

    }

    @Override
    public CollisionStrategy copy(Level level) {
        return new SquareCatCollisionStrategy(level);
    }
}
