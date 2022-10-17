package app.view;

import app.model.Engine;
import app.controllers.*;
import com.google.gson.JsonObject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    private static final String path = "pages/";
    private Engine model;
    private Stage stage;
    private Runnable refresh;
    public ViewFactory(Engine model, Stage stage){
        this.model = model;
        this.stage = stage;
    }
    public void buildLoading() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(path + "loading.fxml"));
        LoadingController controller = new LoadingController();
        controller.setEngine(model);
        loader.setController(controller);
        Parent entry = loader.load();
        Scene scene = new Scene(entry,640,480);
        controller.setScene(scene);
        controller.setup();
        stage.setScene(scene);
        this.refresh = () -> {
            try {
                buildLoading();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
    public void buildSearch() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(path + "search.fxml"));
        SearchController controller = new SearchController();
        controller.setEngine(model);
        loader.setController(controller);
        Parent entry = loader.load();
        Scene scene = new Scene(entry,640,480);
        controller.setScene(scene);
        controller.fillBreadcrumb(model.getBreadcrumb());
        stage.setScene(scene);
        this.refresh = () -> {
            try {
                buildSearch();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
    public void buildCharacter(JsonObject object) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(path + "character.fxml"));
        CharacterController controller = new CharacterController();
        controller.setEngine(model);
        loader.setController(controller);
        Parent entry = loader.load();
        Scene scene = new Scene(entry,640,480);
        controller.setScene(scene);
        controller.setup(object);
        stage.setScene(scene);
        this.refresh = () -> {
            try {
                buildCharacter(object);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
    public void buildComic(JsonObject object) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(path + "comic.fxml"));
        System.out.println("load");
        ComicController controller = new ComicController();
        controller.setEngine(model);
        loader.setController(controller);
        Parent entry = loader.load();
        Scene scene = new Scene(entry,640,480);
        controller.setScene(scene);
        controller.setup(object);
        stage.setScene(scene);
        this.refresh = () -> {
            try {
                buildComic(object);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
    public void buildThreshold() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(path + "threshold.fxml"));
        ThresholdController controller = new ThresholdController();
        controller.setEngine(model);
        loader.setController(controller);
        Parent entry = loader.load();
        Scene scene = new Scene(entry,640,480);
        controller.setScene(scene);
        controller.setup();
        stage.setScene(scene);
        this.refresh = () -> {
            try {
                buildThreshold();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
    public void refresh(){
        if (refresh != null){
            refresh.run();
        }
    }
}
