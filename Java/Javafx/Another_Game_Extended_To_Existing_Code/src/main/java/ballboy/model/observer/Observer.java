package ballboy.model.observer;

import ballboy.model.Level;

public interface Observer {
    void update();

    void addColour(String colour);
}
