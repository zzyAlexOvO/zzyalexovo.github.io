package app.model;

import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import java.io.IOException;

public class DummyOutEngine extends DummyEngine {
    private String fromEmail;
    private EmailManager emailManager;
    public DummyOutEngine(Stage stage, String fromEmail,String emailKey){
        super(stage);
        this.fromEmail = fromEmail;
        this.emailManager = new EmailManager(emailKey);
    }

    @Override
    public void sendReport(String content) throws IOException {
        System.out.println(content);
        TextInputDialog td = new TextInputDialog();
        td.setHeaderText(super.getLanguageManager().getResource("emailDescription"));
        td.showAndWait();
        emailManager.send(fromEmail,td.getResult(),"Report",content);
    }
}
