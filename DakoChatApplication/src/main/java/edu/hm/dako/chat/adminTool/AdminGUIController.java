package edu.hm.dako.chat.adminTool;

import edu.hm.dako.chat.AuditLogServer.AuditLogGUIController;
import edu.hm.dako.chat.common.AuditLogPDU;
import edu.hm.dako.chat.common.SystemConstants;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.*;

public class AdminGUIController extends Application {

    @FXML
    public TreeView treeView;
    @FXML
    public TextField txtSelectedFile;
    @FXML
    public TextField txtAnzahlClients;
    @FXML
    public TextField txtAnzahlPDUs;
    @FXML
    public TextField txtServerSession;
    @FXML
    public TableView tableView;

    public String selectedFile;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(AdminGUIController.class.getResource("AdminGUI.fxml"));
        Parent root = loader.load();
        stage.setTitle("Administrationsprogramm");
        stage.setResizable(false);
        stage.setScene(new Scene(root, 800, 400));
        stage.show();
    }

    @FXML
    public void initialize(){
        // Initialisiert die TreeView mit allen LogFiles
        TreeItem<String> rootItem = new TreeItem<> ("Log-Files", new ImageView(
            new Image(getClass().getResourceAsStream("resources/folder.png"))));
        rootItem.setExpanded(true);

        TreeItem<String> TCPItem = new TreeItem<> ("TCP-LogFiles", new ImageView(
            new Image(getClass().getResourceAsStream("resources/folder.png"))));
        rootItem.getChildren().add(TCPItem);
        TreeItem<String> UDPItem = new TreeItem<> ("UDP-LogFiles", new ImageView(
            new Image(getClass().getResourceAsStream("resources/folder.png"))));
        rootItem.getChildren().add(UDPItem);

        File tcpDir = new File(System.getProperty("user.dir") + "/out/TCP-LogFiles/");
        File[] tcpFiles = tcpDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".dat"));
        for (File tcpFile : tcpFiles) {
            TreeItem<String> item = new TreeItem<> (tcpFile.getName(),new ImageView(
                new Image(getClass().getResourceAsStream("resources/file.png"))));
            TCPItem.getChildren().add(item);
        }

        File udpDir = new File(System.getProperty("user.dir") + "/out/TCP-LogFiles/");
        File[] udpFiles = udpDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".dat"));
        for (File udpFile : udpFiles) {
            TreeItem<String> item = new TreeItem<> (udpFile.getName(),new ImageView(
                new Image(getClass().getResourceAsStream("resources/file.png"))));
            UDPItem.getChildren().add(item);
        }

        treeView.setRoot(rootItem);
    }

    @FXML
    public void handleMouseEvent() {
        TreeItem<String> selectedItem = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();

        ProgressStage ps = new ProgressStage();
        if (selectedItem.getValue().contains(".dat")) {

            // Thread für Progress Indicator
            Runnable progressTask = new Runnable() {
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ps.startProgress();
                        }
                    });

                }
            };
            new Thread(progressTask).start();
            // Thread für Analyse
            Runnable r = new Runnable() {
                int returnValue = 2;

                public void run() {
                    while (returnValue == 2) {
                        returnValue = analyse(selectedItem);
                    }
                    Platform.runLater(() -> {
                        ps.stopProgress();
                        if (returnValue == 200) {
                            txtSelectedFile.setText(selectedFile);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Es ist ein Fehler aufgetreten!");
                            alert.setHeaderText("Fehlerbehandlung (Fehlercode = 95)");
                            alert.setContentText("Bei der Analyse des Log-Files ist ein Fehler aufgetreten.");
                            alert.showAndWait();
                        }
                    });
                }
            };

            Thread cdThread = new Thread(r);
            cdThread.setName("Analyse");
            cdThread.start();
        }

    }


    public int analyse(TreeItem<String> selectedItem){
        String path = "/out/" + selectedItem.getParent().getValue() + "/" + selectedItem.getValue();
        selectedFile = path;
        try {
            FileReader fr = new FileReader(System.getProperty("user.dir") + path);
            BufferedReader br = new BufferedReader(fr);
            // TODO: Analyse des Log-Files
            br.close();
            return 200;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 400;
    }


}
