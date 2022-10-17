package app.controllers;

import app.model.ThresholdListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ThresholdController extends ControllerImpl{
    @FXML
    private TextField textField;
    @FXML
    private Button confirmButton;

    private int threshold;
    public void setup(){
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
    @FXML
    private void confirm() throws IOException {
        if (textField.getText().isBlank() || textField.getText().isEmpty()){
            this.engine.getAlertFactory().thresholdErrorAlert();
        }else {
            int threshold = Integer.parseInt(textField.getText());
            if (threshold < 3 || threshold > 50) {
                this.engine.getAlertFactory().thresholdErrorAlert();
            } else {
                ThresholdListener.setThreshold(threshold);
                this.engine.searchLoad();
            }
        }
    }

}
