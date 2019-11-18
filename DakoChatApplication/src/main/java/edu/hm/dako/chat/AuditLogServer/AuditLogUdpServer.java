package edu.hm.dako.chat.AuditLogServer;

import edu.hm.dako.chat.client.ClientModel;
import edu.hm.dako.chat.client.LogInGuiController;
import edu.hm.dako.chat.common.AuditLogPDU;
import edu.hm.dako.chat.common.AuditLogPduType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class AuditLogUdpServer extends Application implements AuditLogServerInterface  {

	private static Logger log = Logger.getLogger(AuditLogUdpServer.class);
    private Stage stage;
    private AuditLogGUIController lc;
    private AuditLogModel model = new AuditLogModel();

	static final int AUDIT_LOG_SERVER_PORT = 40001;
	static final int DEFAULT_SENDBUFFER_SIZE = 30000;
	static final int DEFAULT_RECEIVEBUFFER_SIZE = 800000;

	static final String auditLogFile = new String("ChatAuditLog.dat");
	protected long counter = 0;

	public static void main(String[] args) {
		PropertyConfigurator.configureAndWatch("log4j.auditLogServer_udp.properties", 60 * 1000);
		System.out.println("AuditLog-UdpServer gestartet, Port: " + AUDIT_LOG_SERVER_PORT);
	}

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(AuditLogGUIController.class.getResource("AuditLogGUI.fxml"));
        Parent root = loader.load();
        AuditLogGUIController lc = loader.getController();
        lc.setAppController(this);
        primaryStage.setTitle("AuditLogServerGUI");
        root.setStyle("-fx-background-color: cornsilk");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 415, 250));
        stage = primaryStage;
        primaryStage.show();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                DatagramSocket socket = new DatagramSocket(AUDIT_LOG_SERVER_PORT);

                while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
                    try {
                        byte[] receive = new byte[DEFAULT_RECEIVEBUFFER_SIZE];

                        DatagramPacket packet = new DatagramPacket(receive, receive.length);
                        socket.receive(packet);
                        ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData());

                        try (ObjectInput in = new ObjectInputStream(bis)) {
                            AuditLogPDU auditLogPDU = (AuditLogPDU) in.readObject();
                            counter++;
                            InetAddress address = packet.getAddress();
                            int port = packet.getPort();

                            System.out.println("*********** received message: *************");
                            System.out.println("user:    " + auditLogPDU.getUserName());
                            System.out.println("address: " + address.toString());
                            System.out.println("port:    " + port);
                            System.out.println("message: " + auditLogPDU.getMessage());
                            System.out.println("type:    " + auditLogPDU.getPduType());
                            System.out.println("*******************************************");

                            setMessageLine(auditLogPDU.getUserName(), auditLogPDU.getPduType(), auditLogPDU.getMessage());

                        } catch (IOException e) {
                            setErrorMessage("AuditLogServer",
                                "Byte Array konnte nicht in ein AuditLogPDU tranformiert werden.",
                                8);
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        setErrorMessage("AuditLogServer",
                            "Beim Empfangen eines PDUs ist ein Fehler aufgetreten.",
                            9);
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

    @Override
    public void setErrorMessage(String sender, String errorMessage, long errorCode) {
        log.debug("errorMessage: " + errorMessage);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Es ist ein Fehler im " + sender + " aufgetreten");
                alert.setHeaderText("Fehlerbehandlung (Fehlercode = " + errorCode + ")");
                alert.setContentText(errorMessage);
                alert.showAndWait();
            }
        });
    }

    @Override
    public void setMessageLine(String user, AuditLogPduType type, String message) {
	    Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getModel().messages.add("[USER] " + user +
                    " | [TYPE] " + type + " | [MESSAGE] " + message);
            }
        });
    }

    public AuditLogModel getModel() {
        return model;
    }
}
