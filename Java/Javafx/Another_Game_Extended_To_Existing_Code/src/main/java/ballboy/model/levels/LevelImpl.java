package ballboy.model.levels;

import ballboy.ConfigurationParseException;
import ballboy.model.Entity;
import ballboy.model.Level;
import ballboy.model.entities.ControllableDynamicEntity;
import ballboy.model.entities.DynamicEntity;
import ballboy.model.entities.StaticEntity;
import ballboy.model.entities.utilities.Vector2D;
import ballboy.model.factories.EntityFactory;
import ballboy.model.observer.LevelOb;
import ballboy.model.observer.Observer;
import javafx.scene.paint.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Level logic, with abstract factor methods.
 */
public class LevelImpl implements Level {

    private List<Entity> entities = new ArrayList<>();
    private final PhysicsEngine engine;
    private final EntityFactory entityFactory;
    private ControllableDynamicEntity<DynamicEntity> hero;
    private Entity finish;
    private double levelHeight;
    private double levelWidth;
    private double levelGravity;
    private double floorHeight;
    private Color floorColor;

    //*
    private boolean win = false;
    private Entity cat;
    private List<LevelOb> obs;
    private Map<String,Integer> scores;
    //*


    private final double frameDurationMilli;

    /**
     * A callback queue for post-update jobs. This is specifically useful for scheduling jobs mid-update
     * that require the level to be in a valid state.
     */
    private Queue<Runnable> afterUpdateJobQueue = new ArrayDeque<>();

    public LevelImpl(
            JSONObject levelConfiguration,
            PhysicsEngine engine,
            EntityFactory entityFactory,
            double frameDurationMilli,
            JSONArray colours) {
        this.engine = engine;
        this.entityFactory = entityFactory;
        this.frameDurationMilli = frameDurationMilli;
        this.obs = new ArrayList<>();
        this.scores = new HashMap<>();
        for (Object i: colours){
            this.scores.put((String)i,0);
        }
        initLevel(levelConfiguration);
    }

    private LevelImpl(List<Entity> entities,
    PhysicsEngine engine,
    EntityFactory entityFactory,
    ControllableDynamicEntity<DynamicEntity> hero,
    Entity finish,
    double levelHeight,
    double levelWidth,
    double levelGravity,
    double floorHeight,
    Color floorColor,
    boolean win,
    Entity cat,
    List<LevelOb> obs,
    Map<String,Integer> scores,
    double frameDurationMilli,
    Queue<Runnable> afterUpdateJobQueue){
        this.entities = entities;
        this.engine = engine;
        this.entityFactory = entityFactory;
        this.levelHeight = levelHeight;
        this.levelWidth = levelWidth;
        this.levelGravity = levelGravity;
        this.floorHeight = floorHeight;
        this.floorColor = floorColor;
        this.win = win;
        this.obs = new ArrayList<>();
        this.scores = scores;
        this.frameDurationMilli = frameDurationMilli;
        this.afterUpdateJobQueue = afterUpdateJobQueue;
        this.entities = new ArrayList<>();
        this.hero = hero.copy(this);
        this.finish = finish.copy(this);
        this.cat = cat.copy(this);
        for(LevelOb ob:obs) {
            this.obs.add(ob.copy(this));
        }
        for (Entity entity: entities){
            if (entity.equals(hero) || entity.equals(finish) || entity.equals(cat)){
                continue;
            }
            this.entities.add(entity.copy(this));
        }
        this.entities.add(this.hero);
        this.entities.add(this.finish);
        this.entities.add(this.cat);
    }
    //*

    /**
     * Instantiates a level from the level configuration.
     *
     * @param levelConfiguration The configuration for the level.
     */
    private void initLevel(JSONObject levelConfiguration) {
        this.levelWidth = ((Number) levelConfiguration.get("levelWidth")).doubleValue();
        this.levelHeight = ((Number) levelConfiguration.get("levelHeight")).doubleValue();
        this.levelGravity = ((Number) levelConfiguration.get("levelGravity")).doubleValue();

        JSONObject floorJson = (JSONObject) levelConfiguration.get("floor");
        this.floorHeight = ((Number) floorJson.get("height")).doubleValue();
        String floorColorWeb = (String) floorJson.get("color");
        this.floorColor = Color.web(floorColorWeb);

        JSONArray generalEntities = (JSONArray) levelConfiguration.get("genericEntities");
        for (Object o : generalEntities) {
            this.entities.add(entityFactory.createEntity(this, (JSONObject) o));
        }

        JSONObject heroConfig = (JSONObject) levelConfiguration.get("hero");
        double maxVelX = ((Number) levelConfiguration.get("maxHeroVelocityX")).doubleValue();

        Object hero = entityFactory.createEntity(this, heroConfig);
        if (!(hero instanceof DynamicEntity)) {
            throw new ConfigurationParseException("hero must be a dynamic entity");
        }
        DynamicEntity dynamicHero = (DynamicEntity) hero;
        Vector2D heroStartingPosition = dynamicHero.getPosition();
        this.hero = new ControllableDynamicEntity<>(dynamicHero, heroStartingPosition, maxVelX, floorHeight,
                levelGravity);
        this.entities.add(this.hero);

        JSONObject finishConfig = (JSONObject) levelConfiguration.get("finish");
        this.finish = entityFactory.createEntity(this, finishConfig);
        this.entities.add(finish);

        JSONObject catConfig = (JSONObject) levelConfiguration.get("squareCat");
        this.cat = entityFactory.createEntity(this, catConfig);
        this.entities.add(cat);

    }

