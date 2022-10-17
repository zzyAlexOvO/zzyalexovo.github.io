package app;

import app.model.*;
import app.view.AlertFactory;
import app.view.ViewFactory;
import app.model.utils.Routes;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class modelTest {
    static HttpHandler httpHandler;
    static HttpResponse response;
    static HttpResponse response2;
    static Routes routes;
    static ViewFactory viewFactory;
    static EmailManager emailManager;
    static BgmManager bgmManager;
    static DBmanager dBmanager;
    static AlertFactory alertFactory;

    @BeforeAll
    public static void init(){
        httpHandler = mock(HttpHandler.class);
        response = mock(HttpResponse.class);
        viewFactory = mock(ViewFactory.class);
        routes = mock(Routes.class);
        emailManager = mock(EmailManager.class);
        bgmManager = mock(BgmManager.class);
        dBmanager = mock(DBmanager.class);
        alertFactory = mock(AlertFactory.class);
        response2 = mock(HttpResponse.class);
    }

    @Test
    public void onlineStartTest() throws IOException {
        Engine oe = new OnlineEngine(viewFactory,alertFactory, emailManager,bgmManager,dBmanager,routes,"publicKey","privateKey");
        oe.start();
        verify(dBmanager,times(1)).createDB();
        verify(dBmanager,times(1)).setupDB();
        verify(bgmManager,times(1)).setup();
        verify(viewFactory,times(1)).buildLoading();
    }

    @Test
    public void bgmTest(){
        Engine oe = new OnlineEngine(viewFactory,alertFactory, emailManager,bgmManager,dBmanager,routes,"publicKey","privateKey");
        oe.playMusic();
        verify(bgmManager,times(1)).play();
        oe.pauseMusic();
        verify(bgmManager,times(1)).pause();
    }

    @Test
    public void languageTest() throws IOException {
        Engine oe = new OnlineEngine(viewFactory,alertFactory, emailManager,bgmManager,dBmanager,routes,"publicKey","privateKey");
        oe.setLanguage("english");
        assertEquals("skip",oe.getResource("skip"));
    }

    @Test
    public void charactersTest() throws NoSuchAlgorithmException {
        Engine oe = new OnlineEngine(viewFactory,alertFactory, emailManager,bgmManager,dBmanager,routes,"publicKey","privateKey");
        when(routes.search(anyString(),anyString(),anyString(),anyString())).thenReturn(response);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("""
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
                }""");
        Map<Integer,String> output = oe.characters("3-D");
        Map<Integer,String> expect = new HashMap(){{put(1011334, "3-D Man");}};
        assertTrue(output.equals(expect));
    }

    @Test
    public void characterTest() throws IOException, NoSuchAlgorithmException {
        Engine oe = new OnlineEngine(viewFactory,alertFactory, emailManager,bgmManager,dBmanager,routes,"publicKey","privateKey");
        when(routes.character(anyInt(),anyString(),anyString(),anyString())).thenReturn(response);
        when(routes.character(anyString(),anyString(),anyString(),anyString())).thenReturn(response);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("""
                {
                  "code": 200,
                  "status": "Ok",
                  "copyright": "© 2022 MARVEL",
                  "attributionText": "Data provided by Marvel. © 2022 MARVEL",
                  "attributionHTML": "<a href=\\"http://marvel.com\\">Data provided by Marvel. © 2022 MARVEL</a>",
                  "etag": "898d5cbc0669476c21e1d026dbd68dbb133248d5",
                  "data": {
                    "offset": 0,
                    "limit": 20,
                    "total": 1562,
                    "count": 20,
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
                }""");
        oe.characterLoad(1);
        verify(routes,times(1)).character(anyInt(),anyString(),anyString(),anyString());
        oe.characterLoad("sb");
        verify(routes,times(1)).character(anyString(),anyString(),anyString(),anyString());

    }

    @Test
    public void comicTest() throws IOException, NoSuchAlgorithmException {
        Engine oe = new OnlineEngine(viewFactory,alertFactory, emailManager,bgmManager,dBmanager,routes,"publicKey","privateKey");
        when(routes.comic(anyInt(),anyString(),anyString(),anyString())).thenReturn(response);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("""
                {
                  "code": 200,
                  "status": "Ok",
                  "copyright": "© 2022 MARVEL",
                  "attributionText": "Data provided by Marvel. © 2022 MARVEL",
                  "attributionHTML": "<a href=\\"http://marvel.com\\">Data provided by Marvel. © 2022 MARVEL</a>",
                  "etag": "973e834d910af263fd5081dacb12a2f7173d053a",
                  "data": {
                    "offset": 0,
                    "limit": 20,
                    "total": 1,
                    "count": 1,
                    "results": [
                      {
                        "id": 49889,
                        "digitalId": 0,
                        "title": "Dexter Down Under (Hardcover)",
                        "issueNumber": 0,
                        "variantDescription": "",
                        "description": "Dexter returns in an all-new, all-Australian, blood-soaked adventure written by his creator, Jeff Lindsay! Dexter Morgan isn't just Miami's No. 1 forensic blood-splatter expert - he's also a serial killer who targets other serial killers! But when Dexter travels Down Under on the trail of a new murderer, he quickly discovers that sharks aren't Australia's only deadly predator! Who is setting up illegal hunting safaris in the Outback, and are they targeting more than big game? Dexter investigates as only he can, but soon discovers that he isn't the hunter - he's the prey! Now Dexter is trapped in a private preserve where humans are in the crosshairs! Will this one end with a bang? Under the hot Australian sun, Dexter's Dark Passenger is given free reign, blood will flow, and the guilty will not go unpunished! Collecting DEXTER DOWN UNDER #1-5.",
                        "modified": "2014-08-01T13:44:12-0400",
                        "isbn": "978-0-7851-5450-1",
                        "upc": "",
                        "diamondCode": "",
                        "ean": "9780785 154501 52499",
                        "issn": "",
                        "format": "Hardcover",
                        "pageCount": 112,
                        "textObjects": [
                          {
                            "type": "issue_solicit_text",
                            "language": "en-us",
                            "text": "Dexter returns in an all-new, all-Australian, blood-soaked adventure written by his creator, Jeff Lindsay! Dexter Morgan isn't just Miami's No. 1 forensic blood-splatter expert - he's also a serial killer who targets other serial killers! But when Dexter travels Down Under on the trail of a new murderer, he quickly discovers that sharks aren't Australia's only deadly predator! Who is setting up illegal hunting safaris in the Outback, and are they targeting more than big game? Dexter investigates as only he can, but soon discovers that he isn't the hunter - he's the prey! Now Dexter is trapped in a private preserve where humans are in the crosshairs! Will this one end with a bang? Under the hot Australian sun, Dexter's Dark Passenger is given free reign, blood will flow, and the guilty will not go unpunished! Collecting DEXTER DOWN UNDER #1-5."
                          }
                        ],
                        "resourceURI": "http://gateway.marvel.com/v1/public/comics/49889",
                        "urls": [
                          {
                            "type": "detail",
                            "url": "http://marvel.com/comics/collection/49889/dexter_down_under_hardcover?utm_campaign=apiRef&utm_source=a4df867c5bad9122e43258cb451170fe"
                          }
                        ],
                        "series": {
                          "resourceURI": "http://gateway.marvel.com/v1/public/series/18746",
                          "name": "Dexter Down Under (2014)"
                        },
                        "variants": [],
                        "collections": [],
                        "collectedIssues": [],
                        "dates": [
                          {
                            "type": "onsaleDate",
                            "date": "2014-08-06T00:00:00-0400"
                          },
                          {
                            "type": "focDate",
                            "date": "2014-07-22T00:00:00-0400"
                          }
                        ],
                        "prices": [
                          {
                            "type": "printPrice",
                            "price": 24.99
                          }
                        ],
                        "thumbnail": {
                          "path": "http://i.annihil.us/u/prod/marvel/i/mg/c/70/53dbcf8f712e2",
                          "extension": "jpg"
                        },
                        "images": [
                          {
                            "path": "http://i.annihil.us/u/prod/marvel/i/mg/c/70/53dbcf8f712e2",
                            "extension": "jpg"
                          }
                        ],
                        "creators": {
                          "available": 3,
                          "collectionURI": "http://gateway.marvel.com/v1/public/comics/49889/creators",
                          "items": [
                            {
                              "resourceURI": "http://gateway.marvel.com/v1/public/creators/11931",
                              "name": "Jeff Lindsay",
                              "role": "writer"
                            },
                            {
                              "resourceURI": "http://gateway.marvel.com/v1/public/creators/11309",
                              "name": "Mike Del Mundo",
                              "role": "penciller (cover)"
                            },
                            {
                              "resourceURI": "http://gateway.marvel.com/v1/public/creators/11560",
                              "name": "Dalibor Talajic",
                              "role": "artist"
                            }
                          ],
                          "returned": 3
                        },
                        "characters": {
                          "available": 0,
                          "collectionURI": "http://gateway.marvel.com/v1/public/comics/49889/characters",
                          "items": [],
                          "returned": 0
                        },
                        "stories": {
                          "available": 2,
                          "collectionURI": "http://gateway.marvel.com/v1/public/comics/49889/stories",
                          "items": [
                            {
                              "resourceURI": "http://gateway.marvel.com/v1/public/stories/111838",
                              "name": "cover from Dexter: TBD (2014)",
                              "type": "cover"
                            },
                            {
                              "resourceURI": "http://gateway.marvel.com/v1/public/stories/111839",
                              "name": "story from Dexter: TBD (2014)",
                              "type": "interiorStory"
                            }
                          ],
                          "returned": 2
                        },
                        "events": {
                          "available": 0,
                          "collectionURI": "http://gateway.marvel.com/v1/public/comics/49889/events",
                          "items": [],
                          "returned": 0
                        }
                      }
                    ]
                  }
                }""");
        oe.comicLoad(0);
        verify(routes,times(1)).comic(anyInt(),anyString(),anyString(),anyString());
    }

    @Test
    public void findCharacterTest() throws IOException, NoSuchAlgorithmException {
        Engine oe = new OnlineEngine(viewFactory,alertFactory, emailManager,bgmManager,dBmanager,routes,"publicKey","privateKey");
        when(routes.characterComics(anyInt(),anyString(),anyString(),anyString())).thenReturn(response2);
        when(response2.statusCode()).thenReturn(200);
        when(response2.body()).thenReturn("{\"data\":{\"total\":20}}");
        oe.findCharacter(1);
        verify(dBmanager,times(1)).checkCache(eq("character"),anyInt());
    }

    @Test
    public void findComicTest() throws IOException, NoSuchAlgorithmException {
        Engine oe = new OnlineEngine(viewFactory,alertFactory, emailManager,bgmManager,dBmanager,routes,"publicKey","privateKey");
        oe.findComic(0);
        verify(dBmanager,times(1)).checkCache(eq("comic"),anyInt());
    }
    @Test
    public void thresholdUnitTest(){
        JsonObject object= new JsonParser().parse( """
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
                      }""").getAsJsonObject();
        ThresholdListener thresholdListener = new ThresholdListener();
        ThresholdListener.setThreshold(4);
        assertTrue(thresholdListener.analyse(11));
        ThresholdListener.setThreshold(22);
        assertTrue(!thresholdListener.analyse(11));
    }
}
