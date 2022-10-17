package app;

import app.model.*;
import app.model.utils.Routes;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;


public class MarvelApp extends Application {
    public static final String publicKey = System.getenv("INPUT_API_KEY");
    public static final String privateKey = System.getenv("INPUT_API_APP_ID");
    public static final String fromEmail = System.getenv("SENDGRID_API_EMAIL");
    public static final String emailKey = System.getenv("SENDGRID_API_KEY");
    private static String[] arg;
    private Engine engine;
    @Override
    public void start(Stage stage) throws IOException {
        if (arg[0].equals("online") && arg[1].equals("online")) {
            this.engine = new OnlineEngine(stage, new Routes(), publicKey, privateKey,fromEmail,emailKey);
        }else if (arg[0].equals("offline") && arg[1].equals("offline")){
            this.engine = new DummyEngine(stage);
        }else if (arg[0].equals("offline") && arg[1].equals("online")){
            this.engine = new DummyOutEngine(stage,fromEmail,emailKey);
        }else if (arg[0].equals("online") && arg[1].equals("offline")){
            this.engine = new DummyInEngine(stage, new Routes(), publicKey, privateKey);
        }else{
            System.out.println(arg);
        }
        engine.start();
        stage.show();
    }
    public static void main(String[] args) {
        arg = args;
        launch();
    }
}