    @Override
    public List<Entity> getEntities() {
        return Collections.unmodifiableList(entities);
    }

    private List<DynamicEntity> getDynamicEntities() {
        return entities.stream().filter(e -> e instanceof DynamicEntity).map(e -> (DynamicEntity) e).collect(
                Collectors.toList());
    }

    private List<StaticEntity> getStaticEntities() {
        return entities.stream().filter(e -> e instanceof StaticEntity).map(e -> (StaticEntity) e).collect(
                Collectors.toList());
    }

    @Override
    public double getLevelHeight() {
        return this.levelHeight;
    }

    @Override
    public double getLevelWidth() {
        return this.levelWidth;
    }

    @Override
    public double getHeroHeight() {
        return hero.getHeight();
    }

    @Override
    public double getHeroWidth() {
        return hero.getWidth();
    }

    @Override
    public double getFloorHeight() {
        return floorHeight;
    }

    @Override
    public Color getFloorColor() {
        return floorColor;
    }

    @Override
    public double getGravity() {
        return levelGravity;
    }

    @Override
    public void update() {
        List<DynamicEntity> dynamicEntities = getDynamicEntities();

        dynamicEntities.stream().forEach(e -> {
            e.update(frameDurationMilli, levelGravity);
        });

        for (int i = 0; i < dynamicEntities.size(); ++i) {
            DynamicEntity dynamicEntityA = dynamicEntities.get(i);

            for (int j = i + 1; j < dynamicEntities.size(); ++j) {
                DynamicEntity dynamicEntityB = dynamicEntities.get(j);

                if (dynamicEntityA.collidesWith(dynamicEntityB)) {
                    dynamicEntityA.collideWith(dynamicEntityB);
                    dynamicEntityB.collideWith(dynamicEntityA);
                    if (!isHero(dynamicEntityA) && !isHero(dynamicEntityB)) {
                        engine.resolveCollision(dynamicEntityA, dynamicEntityB);
                    }
                }
            }

            for (StaticEntity staticEntity : getStaticEntities()) {
                if (dynamicEntityA.collidesWith(staticEntity)) {
                    dynamicEntityA.collideWith(staticEntity);
                    engine.resolveCollision(dynamicEntityA, staticEntity, this);
                }
            }
        }

        dynamicEntities.stream().forEach(e -> engine.enforceWorldLimits(e, this));

        afterUpdateJobQueue.forEach(j -> j.run());
        afterUpdateJobQueue.clear();

    }

    @Override
    public double getHeroX() {
        return hero.getPosition().getX();
    }

    @Override
    public double getHeroY() {
        return hero.getPosition().getY();
    }

    @Override
    public boolean boostHeight() {
        return hero.boostHeight();
    }

    @Override
    public boolean dropHeight() {
        return hero.dropHeight();
    }

    @Override
    public boolean moveLeft() {
        return hero.moveLeft();
    }

    @Override
    public boolean moveRight() {
        return hero.moveRight();
    }

    @Override
    public boolean isHero(Entity entity) {
        return entity == hero;
    }

    @Override
    public boolean isFinish(Entity entity) {
        return this.finish == entity;
    }

    @Override
    public void resetHero() {
        afterUpdateJobQueue.add(() -> this.hero.reset());
    }

    @Override
    public void finish() {
        this.win = true;
    }

    @Override
    public boolean isWin(){
        return this.win;
    }

    @Override
    public void safeMode(){
    }

    @Override
    public void remove(Entity entity){
        if(entity.getColour() != null){
            scores.put(entity.getColour(),scores.get(entity.getColour())+100);
            notifies();
        }
        this.entities.remove(entity);
    }

    @Override
    public boolean isCat(Entity entity){
        return this.cat.equals(entity);
    }

    @Override
    public void attach(LevelOb ob){
        this.obs.add(ob);
    }

    @Override
    public void detach(LevelOb ob){
        this.obs.remove(ob);
    }

    @Override
    public void notifies(){
        for (Observer ob:obs){
            ob.update();
        }
    }

    @Override
    public Map<String,Integer> getScores(){
        return this.scores;
    }

    @Override
    public Level save(){
        Level copy = new LevelImpl(entities,
                this.engine,
                this.entityFactory,
                this.hero,
                this.finish,
                this.levelHeight,
                this.levelWidth,
                this.levelGravity,
                this.floorHeight,
                this.floorColor,
                this.win,
                this.cat,
                this.obs,
                new HashMap<>(this.scores),
                this.frameDurationMilli,
                this.afterUpdateJobQueue);
        return copy;
    }

    @Override
    public List<LevelOb> getObs() {
        return this.obs;
    }

}
