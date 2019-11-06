package edu.hm.dako.chat.AuditLogServer;


import edu.hm.dako.chat.client.LogInGuiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AuditLogFxGUI extends Application {
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AuditLogLoginGUI.fxml"));
        Parent root = loader.load();
        AuditLogLoginGUIController lc = (AuditLogLoginGUIController) loader.getController();
        lc.setAppController(this);
        primaryStage.setTitle("Anmelden");
        primaryStage.setScene(new Scene(root, 280, 320));
        root.setStyle("-fx-background-color: cornsilk");
        stage = primaryStage;
        primaryStage.show();

    }
}
