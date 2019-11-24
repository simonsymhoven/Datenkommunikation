package edu.hm.dako.chat.AuditLogServer;

import edu.hm.dako.chat.common.AuditLogPDU;
import edu.hm.dako.chat.common.AuditLogPduType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;

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

    @Override
    public void start(Stage primaryStage) throws Exception {
        PropertyConfigurator.configureAndWatch("log4j.auditLogServer_udp.properties", 60 * 1000);
        System.out.println("AuditLog-UdpServer gestartet, Port: " + AUDIT_LOG_SERVER_PORT);
        getModel().serverAddress = "127.0.0.1";

        FXMLLoader loader = new FXMLLoader(AuditLogGUIController.class.getResource("AuditLogGUI.fxml"));
        Parent root = loader.load();
        AuditLogGUIController lc = loader.getController();
        lc.setAppController(this);
        primaryStage.setTitle("AuditLogServerGUI");
        root.setStyle("-fx-background-color: cornsilk");
        primaryStage.setOpacity(0.7);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 800, 400));
        stage = primaryStage;
        primaryStage.show();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                DatagramSocket socket = new DatagramSocket(AUDIT_LOG_SERVER_PORT);

                while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
                    try {
                        byte[] receive = new byte[64000];

                        DatagramPacket packet = new DatagramPacket(receive, receive.length);
                        socket.receive(packet);
                        ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData());

                        try (ObjectInput in = new ObjectInputStream(bis)) {
                            AuditLogPDU auditLogPDU = (AuditLogPDU) in.readObject();
                            counter++;
                            System.out.println("*********** received message: *************");
                            System.out.println("user:    " + auditLogPDU.getUserName());
                            System.out.println("address: " + packet.getAddress());
                            System.out.println("port:    " + packet.getPort());
                            System.out.println("message: " + auditLogPDU.getMessage());
                            System.out.println("type:    " + auditLogPDU.getPduType());
                            System.out.println("time:    " + new Timestamp(auditLogPDU.getAuditTime()));
                            System.out.println("*******************************************");


                            setMessageLine(auditLogPDU.getUserName(), auditLogPDU.getPduType(),
                                auditLogPDU.getMessage(), auditLogPDU.getAuditTime(), packet.getAddress(), packet.getPort());

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
    public void stop(){
        try {
            stage.hide();
            String message = "AuditLogServerGUI beendet, Gesendete AuditLog-Saetze: " + counter;
            System.out.println(message);
            writeDataToLogFile(message);
        } catch (Exception e) {
            System.out.println("Fehler beim Schliessen der AuditLogServerGUI");
        }
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
    public void setMessageLine(String user, AuditLogPduType type, String message, Long auditLogTime,
                               InetAddress address, int port) {
	    Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String data = "[TIME] " + new Timestamp(auditLogTime)
                         + " | [SERVER ] " + address + "/" + port
                         + " | [USER] " + user
                         + " | [TYPE] " + type
                         + " | [MESSAGE] " + message + "\n";
                getModel().messages.add(data);
                writeDataToLogFile(data);
            }
        });
    }

    public AuditLogModel getModel() {
        return model;
    }


    @Override
    public void writeDataToLogFile(String data) {
        String path = System.getProperty("user.dir");
        String fileName = auditLogFile;
        String dirName = "/out/LogFiles/";
        File file = new File(path + dirName + "/" + fileName);
        File dir = new File(path + dirName);
        //Path path = Paths.get(System.getProperty("user.dir") + "/out/LogFiles/" + auditLogFile);
        if (!file.exists()) {
            try {
                if (!dir.exists()){
                    dir.mkdir();
                }
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                setErrorMessage("AuditLogServer",
                    "Ordner ./out/LogFiles/" + auditLogFile + "konnte nicht angelegt werden.", 99);
            }
        }

        try {
            Files.write(Paths.get(System.getProperty("user.dir") + "/out/LogFiles/"
                + auditLogFile), data.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
            setErrorMessage("AuditLogServer",
                "Ein Fehler beim Schreiben in das Log-File ist aufgetreten.", 98);
        }
    }
}
