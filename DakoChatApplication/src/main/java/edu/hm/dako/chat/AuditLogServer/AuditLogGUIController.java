package edu.hm.dako.chat.AuditLogServer;


import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class AuditLogGUIController {

    @FXML
    public ScrollPane sp;
    public ListView lv;

    public static Stage primaryStage;
    private AuditLogUdpServer appController;


    /**
     * stoppt die GUI
     */
    public static void stop() {
        System.out.println("AuditLogServerGui beendet.");
        primaryStage.hide();
    }

    public void setAppController(AuditLogUdpServer appController) {
        this.appController = appController;
        lv.setItems(appController.getModel().messages);
        lv.scrollTo(appController.getModel().messages.size());
        lv.maxWidthProperty().bind(sp.widthProperty().subtract(2));
        lv.minWidthProperty().bind(sp.widthProperty().subtract(2));
        lv.maxHeightProperty().bind(sp.heightProperty().subtract(2));
        lv.minHeightProperty().bind(sp.heightProperty().subtract(2));


    }

}
