package app.controllers;

import app.model.Engine;
import com.google.gson.JsonArray;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.io.IOException;
import java.util.List;

public class ControllerImpl implements Controller{
    protected Scene scene;
    protected Engine engine;
    @FXML
    Menu music;
    @FXML
    MenuItem play;
    @FXML
    MenuItem pause;
    @FXML
    Menu language;
    @Override
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void backEvent() throws IOException {
        engine.searchLoad();
    }
    public void pauseMusic(){
        engine.pauseMusic();
    }
    public void playMusic(){
        engine.playMusic();
    }

    @Override
    public void setup(JsonArray object, String label) { }

    @Override
    public void setText() {
    }

    @Override
    public void setMenuText(){
        music.setText(engine.getResource(music.getText()));
        play.setText(engine.getResource(play.getText()));
        pause.setText(engine.getResource(pause.getText()));
        language.setText(engine.getResource(language.getText()));
    }

    @Override
    public void setLanguage(String language) throws IOException {
        engine.setLanguage(language);
    }

    public void initLanguages(){
        for (String l:engine.getLanguages()){
            MenuItem item = new MenuItem(l);
            item.setOnAction((ActionEvent event) -> {
                try {
                    engine.setLanguage(l);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("item added "+l);
            language.getItems().add(item);
        }
    }
}
