package app.view;

import app.model.LanguageManager;
import javafx.scene.control.Alert;

public class AlertFactory {
    private LanguageManager languageManager;
    public AlertFactory(LanguageManager languageManager){
        this.languageManager = languageManager;
    }
    public void thresholdAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(languageManager.getResource("PopularityHeader"));
        alert.setContentText(languageManager.getResource("PopularityContext"));
        alert.showAndWait();
    }
    public void thresholdErrorAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(languageManager.getResource("InputErrorHeader"));
        alert.setContentText(languageManager.getResource("InputErrorContext"));
        alert.showAndWait();
    }
    public void normalErrorAlert(String body){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(languageManager.getResource("Sorry"));
        alert.setContentText(body);
        alert.showAndWait();
    }
}
