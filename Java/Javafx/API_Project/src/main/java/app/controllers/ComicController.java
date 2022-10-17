package app.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ComicController extends ControllerImpl{
    @FXML
    Button backButton;
    @FXML
    Label list;
    @FXML
    Text name;
    @FXML
    TableView table;
    public class Character{
        private String name;
        private int id;
        public Character(String name,int id){
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
    public void setText(){
        backButton.setText(engine.getResource(backButton.getText()));
        list.setText(engine.getResource(list.getText()));
        name.setText(engine.getResource(name.getText()));
    }
    public void setup(JsonObject object) {
        initLanguages();
        setMenuText();
        setText();
        name.setText(object.get("title").getAsString());

        JsonArray characterArray= object.get("characters").getAsJsonObject().get("items").getAsJsonArray();

        ObservableList<Character> tvObservableList = FXCollections.observableArrayList();
        for (JsonElement character: characterArray){
            String url = character.getAsJsonObject().get("resourceURI").getAsString();
            String[] list = url.split("/");
            int id = Integer.parseInt(list[list.length - 1]);
            tvObservableList.add(new Character(character.getAsJsonObject().get("name").getAsString(),id));
        }
        table.setItems(tvObservableList);
        TableColumn<Character, String> col1 = new TableColumn<>(engine.getResource("name"));
        col1.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().addAll(col1);

        TableColumn<Character, Void> colBtn = new TableColumn(engine.getResource("detail"));

        Callback<TableColumn<Character, Void>, TableCell<Character, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Character, Void> call(final TableColumn<Character, Void> param) {
                final TableCell<Character, Void> cell = new TableCell<>() {

                    private final Button btn = new Button(engine.getResource("view"));

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Character data = getTableView().getItems().get(getIndex());
                            try {
                                engine.getDBManager().insertItem("character",data.name,data.id);
                                engine.findCharacter(data.id);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (NoSuchAlgorithmException e) {
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
}
