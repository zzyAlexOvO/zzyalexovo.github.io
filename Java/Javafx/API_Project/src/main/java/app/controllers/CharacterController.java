package app.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class CharacterController extends ControllerImpl{
    private String reportContent;
    private JsonObject object;
    @FXML
    Label name;
    @FXML
    Button back;
    @FXML
    Label comics;
    @FXML
    Button sendReport;
    @FXML
    ImageView image;
    @FXML
    TableView table;
    public class Comic{
        private String name;
        private int id;
        public Comic(int id, String name){
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }

    @Override
    public void setText(){
        name.setText(engine.getResource(name.getText()));
        back.setText(engine.getResource(back.getText()));
        comics.setText(engine.getResource(comics.getText()));
        sendReport.setText(engine.getResource(sendReport.getText()));
    }
    public void setup(JsonObject object){
        initLanguages();
        setMenuText();
        setText();
        this.object = object;
        name.setText(object.get("name").getAsString());

        JsonObject thumbnail = object.get("thumbnail").getAsJsonObject();
        String imagePath = thumbnail.get("path").getAsString() + "." + thumbnail.get("extension").getAsString();
        System.out.println("image path: " + imagePath);

        Task task = new Task<>() {
            public Object call() {
                image.setImage(new Image(imagePath));
                return null;
            }
        };
        new Thread(task).start();

        JsonArray comicArray= object.get("comics").getAsJsonObject().get("items").getAsJsonArray();

        ObservableList<Comic> tvObservableList = FXCollections.observableArrayList();
        for (JsonElement comic: comicArray){
            String url = comic.getAsJsonObject().get("resourceURI").getAsString();
            String[] list = url.split("/");
            int id = Integer.parseInt(list[list.length - 1]);
            tvObservableList.add(new Comic(id,comic.getAsJsonObject().get("name").getAsString()));
        }

        table.setItems(tvObservableList);
        TableColumn<Comic, String> col1 = new TableColumn<>(engine.getResource("name"));
        col1.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().addAll(col1);

        TableColumn<Comic, Void> colBtn = new TableColumn(engine.getResource("detail"));

        Callback<TableColumn<Comic, Void>, TableCell<Comic, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Comic, Void> call(final TableColumn<Comic, Void> param) {
                final TableCell<Comic, Void> cell = new TableCell<>() {

                    private final Button btn = new Button(engine.getResource("view"));

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Comic data = getTableView().getItems().get(getIndex());
                            try {
                                engine.getDBManager().insertItem("comic",data.name,data.id);
                                engine.findComic(data.id);
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        table.getColumns().add(colBtn);

    }
    public void sendReport() throws IOException {
        reportContent = "<h1>name: " + object.get("name").getAsString() + "</h1>"+ "<h1>comics: </h1>" + "<p>";
        JsonArray comicArray= object.get("comics").getAsJsonObject().get("items").getAsJsonArray();
        for (JsonElement comic: comicArray){
            reportContent += comic.getAsJsonObject().get("name").getAsString() + ";" + "<br>";
        }
        reportContent += "</p>" + "<h1>stories: </h1>" + "<p>";
        JsonArray storyArray = object.get("stories").getAsJsonObject().get("items").getAsJsonArray();
        for (JsonElement story: storyArray){
            reportContent += story.getAsJsonObject().get("name").getAsString() + ";" + "<br>";
        }
        reportContent += "</p>" + "<h1>events: </h1>" + "<p>";
        JsonArray eventArray = object.get("events").getAsJsonObject().get("items").getAsJsonArray();
        if (eventArray.isEmpty()){
            reportContent += "null";
        }
        for (JsonElement event: eventArray){
            reportContent += event.getAsJsonObject().get("name").getAsString() + ";"  + "<br>";
        }
        reportContent += "</p>";
        engine.sendReport(reportContent);
    }
}
