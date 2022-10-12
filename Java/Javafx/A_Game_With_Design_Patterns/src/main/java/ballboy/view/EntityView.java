package ballboy.view;

import javafx.scene.Node;
import ballboy.Entities.Entity;

public interface EntityView {
    void update(double xViewportOffset, double yViewportOffset);

    boolean matchesEntity(Entity entity);

    void markForDelete();

    Node getNode();

    boolean isMarkedForDelete();
}
