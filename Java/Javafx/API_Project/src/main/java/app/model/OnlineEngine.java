package app.model;

import app.model.exception.CharacterNotFoundException;
import app.view.AlertFactory;
import app.view.ViewFactory;
import app.model.utils.Hash;
import app.model.utils.Routes;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OnlineEngine implements Engine {
    public final String publicKey;
    public final String privateKey;

    public ViewFactory viewFactory;
    private BgmManager bgmManager;
    private DBmanager dBmanager;
    private Routes routes;
    private EmailManager emailManager;
    private LanguageManager languageManager;
    private BreadcrumbObserver breadcrumb;

    private ThresholdListener thresholdListener;
    private AlertFactory alertFactory;

    private String fromEmail;
    public OnlineEngine(Stage stage, Routes routes, String publicKey, String privateKey, String fromEmail,String emailKey){
        this.viewFactory = new ViewFactory(this,stage);
        this.emailManager = new EmailManager(emailKey);
        this.dBmanager = new DBmanager();
        this.bgmManager = new BgmManager();
        this.breadcrumb = new BreadcrumbObserver();
        this.routes = routes;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.fromEmail = fromEmail;
        this.languageManager = new LanguageManager();

        this.thresholdListener = new ThresholdListener();
        this.alertFactory = new AlertFactory(languageManager);
    }
    public OnlineEngine(ViewFactory viewFactory,AlertFactory alertFactory, EmailManager emailManager, BgmManager bgmManager,DBmanager dBmanager, Routes routes, String publicKey, String privateKey){
        this.viewFactory = viewFactory;
        this.emailManager = emailManager;
        this.bgmManager = bgmManager;
        this.dBmanager = dBmanager;
        this.routes = routes;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.languageManager = new LanguageManager();
        this.breadcrumb = new BreadcrumbObserver();

        this.thresholdListener = new ThresholdListener();
        this.alertFactory = alertFactory;
    }
    public void start() throws IOException {
        //dBmanager.removeDB();
        dBmanager.createDB();
        dBmanager.setupDB();
        bgmManager.setup();
        languageManager.initLanguages();
        setLanguage("english");
        loadingLoad();
    }
    @Override
    public AlertFactory getAlertFactory(){
        return this.alertFactory;
    }
    public void pauseMusic(){
        bgmManager.pause();
    }
    public void playMusic(){
        bgmManager.play();
    }
    public void thresholdLoad() throws IOException {
        viewFactory.buildThreshold();
    }
    public void loadingLoad() throws IOException {
        viewFactory.buildLoading();
    }

    public void searchLoad() throws IOException {
        viewFactory.buildSearch();
    }

    public List<Hyperlink> getBreadcrumb(){
        return this.breadcrumb.get();
    }

    public Map<Integer,String> characters(String pattern) throws NoSuchAlgorithmException {
        String ts = OffsetDateTime.now().toString();
        ts = ts.replace("+","");
        System.out.println(ts);
        String hash = Hash.md5(ts,privateKey,publicKey);
        HttpResponse response = routes.search(pattern,ts,publicKey,hash);
        if(response.statusCode() == 200){
            JsonObject jsonObject = new JsonParser().parse(response.body().toString()).getAsJsonObject();
            JsonArray list = jsonObject.get("data").getAsJsonObject().get("results").getAsJsonArray();
            Map<Integer,String> map = new HashMap<>();
            for (JsonElement object:list){
                JsonObject character = object.getAsJsonObject();
                String name = character.get("name").getAsString();
                map.put(character.get("id").getAsInt(),name);
            }
            return map;
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sorry");
            alert.setContentText(response.body().toString());
            alert.showAndWait();
        }
        return null;
    }

    public JsonObject characterLoad(int id) throws NoSuchAlgorithmException {
        String ts = OffsetDateTime.now().toString();
        ts = ts.replace("+","");
        String hash = Hash.md5(ts,privateKey,publicKey);

        HttpResponse response = routes.character(id,ts,publicKey,hash);
        if(response.statusCode() == 200){
            JsonObject jsonObject = new JsonParser().parse(response.body().toString()).getAsJsonObject();
            JsonArray list = jsonObject.get("data").getAsJsonObject().get("results").getAsJsonArray();
            JsonObject object = list.get(0).getAsJsonObject();
            return object;
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sorry");
            alert.setContentText(response.body().toString());
            alert.showAndWait();
        }
        return null;
    }

    public JsonObject characterLoad(String name) throws IOException, NoSuchAlgorithmException {
        String ts = OffsetDateTime.now().toString();
        ts = ts.replace("+","");
        String hash = Hash.md5(ts,privateKey,publicKey);

        String new_name = name.replace(" ","%20");
        HttpResponse response = routes.character(new_name,ts,publicKey,hash);
        if(response.statusCode() == 200){
            JsonObject jsonObject = new JsonParser().parse(response.body().toString()).getAsJsonObject();
            JsonArray list = jsonObject.get("data").getAsJsonObject().get("results").getAsJsonArray();
            JsonObject object = list.get(0).getAsJsonObject();
            return object;
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sorry");
            alert.setContentText(response.body().toString());
            alert.showAndWait();
        }
        return null;
    }

    public JsonObject comicLoad(int id) throws NoSuchAlgorithmException{
        String ts = OffsetDateTime.now().toString();
        ts = ts.replace("+","");
        String hash = Hash.md5(ts,privateKey,publicKey);

        HttpResponse response = routes.comic(id,ts,publicKey,hash);
        if(response.statusCode() == 200){
            JsonObject jsonObject = new JsonParser().parse(response.body().toString()).getAsJsonObject();
            JsonArray list = jsonObject.get("data").getAsJsonObject().get("results").getAsJsonArray();
            JsonObject object = list.get(0).getAsJsonObject();
            return object;
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(languageManager.getResource("Sorry"));
            alert.setContentText(response.body().toString());
            alert.showAndWait();
        }
        return null;
    }

    @Override
    public void sendReport(String content) throws IOException {
        System.out.println(content);
        TextInputDialog td = new TextInputDialog();
        td.setHeaderText(languageManager.getResource("emailDescription"));
        td.showAndWait();
        emailManager.send(fromEmail,td.getResult(),"Report",content);
    }

    @Override
    public void findCharacter(int id) throws IOException, NoSuchAlgorithmException{
        System.out.println("find character");
        JsonObject object;
        String result = dBmanager.checkCache("character",id);
        if (result == null){
            System.out.println("no cache hit");
            object = characterLoad(id);
            if (object == null){
                return;
            }
            dBmanager.insertCache("character",id,object.get("name").getAsString(),object.toString());
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, languageManager.getResource("cacheConfirmation"), ButtonType.YES, ButtonType.NO);
            alert.setTitle(languageManager.getResource("cacheHit"));
            alert.showAndWait();
            if(alert.getResult() == ButtonType.YES){
                object = new JsonParser().parse(result).getAsJsonObject();
            }else{
                object = characterLoad(id);
                if (object == null){
                    return;
                }
                dBmanager.updateCache("character",id,object.get("name").getAsString(),object.toString());
            }
        }
        update(object.get("name").getAsString(),id,"character");
        try {
            if(thresholdListener.analyse(characterComics(id))){
                alertFactory.thresholdAlert();
            }
            viewFactory.buildCharacter(object);
        } catch (CharacterNotFoundException e) {
            alertFactory.normalErrorAlert("this character's comic record is not found");
            viewFactory.buildCharacter(object);
        }
    }

    public int characterComics(int id) throws NoSuchAlgorithmException, CharacterNotFoundException {
        String ts = OffsetDateTime.now().toString();
        ts = ts.replace("+","");
        String hash = Hash.md5(ts,privateKey,publicKey);
        HttpResponse response = routes.characterComics(id,ts,publicKey,hash);
        if(response.statusCode() == 200){
            JsonObject jsonObject = new JsonParser().parse(response.body().toString()).getAsJsonObject();
            return jsonObject.get("data").getAsJsonObject().get("total").getAsInt();
        }else{
            alertFactory.normalErrorAlert(response.body().toString());
            throw new CharacterNotFoundException();
        }
    }

    @Override
    public void findComic(int id) throws IOException, NoSuchAlgorithmException {
        JsonObject object;
        String result = dBmanager.checkCache("comic",id);
        if (result == null){
            System.out.println("no cache hit");
            object = comicLoad(id);
            if (object == null){
                return;
            }
            dBmanager.insertCache("comic",id,object.get("title").getAsString(),object.toString());
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, languageManager.getResource("cacheConfirmation"), ButtonType.YES, ButtonType.NO);
            alert.setTitle(languageManager.getResource("cacheHit"));
            alert.showAndWait();
            if(alert.getResult() == ButtonType.YES){
                object = new JsonParser().parse(result).getAsJsonObject();
            }else{
                object = comicLoad(id);
                if (object == null){
                    return;
                }
                dBmanager.updateCache("comic",id,object.get("title").getAsString(),object.toString());
            }
        }
        update(object.get("title").getAsString(),id,"comic");
        viewFactory.buildComic(object);
    }

    @Override
    public void clearCache() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, languageManager.getResource("clearCacheConfirmation"), ButtonType.YES, ButtonType.NO);
        alert.setTitle(languageManager.getResource("cacheHit"));
        alert.showAndWait();
        if(alert.getResult() == ButtonType.YES){
            dBmanager.clearCache();
        }
    }

    @Override
    public void setLanguage(String language) throws IOException {
        languageManager.setResources(language);
        viewFactory.refresh();
    }

    @Override
    public String getResource(String key){
        return languageManager.getResource(key);
    }

    @Override
    public List<String> getLanguages(){
        return languageManager.getLanguages();
    }

    public DBmanager getDBManager(){
        return this.dBmanager;
    }

    public void update(String name, int id, String type){
        //System.out.println("build link "+ name + type);
        try {
            Hyperlink link = new Hyperlink();
            link.setText(name);
            if (type == "comic") {
                link.setOnAction((ActionEvent e) -> {
                    try {
                        findComic(id);
                    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        noSuchAlgorithmException.printStackTrace();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
            } else {
                link.setOnAction((ActionEvent e) -> {
                    try {
                        findCharacter(id);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        noSuchAlgorithmException.printStackTrace();
                    }
                });
            }
            this.breadcrumb.update(link);
        }
        catch (NoClassDefFoundError e1){

        }
        catch (ExceptionInInitializerError e2){

        }
    }
}
