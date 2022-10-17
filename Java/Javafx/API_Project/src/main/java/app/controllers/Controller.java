package app.controllers;

import app.model.Engine;
import com.google.gson.JsonArray;
import javafx.scene.Scene;

import java.io.IOException;

public interface Controller {
    void setScene(Scene scene);
    void setEngine(Engine engine);
    void backEvent() throws IOException;
    void setup(JsonArray object, String label);
    void setText();

    void setMenuText();

    void setLanguage(String language) throws IOException;
}
