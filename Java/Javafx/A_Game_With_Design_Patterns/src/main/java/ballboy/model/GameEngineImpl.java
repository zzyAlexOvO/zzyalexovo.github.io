package ballboy.model;

import ballboy.levels.Level;

import ballboy.levels.LevelBuilder;
import netscape.javascript.JSObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class GameEngineImpl implements GameEngine{
    private Level currentLevel;
    private JSONObject configuration;

    public GameEngineImpl(String json) throws IOException {
        JSONParser parser = new JSONParser();
        try{
            FileReader reader = new FileReader(json);
            configuration = (JSONObject) parser.parse(reader);
            reader.close();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.startLevel();
    }
    @Override
    public Level getCurrentLevel() {
        return this.currentLevel;
    }

    @Override
    public void startLevel() {
        loadLevel(1);
    }

    @Override
    public boolean boostHeight() {
        return currentLevel.boostHeight();
    }

    @Override
    public boolean dropHeight() {

        return currentLevel.dropHeight();
    }

    @Override
    public boolean moveLeft() {
        return currentLevel.moveLeft();
    }

    @Override
    public boolean moveRight() {
        return currentLevel.moveRight();
    }

    @Override
    public boolean stopLeft() {
        return currentLevel.stopLeft();
    }

    @Override
    public boolean stopRight() {
        return currentLevel.stopRight();
    }

    @Override
    public void tick() {
        currentLevel.tick();
    }

    private void loadLevel(int levelNumber) {
        JSONObject levels = (JSONObject)configuration.get("levels");

        String key = String.valueOf(levelNumber);
        JSONObject levelObj = (JSONObject)levels.get(key);

        if (levelObj != null) {
            this.currentLevel = (new LevelBuilder(levelObj)).generate();
        }
    }
}
