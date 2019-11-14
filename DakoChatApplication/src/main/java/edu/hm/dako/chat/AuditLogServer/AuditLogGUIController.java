package edu.hm.dako.chat.AuditLogServer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AuditLogGUIController {

    @FXML
    public ScrollPane sp;
    public ListView lv;

    public static Stage primaryStage;

    /**
     * startet die GUI
     */
    public static void start() throws IOException {
        Stage stage = new Stage();
        primaryStage = stage;
        FXMLLoader loader = new FXMLLoader(AuditLogGUIController.class.getResource("AuditLogGUI.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("AuditLogServerGUI");
        root.setStyle("-fx-background-color: cornsilk");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 415, 250));
        primaryStage.show();
    }


    /**
     * stoppt die GUI
     */
    public static void stop() {
        System.out.println("AuditLogServerGui beendet.");
        primaryStage.hide();
    }

    /**
     * wird impliziet aufgerufen, sobald GUI geladen wird
     */
    @FXML
    public void initialize(){
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        lv.maxWidthProperty().bind(sp.widthProperty().subtract(2));
        lv.minWidthProperty().bind(sp.widthProperty().subtract(2));
        lv.maxHeightProperty().bind(sp.heightProperty().subtract(2));
        lv.minHeightProperty().bind(sp.heightProperty().subtract(2));

        ObservableList<String> items = FXCollections.observableArrayList (
            "1. Nachricht", "2. Nachricht", "3. Nachricht", "4. Nachricht");
        lv.setItems(items);
    }
}
