package app.model;

import app.model.exception.CharacterNotFoundException;
import app.view.AlertFactory;
import app.view.ViewFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyEngine implements Engine {
    public Stage stage;
    private BgmManager bgmManager;
    private LanguageManager languageManager;
    private ViewFactory viewFactory;
    private DBmanager dBmanager;
    private BreadcrumbObserver breadcrumb;
    protected AlertFactory alertFactory;
    protected ThresholdListener thresholdListener;
    public DummyEngine(Stage stage){
        this.breadcrumb = new BreadcrumbObserver();
        this.stage = stage;
        this.languageManager = new LanguageManager();
        this.bgmManager = new BgmManager();
        this.dBmanager = new DBmanager();
        this.viewFactory = new ViewFactory(this,stage);
        this.alertFactory = new AlertFactory(languageManager);
        this.thresholdListener = new ThresholdListener();
    }
    public void start() throws IOException {
        dBmanager.createDB();
        dBmanager.setupDB();
        bgmManager.setup();
        languageManager.initLanguages();
        setLanguage("english");
        loadingLoad();
    }
    public void pauseMusic(){
        bgmManager.pause();
    }
    public void playMusic(){
        bgmManager.play();
    }
    public void loadingLoad() throws IOException {
        viewFactory.buildLoading();
    }
    public void thresholdLoad() throws IOException {
        viewFactory.buildThreshold();
    }
    public void searchLoad() throws IOException {
        viewFactory.buildSearch();
    }

    public Map<Integer,String> characters(String pattern) throws NoSuchAlgorithmException {
        Map<Integer,String> map = new HashMap<>(){{
            put(1,"Superman");
            put(2,"SpiderMan");}};
        return map;
    }

    public JsonObject characterLoad(int id) throws IOException, NoSuchAlgorithmException {
        String json = """
                           {
                           "code": 200,
                           "status": "Ok",
                           "copyright": "© 2022 MARVEL",
                           "attributionText": "Data provided by Marvel. © 2022 MARVEL",
                           "attributionHTML": "<a href=\\"http://marvel.com\\">Data provided by Marvel. © 2022 MARVEL</a>",
                           "etag": "55342c8b21941bfea4b795ff85633d9063e1da0e",
                           "data": {
                             "offset": 0,
                             "limit": 20,
                             "total": 1,
                             "count": 1,
                             "results": [
                               {
                                 "id": 1011334,
                                 "name": "3-D Man",
                                 "description": "",
                                 "modified": "2014-04-29T14:18:17-0400",
                                 "thumbnail": {
                                   "path": "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784",
                                   "extension": "jpg"
                                 },
                                 "resourceURI": "http://gateway.marvel.com/v1/public/characters/1011334",
                                 "comics": {
                                   "available": 12,
                                   "collectionURI": "http://gateway.marvel.com/v1/public/characters/1011334/comics",
                                   "items": [
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/comics/21366",
                                       "name": "Avengers: The Initiative (2007) #14"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/comics/24571",
                                       "name": "Avengers: The Initiative (2007) #14 (SPOTLIGHT VARIANT)"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/comics/21546",
                                       "name": "Avengers: The Initiative (2007) #15"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/comics/21741",
                                       "name": "Avengers: The Initiative (2007) #16"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/comics/21975",
                                       "name": "Avengers: The Initiative (2007) #17"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/comics/22299",
                                       "name": "Avengers: The Initiative (2007) #18"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/comics/22300",
                                       "name": "Avengers: The Initiative (2007) #18 (ZOMBIE VARIANT)"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/comics/22506",
                                       "name": "Avengers: The Initiative (2007) #19"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/comics/8500",
                                       "name": "Deadpool (1997) #44"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/comics/10223",
                                       "name": "Marvel Premiere (1972) #35"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/comics/10224",
                                       "name": "Marvel Premiere (1972) #36"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/comics/10225",
                                       "name": "Marvel Premiere (1972) #37"
                                     }
                                   ],
                                   "returned": 12
                                 },
                                 "series": {
                                   "available": 3,
                                   "collectionURI": "http://gateway.marvel.com/v1/public/characters/1011334/series",
                                   "items": [
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/series/1945",
                                       "name": "Avengers: The Initiative (2007 - 2010)"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/series/2005",
                                       "name": "Deadpool (1997 - 2002)"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/series/2045",
                                       "name": "Marvel Premiere (1972 - 1981)"
                                     }
                                   ],
                                   "returned": 3
                                 },
                                 "stories": {
                                   "available": 21,
                                   "collectionURI": "http://gateway.marvel.com/v1/public/characters/1011334/stories",
                                   "items": [
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/19947",
                                       "name": "Cover #19947",
                                       "type": "cover"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/19948",
                                       "name": "The 3-D Man!",
                                       "type": "interiorStory"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/19949",
                                       "name": "Cover #19949",
                                       "type": "cover"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/19950",
                                       "name": "The Devil's Music!",
                                       "type": "interiorStory"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/19951",
                                       "name": "Cover #19951",
                                       "type": "cover"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/19952",
                                       "name": "Code-Name:  The Cold Warrior!",
                                       "type": "interiorStory"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/47184",
                                       "name": "AVENGERS: THE INITIATIVE (2007) #14",
                                       "type": "cover"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/47185",
                                       "name": "Avengers: The Initiative (2007) #14 - Int",
                                       "type": "interiorStory"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/47498",
                                       "name": "AVENGERS: THE INITIATIVE (2007) #15",
                                       "type": "cover"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/47499",
                                       "name": "Avengers: The Initiative (2007) #15 - Int",
                                       "type": "interiorStory"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/47792",
                                       "name": "AVENGERS: THE INITIATIVE (2007) #16",
                                       "type": "cover"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/47793",
                                       "name": "Avengers: The Initiative (2007) #16 - Int",
                                       "type": "interiorStory"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/48361",
                                       "name": "AVENGERS: THE INITIATIVE (2007) #17",
                                       "type": "cover"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/48362",
                                       "name": "Avengers: The Initiative (2007) #17 - Int",
                                       "type": "interiorStory"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/49103",
                                       "name": "AVENGERS: THE INITIATIVE (2007) #18",
                                       "type": "cover"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/49104",
                                       "name": "Avengers: The Initiative (2007) #18 - Int",
                                       "type": "interiorStory"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/49106",
                                       "name": "Avengers: The Initiative (2007) #18, Zombie Variant - Int",
                                       "type": "interiorStory"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/49888",
                                       "name": "AVENGERS: THE INITIATIVE (2007) #19",
                                       "type": "cover"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/49889",
                                       "name": "Avengers: The Initiative (2007) #19 - Int",
                                       "type": "interiorStory"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/stories/54371",
                                       "name": "Avengers: The Initiative (2007) #14, Spotlight Variant - Int",
                                       "type": "interiorStory"
                                     }
                                   ],
                                   "returned": 20
                                 },
                                 "events": {
                                   "available": 1,
                                   "collectionURI": "http://gateway.marvel.com/v1/public/characters/1011334/events",
                                   "items": [
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/events/269",
                                       "name": "Secret Invasion"
                                     }
                                   ],
                                   "returned": 1
                                 },
                                 "urls": [
                                   {
                                     "type": "detail",
                                     "url": "http://marvel.com/characters/74/3-d_man?utm_campaign=apiRef&utm_source=a4df867c5bad9122e43258cb451170fe"
                                   },
                                   {
                                     "type": "wiki",
                                     "url": "http://marvel.com/universe/3-D_Man_(Chandler)?utm_campaign=apiRef&utm_source=a4df867c5bad9122e43258cb451170fe"
                                   },
                                   {
                                     "type": "comiclink",
                                     "url": "http://marvel.com/comics/characters/1011334/3-d_man?utm_campaign=apiRef&utm_source=a4df867c5bad9122e43258cb451170fe"
                                   }
                                 ]
                               }
                             ]
                           }
                         }
                         """;
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        JsonArray list = jsonObject.get("data").getAsJsonObject().get("results").getAsJsonArray();
        JsonObject object = list.get(0).getAsJsonObject();
        return object;
    }

    public JsonObject characterLoad(String url) throws IOException, NoSuchAlgorithmException {
        return(characterLoad(0));
    }

    public JsonObject comicLoad(int id) throws NoSuchAlgorithmException, IOException {
        String json = """
                {
                  "code": 200,
                  "status": "Ok",
                  "copyright": "© 2022 MARVEL",
                  "attributionText": "Data provided by Marvel. © 2022 MARVEL",
                  "attributionHTML": "<a href=\\"http://marvel.com\\">Data provided by Marvel. © 2022 MARVEL</a>",
                  "etag": "af4588c3d34cb4ea24125d77e36c532e6edc226a",
                  "data": {
                    "offset": 0,
                    "limit": 20,
                    "total": 1,
                    "count": 1,
                    "results": [
                      {
                        "id": 49888,
                        "digitalId": 0,
                        "title": "Deadpool Minibus (Hardcover)",
                        "issueNumber": 0,
                        "variantDescription": "",
                        "description": "Deadpool's wildest adventures are collected in one blood-soaked volume! What if Deadpool decided to kill everyone and everything that makes up the Marvel Universe? What if he followed that up by slaughtering the most famous fictional characters in classic literature? And what if he finally took aim at the ultimate target: himself? You'd have the fan-favorite \\"Deadpool Killogy,\\" that's what! But when Deadpool battles Deadpool, will he win or perish? Yes! Then: Deadpool awakes from a food coma to find...the zombie apocalypse has occurred! Can the Merc with a Mouth avoid becoming the Merc in their mouths?! And when Deadpool takes on Carnage, good-crazy battles bad-crazy, blood will flow - and Deadpool will literally go to pieces! Collecting DEADPOOL KILLS THE MARVEL UNIVERSE #1-4, DEADPOOL KILLUSTRATED #1-4, DEADPOOL KILLS DEADPOOL #1-4, NIGHT OF THE LIVING DEADPOOL #1-4, DEADPOOL VS. CARNAGE #1-4.",
                        "modified": "2014-08-29T12:44:12-0400",
                        "isbn": "978-0-7851-9030-1",
                        "upc": "",
                        "diamondCode": "",
                        "ean": "9780785 190301 55999",
                        "issn": "",
                        "format": "Hardcover",
                        "pageCount": 480,
                        "textObjects": [
                          {
                            "type": "issue_solicit_text",
                            "language": "en-us",
                            "text": "Deadpool's wildest adventures are collected in one blood-soaked volume! What if Deadpool decided to kill everyone and everything that makes up the Marvel Universe? What if he followed that up by slaughtering the most famous fictional characters in classic literature? And what if he finally took aim at the ultimate target: himself? You'd have the fan-favorite \\"Deadpool Killogy,\\" that's what! But when Deadpool battles Deadpool, will he win or perish? Yes! Then: Deadpool awakes from a food coma to find...the zombie apocalypse has occurred! Can the Merc with a Mouth avoid becoming the Merc in their mouths?! And when Deadpool takes on Carnage, good-crazy battles bad-crazy, blood will flow - and Deadpool will literally go to pieces! Collecting DEADPOOL KILLS THE MARVEL UNIVERSE #1-4, DEADPOOL KILLUSTRATED #1-4, DEADPOOL KILLS DEADPOOL #1-4, NIGHT OF THE LIVING DEADPOOL #1-4, DEADPOOL VS. CARNAGE #1-4."
                          }
                        ],
                        "resourceURI": "http://gateway.marvel.com/v1/public/comics/49888",
                        "urls": [
                          {
                            "type": "detail",
                            "url": "http://marvel.com/comics/collection/49888/deadpool_minibus_hardcover?utm_campaign=apiRef&utm_source=a4df867c5bad9122e43258cb451170fe"
                          }
                        ],
                        "series": {
                          "resourceURI": "http://gateway.marvel.com/v1/public/series/18745",
                          "name": "Deadpool Minibus (2014)"
                        },
                        "variants": [],
                        "collections": [],
                        "collectedIssues": [],
                        "dates": [
                          {
                            "type": "onsaleDate",
                            "date": "2014-09-03T00:00:00-0400"
                          },
                          {
                            "type": "focDate",
                            "date": "2014-07-15T00:00:00-0400"
                          }
                        ],
                        "prices": [
                          {
                            "type": "printPrice",
                            "price": 59.99
                          }
                        ],
                        "thumbnail": {
                          "path": "http://i.annihil.us/u/prod/marvel/i/mg/c/b0/5400a54331943",
                          "extension": "jpg"
                        },
                        "images": [
                          {
                            "path": "http://i.annihil.us/u/prod/marvel/i/mg/c/b0/5400a54331943",
                            "extension": "jpg"
                          }
                        ],
                        "creators": {
                          "available": 6,
                          "collectionURI": "http://gateway.marvel.com/v1/public/comics/49888/creators",
                          "items": [
                            {
                              "resourceURI": "http://gateway.marvel.com/v1/public/creators/10023",
                              "name": "Cullen Bunn",
                              "role": "writer"
                            },
                            {
                              "resourceURI": "http://gateway.marvel.com/v1/public/creators/13251",
                              "name": "Salva Espin",
                              "role": "artist"
                            },
                            {
                              "resourceURI": "http://gateway.marvel.com/v1/public/creators/9081",
                              "name": "Matteo Lolli",
                              "role": "artist"
                            },
                            {
                              "resourceURI": "http://gateway.marvel.com/v1/public/creators/9286",
                              "name": "Ramon Rosanas",
                              "role": "artist"
                            },
                            {
                              "resourceURI": "http://gateway.marvel.com/v1/public/creators/11560",
                              "name": "Dalibor Talajic",
                              "role": "artist"
                            },
                            {
                              "resourceURI": "http://gateway.marvel.com/v1/public/creators/11309",
                              "name": "Mike Del Mundo",
                              "role": "penciller (cover)"
                            }
                          ],
                          "returned": 6
                        },
                        "characters": {
                                   "available": 1,
                                   "collectionURI": "http://gateway.marvel.com/v1/public/comics/47793/characters",
                                   "items": [
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/characters/1009313",
                                       "name": "Superman"
                                     },
                                     {
                                       "resourceURI": "http://gateway.marvel.com/v1/public/characters/1009313",
                                       "name": "SpiderMan"
                                     }
                                   ],
                                   "returned": 1
                        },
                        "stories": {
                          "available": 2,
                          "collectionURI": "http://gateway.marvel.com/v1/public/comics/49888/stories",
                          "items": [
                            {
                              "resourceURI": "http://gateway.marvel.com/v1/public/stories/111836",
                              "name": "cover from Deadpool Minibus (2014)",
                              "type": "cover"
                            },
                            {
                              "resourceURI": "http://gateway.marvel.com/v1/public/stories/111837",
                              "name": "story from Deadpool Minibus (2014)",
                              "type": "interiorStory"
                            }
                          ],
                          "returned": 2
                        },
                        "events": {
                          "available": 0,
                          "collectionURI": "http://gateway.marvel.com/v1/public/comics/49888/events",
                          "items": [],
                          "returned": 0
                        }
                      }
                    ]
                  }
                }""";
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        JsonArray list = jsonObject.get("data").getAsJsonObject().get("results").getAsJsonArray();
        JsonObject object = list.get(0).getAsJsonObject();
        return object;
    }

    @Override
    public int characterComics(int id) throws NoSuchAlgorithmException, CharacterNotFoundException {
        return 10;
    }

    @Override
    public void sendReport(String content) throws IOException {
        System.out.println(content);
        TextInputDialog td = new TextInputDialog();
        td.setHeaderText(languageManager.getResource("emailDescription"));
        td.showAndWait();
    }

    @Override
    public void findCharacter(int id) throws IOException, NoSuchAlgorithmException {
        JsonObject object = characterLoad(id);
        update(object.get("name").getAsString(),id,"character");
        try {
            if(thresholdListener.analyse(characterComics(id))){
                alertFactory.thresholdAlert();
            }
        } catch (CharacterNotFoundException e) {
            alertFactory.normalErrorAlert("this character's comic record is not found");
        }
        viewFactory.buildCharacter(object);
    }

    @Override
    public void findComic(int id) throws IOException, NoSuchAlgorithmException {
        JsonObject object = comicLoad(id);
        update(object.get("title").getAsString(),id,"character");
        viewFactory.buildComic(comicLoad(id));
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
    public List<Hyperlink> getBreadcrumb() {
        return breadcrumb.get();
    }

    @Override
    public DBmanager getDBManager(){
        return this.dBmanager;
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
    public List<String> getLanguages() {
        return languageManager.getLanguages();
    }

    @Override
    public AlertFactory getAlertFactory() {
        return this.alertFactory;
    }

    public void update(String name, int id, String type){
        //System.out.println("build link "+ name + type);
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
        }else{
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
    public ViewFactory getFactory(){
        return viewFactory;
    }
    public LanguageManager getLanguageManager(){
        return languageManager;
    }
}
