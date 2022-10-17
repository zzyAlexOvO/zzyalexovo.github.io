package app.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SearchController extends ControllerImpl{
    @FXML
    Label search_description;
    @FXML
    Button search;
    @FXML
    Button clearCache;
    @FXML
    FlowPane breadcrumbArea;
    @FXML
    TableView table;
    public class Character{
        private int id;
        private String name;
        public Character(int id, String name){
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }

    @FXML
    void searchEvent() throws ExecutionException, InterruptedException {
        TextField textField = (TextField) scene.lookup("#inputPattern");
        String pattern = textField.getText();
        Task task = new Task<>() {
            public Object call() {
                Map<Integer, String> characters = null;
                try {
                    characters = engine.characters(pattern);
                    fill(characters);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                return characters;
            }
        };

        new Thread(task).start();
    }

    void fill(Map<Integer,String> characters){

        table.getColumns().clear();

        ObservableList<Character> tvObservableList = FXCollections.observableArrayList();
        for (Map.Entry<Integer,String> character: characters.entrySet()){
            tvObservableList.add(new Character( character.getKey(), character.getValue()));
        }
        table.setItems(tvObservableList);
        TableColumn<Character, Integer> col1 = new TableColumn<>("id");
        col1.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Character, String> col2 = new TableColumn<>(engine.getResource("name"));
        col2.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().addAll(col1,col2);

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
                                engine.getDBManager().insertItem("character",data.name, data.id);
                                engine.findCharacter(data.id);
                            } catch (IOException | NoSuchAlgorithmException e) {
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

    public void fillBreadcrumb(List<Hyperlink> links){
        initLanguages();
        setMenuText();
        setText();
        if (links == null || links.size() == 0){
            System.out.println("Null, return");
            return;
        }else{
            for (Hyperlink link: links){
                breadcrumbArea.getChildren().add(link);
                breadcrumbArea.getChildren().add(new Text(">"));
            }
        }
    }

    @FXML
    public void clearCache(){
        engine.clearCache();
    }

    @Override
    public void setText(){
        search_description.setText(engine.getResource(search_description.getText()));
        search.setText(engine.getResource(search.getText()));
        clearCache.setText(engine.getResource(clearCache.getText()));
    }
}
