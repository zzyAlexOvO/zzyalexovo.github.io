package app.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


import java.io.IOException;
import java.util.Timer;


public class LoadingController extends ControllerImpl{
    Timeline task;
    @FXML
    Button skip;
    @FXML
    ImageView loadingImage;
    @FXML
    ProgressBar progressBar;
    public void setup(){
        initLanguages();
        setMenuText();
        setText();

        loadingImage.setImage(new Image("loading.jpg"));

        task = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(progressBar.progressProperty(), 0)
                ),
                new KeyFrame(
                        Duration.seconds(15),
                        new KeyValue(progressBar.progressProperty(), 1)
                )
        );
        task.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    backEvent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        task.playFromStart();
    }
    @Override
    public void backEvent() throws IOException {
        task.stop();
        engine.thresholdLoad();
    }

    @Override
    public void setText(){
        skip.setText(engine.getResource(skip.getText()));
    }
}
