package app.model;

import app.model.exception.CharacterNotFoundException;
import app.view.ViewFactory;
import app.model.utils.Hash;
import app.model.utils.Routes;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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


public class DummyInEngine extends DummyEngine {
    public final String publicKey;
    public final String privateKey;

    private Routes routes;

    public DummyInEngine(Stage stage, Routes routes, String publicKey, String privateKey){
        super(stage);
        this.routes = routes;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
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

    public JsonObject characterLoad(int id) throws IOException, NoSuchAlgorithmException {
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

    public JsonObject comicLoad(int id) throws NoSuchAlgorithmException, IOException {
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
            alert.setTitle(super.getLanguageManager().getResource("Sorry"));
            alert.setContentText(response.body().toString());
            alert.showAndWait();
        }
        return null;
    }

    @Override
    public void findCharacter(int id) throws IOException, NoSuchAlgorithmException {
        System.out.println("find character");
        JsonObject object;
        String result = super.getDBManager().checkCache("character",id);
        if (result == null){
            System.out.println("no cache hit");
            object = characterLoad(id);
            if (object == null){
                return;
            }
            super.getDBManager().insertCache("character",id,object.get("name").getAsString(),object.toString());
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, super.getLanguageManager().getResource("cacheConfirmation"), ButtonType.YES, ButtonType.NO);
            alert.setTitle(super.getLanguageManager().getResource("cacheHit"));
            alert.showAndWait();
            if(alert.getResult() == ButtonType.YES){
                object = new JsonParser().parse(result).getAsJsonObject();
            }else{
                object = characterLoad(id);
                if (object == null){
                    return;
                }
                super.getDBManager().updateCache("character",id,object.get("name").getAsString(),object.toString());
            }
        }
        update(object.get("name").getAsString(),id,"character");
        try {
            if(thresholdListener.analyse(characterComics(id))){
                alertFactory.thresholdAlert();
            }
        } catch (CharacterNotFoundException e) {
            alertFactory.normalErrorAlert("this character's comic record is not found");
        }
        super.getFactory().buildCharacter(object);
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
        String result = super.getDBManager().checkCache("comic",id);
        if (result == null){
            System.out.println("no cache hit");
            object = comicLoad(id);
            if (object == null){
                return;
            }
            super.getDBManager().insertCache("comic",id,object.get("title").getAsString(),object.toString());
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, super.getLanguageManager().getResource("cacheConfirmation"), ButtonType.YES, ButtonType.NO);
            alert.setTitle(super.getLanguageManager().getResource("cacheHit"));
            alert.showAndWait();
            if(alert.getResult() == ButtonType.YES){
                object = new JsonParser().parse(result).getAsJsonObject();
            }else{
                object = comicLoad(id);
                if (object == null){
                    return;
                }
                super.getDBManager().updateCache("comic",id,object.get("title").getAsString(),object.toString());
            }
        }
        update(object.get("title").getAsString(),id,"comic");
        super.getFactory().buildComic(object);
    }
}
