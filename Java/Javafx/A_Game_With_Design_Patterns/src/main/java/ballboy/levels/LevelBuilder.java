package ballboy.levels;

import ballboy.Entities.Entity;

import ballboy.Entities.LandScape;
import ballboy.Entities.Platform;
import ballboy.Factory.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class LevelBuilder {
    private JSONObject configuration;
    private int levelHeight;
    private int levelWidth;
    private Level level;
    private double gravity;
    private double max_speed;
    private double accelerateRate;


    public LevelBuilder(JSONObject configuration){
        this.configuration = configuration;
        this.levelHeight = ((Long)configuration.get("height")).intValue();
        this.levelWidth = ((Long)configuration.get("width")).intValue();
        this.gravity = ((Double)configuration.get("gravity"));
        this.max_speed = ((Double)configuration.get("max_speed"));
        this.accelerateRate = ((Double)configuration.get("accelerate"));
        buildLevelAndHero();
        buildEnemies();
        buildClouds();
        buildFlag();
        buildPlatforms();
        buildLandScape();
    }

    private void buildLevelAndHero(){
        double floorHeight = (Double)((JSONObject)configuration.get("floor")).get("height");
        Entity hero = new HeroFactory().build((JSONObject) configuration.get("hero"));
        this.level = new LevelImpl(levelHeight,levelWidth,floorHeight,hero,gravity,max_speed,accelerateRate);
        this.level.getEntities().add(hero);
    }

    private void buildFlag(){
        Entity flag = new FlagFactory().build((JSONObject)configuration.get("flag"));
        this.level.getEntities().add(flag);
    }

    private void buildEnemies(){
        for (Object i:(JSONArray)configuration.get("enemies")) {
            Entity enemy = new EnemyFactory().build((JSONObject)i);
            this.level.getEntities().add(enemy);
        }
    }

    private void buildClouds(){
        for (Object i:(JSONArray)configuration.get("clouds")) {
            Entity cloud = new CloudFactory().build((JSONObject)i);
            this.level.getEntities().add(cloud);
        }
    }

    private void buildPlatforms(){
        for (Object i:(JSONArray)configuration.get("platforms")) {
            Entity platform = new PlatformFactory().build((JSONObject)i);
            this.level.getEntities().add(platform);
        }
    }

    private void buildLandScape(){
        Entity land = new LandScapeFactory().build((JSONObject)configuration.get("landscape"));
        this.level.getEntities().add(land);
    }


    public Level generate(){
        return this.level;
    }
}
