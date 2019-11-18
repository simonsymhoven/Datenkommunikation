package edu.hm.dako.chat.AuditLogServer;

import edu.hm.dako.chat.client.ClientFxGUI;
import edu.hm.dako.chat.common.AuditLogPDU;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import java.io.*;
import java.net.*;

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
    public void initialize() {
        System.out.println("AuditLogServerGui wird initialisiert.");

        lv.maxWidthProperty().bind(sp.widthProperty().subtract(2));
        lv.minWidthProperty().bind(sp.widthProperty().subtract(2));
        lv.maxHeightProperty().bind(sp.heightProperty().subtract(2));
        lv.minHeightProperty().bind(sp.heightProperty().subtract(2));

        ObservableList<String> items = FXCollections.observableArrayList("AuditLog Server erreichbar, Nachrichten werden geloggt:");
        lv.setItems(items);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                DatagramSocket socket = AuditLogUdpServer.createAuditLogUdpServer();

                while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
                    try {
                        byte[] receive = new byte[2048];

                        DatagramPacket packet = new DatagramPacket(receive, receive.length);
                        socket.receive(packet);
                        ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData());

                        try (ObjectInput in = new ObjectInputStream(bis)) {
                            AuditLogPDU auditLogPDU = (AuditLogPDU) in.readObject();
                            InetAddress address = packet.getAddress();
                            int port = packet.getPort();

                            System.out.println("*********** received message: *************");
                            System.out.println("user:    " + auditLogPDU.getUserName());
                            System.out.println("address: " + address.toString());
                            System.out.println("port:    " + port);
                            System.out.println("message: " + auditLogPDU.getMessage());
                            System.out.println("type:    " + auditLogPDU.getPduType());
                            System.out.println("*******************************************");



                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    lv.getItems().add("[USER] " + auditLogPDU.getUserName() +
                                        " | [TYPE] " + auditLogPDU.getPduType() + " | [MESSAGE] " + auditLogPDU.getMessage());
                                    lv.scrollTo(lv.getItems().size()-1);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                       e.printStackTrace();
                    }
                }
                return null;
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

}
