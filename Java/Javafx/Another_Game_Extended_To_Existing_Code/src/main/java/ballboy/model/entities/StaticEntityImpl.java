package ballboy.model.entities;

import ballboy.model.Entity;
import ballboy.model.Level;
import ballboy.model.entities.utilities.AxisAlignedBoundingBox;
import ballboy.model.entities.utilities.Vector2D;
import javafx.scene.image.Image;

/**
 * A static entity.
 */
public class StaticEntityImpl extends StaticEntity {
    private final AxisAlignedBoundingBox volume;
    private final Entity.Layer layer;
    private final Image image;

    public StaticEntityImpl(
            AxisAlignedBoundingBox volume,
            Entity.Layer layer,
            Image image
    ) {
        this.volume = volume;
        this.layer = layer;
        this.image = image;
    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public Vector2D getPosition() {
        return new Vector2D(volume.getLeftX(), volume.getTopY());
    }

    @Override
    public double getHeight() {
        return volume.getHeight();
    }

    @Override
    public double getWidth() {
        return volume.getWidth();
    }

    @Override
    public Entity.Layer getLayer() {
        return this.layer;
    }

    @Override
    public AxisAlignedBoundingBox getVolume() {
        return this.volume;
    }

    @Override
    public String getColour() {
        return null;
    }

    @Override
    public Entity copy(Level level) {
        return new StaticEntityImpl(this.volume.copy(),this.layer,this.image);
    }

}
