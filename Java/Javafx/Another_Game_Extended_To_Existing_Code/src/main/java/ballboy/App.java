package ballboy;

import ballboy.model.Entity;
import ballboy.model.GameEngine;
import ballboy.model.GameEngineImpl;
import ballboy.model.Level;
import ballboy.model.factories.*;
import ballboy.model.levels.LevelImpl;
import ballboy.model.levels.PhysicsEngine;
import ballboy.model.levels.PhysicsEngineImpl;
import ballboy.model.observer.Observer;
import ballboy.model.observer.LevelOb;
import ballboy.model.observer.TotalOb;
import ballboy.view.GameWindow;
import javafx.application.Application;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * Application root.
 *
 * Wiring of the dependency graph should be done manually in the start method.
 */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Map<String, String> params = getParameters().getNamed();

        String s = "Java 11 sanity check";
        if (s.isBlank()) {
            throw new IllegalStateException("You must be running Java 11+. You won't ever see this exception though" +
                    " as your code will fail to compile on Java 10 and below.");
        }

        ConfigurationParser configuration = new ConfigurationParser();
        JSONObject parsedConfiguration = null;
        try {
            parsedConfiguration = configuration.parseConfig("config.json");
        } catch (ConfigurationParseException e) {
            System.out.println(e);
            System.exit(-1);
        }

        final double frameDurationMilli = 17;
        PhysicsEngine engine = new PhysicsEngineImpl(frameDurationMilli);

        EntityFactoryRegistry entityFactoryRegistry = new EntityFactoryRegistry();
        entityFactoryRegistry.registerFactory("cloud", new CloudFactory());
        entityFactoryRegistry.registerFactory("enemy", new EnemyFactory());
        entityFactoryRegistry.registerFactory("background", new StaticEntityFactory(Entity.Layer.BACKGROUND));
        entityFactoryRegistry.registerFactory("static", new StaticEntityFactory(Entity.Layer.FOREGROUND));
        entityFactoryRegistry.registerFactory("finish", new FinishFactory());
        entityFactoryRegistry.registerFactory("hero", new BallboyFactory());
        entityFactoryRegistry.registerFactory("squareCat", new SquareCatFactory());


        Integer levelIndex = ((Number) parsedConfiguration.get("currentLevelIndex")).intValue();
        JSONArray levelConfigs = (JSONArray) parsedConfiguration.get("levels");
        JSONArray colours = (JSONArray)parsedConfiguration.get("colours");

        List<Level> levels = new ArrayList<>();
        Text currentScore = new Text(0,10,"current score: ");
        List<LevelOb> obs = new ArrayList<>();
        Text totalScore = new Text(0,20,"total score: ");
        TotalOb totalOb = new TotalOb(totalScore,obs);
        for(Object levelConfig: levelConfigs) {
            Level level = new LevelImpl((JSONObject)levelConfig, engine, entityFactoryRegistry, frameDurationMilli,colours);
            LevelOb levelOb = new LevelOb(level,currentScore);
            level.attach(levelOb);
            levelOb.attach(totalOb);
            obs.add(levelOb);
            levels.add(level);
        }
        for (Object i: colours){
            for (Observer ob:obs){
                ob.addColour((String) i);
            }
            totalOb.addColour((String) i);
        }

        GameEngine gameEngine = new GameEngineImpl(levels,levelIndex,totalOb);

        GameWindow window = new GameWindow(gameEngine, 640, 400, frameDurationMilli);
        window.getPane().getChildren().addAll(currentScore,totalScore);
        window.run();

        primaryStage.setTitle("Ballboy");
        primaryStage.setScene(window.getScene());
        primaryStage.setResizable(false);
        primaryStage.show();

        window.run();
    }
}
