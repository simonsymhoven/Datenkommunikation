package edu.hm.dako.chat.adminTool;

import edu.hm.dako.chat.common.AuditLogPduType;
import javafx.application.Application;
import javafx.application.Platform;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
    public TableView tableView;

    @FXML
    public TableColumn clientColumn;
    @FXML
    public TableColumn pduColumn;
    @FXML
    public TableColumn timeColumn;
    @FXML
    public TableColumn pduLoginColumn;
    @FXML
    public TableColumn pduLogoutColumn;
    @FXML
    public TableColumn pduFinishColumn;
    @FXML
    public TableColumn pduUndefineColumn;
    @FXML
    public TableColumn pduMessagesColumn;
    @FXML
    public TableColumn logoutColumn;
    @FXML
    public TableColumn loginColumn;

    private String selectedFile;
    private HashMap<String, TableItem> userMap;
    private int pduCounter;
    public ObservableList<TableItem> data = FXCollections.observableArrayList();
    private TreeItem<String> rootItem;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(AdminGUIController.class.getResource("AdminGUI.fxml"));
        Parent root = loader.load();
        stage.setTitle("Administrationsprogramm");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void initialize(){
        // Initialisiert die TreeView mit allen LogFiles
        rootItem = new TreeItem<> ("logs", new ImageView(
            new Image(getClass().getResourceAsStream("resources/folder.png"))));
        rootItem.setExpanded(true);

        File dir = new File(System.getProperty("user.dir") + "/logs/");
        File[] files = dir.listFiles((dire, name) -> name.toLowerCase().endsWith(".csv"));
        for (File file : files) {
            TreeItem<String> item = new TreeItem<> (file.getName(),new ImageView(
                new Image(getClass().getResourceAsStream("resources/file.png"))));
            rootItem.getChildren().add(item);
        }

        treeView.setRoot(rootItem);
        clientColumn.setCellValueFactory(new PropertyValueFactory("clientName"));
        pduLoginColumn.setCellValueFactory(new PropertyValueFactory("pduLoginCounter"));
        pduLogoutColumn.setCellValueFactory(new PropertyValueFactory("pduLogoutCounter"));
        pduMessagesColumn.setCellValueFactory(new PropertyValueFactory("pduMessageCounter"));
        pduFinishColumn.setCellValueFactory(new PropertyValueFactory("pduFinishCounter"));
        pduUndefineColumn.setCellValueFactory(new PropertyValueFactory("pduUndefineCounter"));
        loginColumn.setCellValueFactory(new PropertyValueFactory("loginTime"));
        logoutColumn.setCellValueFactory(new PropertyValueFactory("logoutTime"));
        timeColumn.setCellValueFactory(new PropertyValueFactory("estimatedTime"));
    }

    @FXML
    public void handleMouseEvent() {
        TreeItem<String> selectedItem = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();

        ProgressStage ps = new ProgressStage();
        if (selectedItem.getValue().contains(".csv")) {
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
                            txtAnzahlClients.setText(String.valueOf(userMap.size()));
                            // -1 because of csv header
                            txtAnzahlPDUs.setText(String.valueOf(pduCounter-1));
                            tableView.setItems(data);

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


    public int analyse(TreeItem<String> selectedItem) {
        String path = "/" + selectedItem.getParent().getValue() + "/" + selectedItem.getValue();
        selectedFile = path;

        String line = "";
        String cvsSplitBy = ";";
        userMap = new HashMap<>();
        data.clear();
        pduCounter = 0;

        try {
            FileReader fr = new FileReader(System.getProperty("user.dir") + path);
            BufferedReader br = new BufferedReader(fr);

            while ((line = br.readLine()) != null) {

                String[] pdu = line.split(cvsSplitBy);

                if (pdu[0].equals(AuditLogPduType.LOGIN_REQUEST.getDescription())) {
                    if (!userMap.containsKey(pdu[3])) {
                        TableItem ti = new TableItem(
                            pdu[3],
                            0,
                            1,
                            0,
                            0,
                            0,
                            pdu[4],
                            "",
                            ""
                        );
                        userMap.put(pdu[3], ti);
                    }
                } else if (pdu[0].equals(AuditLogPduType.LOGOUT_REQUEST.getDescription())) {
                    TableItem ti = userMap.get(pdu[3]);
                    ti.setLogoutTime(pdu[4]);
                    ti.setPduLogoutCounter(ti.getPduLogoutCounter() + 1);
                    userMap.put(pdu[3], ti);
                } else if (pdu[0].equals(AuditLogPduType.CHAT_MESSAGE_REQUEST.getDescription())) {
                    TableItem ti = userMap.get(pdu[3]);
                    ti.setPduMessageCounter(ti.getPduMessageCounter() + 1);
                    userMap.put(pdu[3], ti);
                } else if (pdu[0].equals(AuditLogPduType.FINISH_AUDIT_REQUEST.getDescription())) {
                    TableItem ti = userMap.get(pdu[3]);
                    ti.setPduFinishCounter(ti.getPduFinishCounter() + 1);
                    userMap.put(pdu[3], ti);
                } else if (pdu[0].equals(AuditLogPduType.UNDEFINED.getDescription())) {
                    TableItem ti = userMap.get(pdu[3]);
                    ti.setPduUndefineCounter(ti.getPduUndefineCounter() + 1);
                    userMap.put(pdu[3], ti);
                }
                pduCounter++;
            }

            for (Map.Entry<String, TableItem> entry : userMap.entrySet()) {
                String s_logout = entry.getValue().getLogoutTime();
                String s_login = entry.getValue().getLoginTime();
                entry.getValue().setEstimatedTime(calculateTiemDif(s_logout,s_login));
                data.add(entry.getValue());
            }

            br.close();
            return 200;
        } catch (IOException e ) {
            setErrorMessage("AdminGUIController", "Bei der Analayse ist ein Fehler aufgetreten. " +
                "Bitte Vorgang wiederholen", 90);
            e.printStackTrace();
        }
        return 400;
    }

    public String calculateTiemDif(String d1, String d2){
        try {
            Date logout = new SimpleDateFormat(
                "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(d1);
            Date login = new SimpleDateFormat(
                "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(d2);

            long diff = logout.getTime() - login.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            System.out.print(diffDays + " days, ");
            System.out.print(diffHours + " hours, ");
            System.out.print(diffMinutes + " minutes, ");
            System.out.print(diffSeconds + " seconds.");

            return diffHours + ":" + diffMinutes + ":" + diffSeconds;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public void setErrorMessage(String sender, String errorMessage, long errorCode) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Es ist ein Fehler im " + sender + " aufgetreten");
            alert.setHeaderText("Fehlerbehandlung (Fehlercode = " + errorCode + ")");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        });
    }


}
