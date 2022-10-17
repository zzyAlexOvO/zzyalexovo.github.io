package app.model;
import app.model.exception.CharacterNotFoundException;
import app.view.AlertFactory;
import com.google.gson.JsonObject;
import javafx.scene.control.Hyperlink;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public interface Engine {
    void start() throws IOException;
    void pauseMusic();
    void playMusic();
    void loadingLoad() throws IOException;
    void searchLoad() throws IOException;
    Map<Integer,String> characters(String pattern) throws NoSuchAlgorithmException;
    JsonObject characterLoad(int id) throws IOException, NoSuchAlgorithmException;
    JsonObject characterLoad(String url) throws IOException, NoSuchAlgorithmException;
    JsonObject comicLoad(int id) throws NoSuchAlgorithmException, IOException;
    int characterComics(int id) throws NoSuchAlgorithmException, CharacterNotFoundException;
    void thresholdLoad() throws IOException;
    void sendReport(String content) throws IOException;
    void findCharacter(int id) throws IOException, NoSuchAlgorithmException;
    void findComic(int id) throws IOException, NoSuchAlgorithmException;
    void clearCache();
    List<Hyperlink> getBreadcrumb();

    String getResource(String key);

    List<String> getLanguages();
    AlertFactory getAlertFactory();
    DBmanager getDBManager();
    void setLanguage(String language) throws IOException;
}
