package edu.hm.dako.chat.AuditLogServer;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class AuditLogGUIController {

    @FXML
    public ScrollPane sp;
    public ListView lv;
    public Label headerLabel;

    public static Stage primaryStage;
    private AuditLogUdpServer appController;


    /**
     * Setzt den App-Controller
     * @param appController
     */
    public void setAppController(AuditLogUdpServer appController) {
        this.appController = appController;
        lv.setItems(appController.getModel().messages);
        lv.scrollTo(appController.getModel().messages.size());
        headerLabel.setText("Verbindung zum Server mit der IP-Adresse " + appController.getModel().serverAddress);
        lv.maxWidthProperty().bind(sp.widthProperty().subtract(2));
        lv.minWidthProperty().bind(sp.widthProperty().subtract(2));
        lv.maxHeightProperty().bind(sp.heightProperty().subtract(2));
        lv.minHeightProperty().bind(sp.heightProperty().subtract(2));


    }

}
