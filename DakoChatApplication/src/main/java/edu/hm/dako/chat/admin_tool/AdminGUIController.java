package edu.hm.dako.chat.admin_tool;

import edu.hm.dako.chat.common.AuditLogPduType;
import edu.hm.dako.chat.common.ExceptionHandler;
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
import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class AdminGUIController extends Application {
    private static final Logger log = Logger.getLogger(AdminGUIController.class);
    @FXML
    private TreeView<String> treeView;
    @FXML
    private TextField txtSelectedFile;
    @FXML
    private TextField txtClientsCounter;
    @FXML
    private TextField txtPDUCounter;
    @FXML
    private TableView<TableItem> tableView;
    @FXML
    private TableColumn<TableItem, String> clientColumn;
    @FXML
    private TableColumn<TableItem, String> pduMessagesColumn;
    @FXML
    private TableColumn<TableItem, String> logoutColumn;
    @FXML
    private TableColumn<TableItem, String> loginColumn;
    @FXML
    private TableColumn<TableItem, Integer> timeColumn;
    @FXML
    private TableColumn<TableItem, Integer> pduLoginColumn;
    @FXML
    private TableColumn<TableItem, Integer> pduLogoutColumn;
    @FXML
    private TableColumn<TableItem, Integer> pduFinishColumn;
    @FXML
    private TableColumn<TableItem, Integer> pduUndefineColumn;
    private String selectedFile;

    /**
     * userMap stores for each client the corresponding TableItem which is
     * filled with certain values
     */
    private HashMap<String, TableItem> userMap;
    private int pduCounter;

    /**
     * data is used to set the tableView, contains all TableItems of userMap
     */
    private final ObservableList<TableItem> data = FXCollections.observableArrayList();

    /**
     * entry point, calls implicit the {@link AdminGUIController#start(Stage stage)},
     * @param args run params
     */
    public static void main(String[] args) {
        launch(args);
    }


    /**
     * load the fxml sheet and shows the stage to user
     * @param stage stage to show
     */
    @Override
    public void start(Stage stage) {
        FXMLLoader loader = new FXMLLoader(AdminGUIController.class.getResource("AdminGUI.fxml"));
        try {
            Parent root = loader.load();
            stage.setTitle("Administrationsprogramm");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            log.error(e);
        }

    }


    /**
     * implicit called function if fxml sheet to this Controller is loaded
     *
     * sets the rootItem of  {@link AdminGUIController#treeView} and adds all files of
     * the log/ directory.
     *
     * for each column of {@link AdminGUIController#tableView} the CellValueFactory will be set
     * ( PropertyValueFactory has to be the same as the value of
     * {@link TableItem#TableItem(String, int, int, int, int, int, String, String, String)})
     */
    @FXML
    public void initialize(){
        TreeItem<String> rootItem = new TreeItem<>("logs", new ImageView(
            new Image(getClass().getResourceAsStream("resources/folder.png"))));
        rootItem.setExpanded(true);

        File dir = new File(System.getProperty("user.dir") + "/logs/");
        File[] files = dir.listFiles((dire, name) -> name.toLowerCase().endsWith(".csv"));
        if (files != null) {
            for (File file : files) {
                TreeItem<String> item = new TreeItem<> (file.getName(),new ImageView(
                    new Image(getClass().getResourceAsStream("resources/file.png"))));
                rootItem.getChildren().add(item);
            }
        }

        treeView.setRoot(rootItem);
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        pduLoginColumn.setCellValueFactory(new PropertyValueFactory<>("pduLoginCounter"));
        pduLogoutColumn.setCellValueFactory(new PropertyValueFactory<>("pduLogoutCounter"));
        pduMessagesColumn.setCellValueFactory(new PropertyValueFactory<>("pduMessageCounter"));
        pduFinishColumn.setCellValueFactory(new PropertyValueFactory<>("pduFinishCounter"));
        pduUndefineColumn.setCellValueFactory(new PropertyValueFactory<>("pduUndefineCounter"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("loginTime"));
        logoutColumn.setCellValueFactory(new PropertyValueFactory<>("logoutTime"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("estimatedTime"));
    }

    /**
     * this method is called if one of the treeView items is selected.
     *
     * one thread for the load animation and one for the log analysis
     */
    @FXML
    public void handleMouseEvent() {
        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();

        ProgressStage ps = new ProgressStage();
        if (selectedItem.getValue().contains(".csv")) {

            // Thread für Progress Indicator
            Runnable progressTask = () -> Platform.runLater(ProgressStage::startProgress);
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
                            txtClientsCounter.setText(String.valueOf(userMap.size()));
                            // -1 because of csv header
                            txtPDUCounter.setText(String.valueOf(pduCounter-1));
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


    /**
     * analyse each row of the csv file and handle the information, depends on the PDU type
     *
     * @param selectedItem selected Item from TreeView
     * @return int 200 for success, 400 else
     */
    private int analyse(TreeItem<String> selectedItem) {
        String path = "/" + selectedItem.getParent().getValue() + "/" + selectedItem.getValue();
        selectedFile = path;

        String line;
        String cvsSplitBy = ";";
        userMap = new HashMap<>();
        data.clear();
        pduCounter = 0;

        try {
            FileReader fr = new FileReader(System.getProperty("user.dir") + path);
            try (BufferedReader br = new BufferedReader(fr)) {
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
                        } else {
                            TableItem ti = userMap.get(pdu[3]);
                            ti.setLoginTime(pdu[4]);
                            ti.setPduLoginCounter(ti.getPduLoginCounter() + 1);
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
                    entry.getValue().setEstimatedTime(
                        calculateTimeDif(entry.getValue().getLogoutTime(), entry.getValue().getLoginTime()));
                    data.add(entry.getValue());
                }

            }
            return 200;
        } catch (IOException e ) {
            setErrorMessage();
            ExceptionHandler.logException(e);
        }
        return 400;
    }


    /**
     * helper method to calculate the difference between to timeStamps
     * @param d1 Date 1 as String
     * @param d2 Date 2 as String
     * @return Time Difference as String, else NaN
     */
    private String calculateTimeDif(String d1, String d2){
        try {
            SimpleDateFormat df = new SimpleDateFormat(
                "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            Date logout = df.parse(d1);
            Date login = df.parse(d2);

            long diff = logout.getTime() - login.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;

            return diffHours + ":" + diffMinutes + ":" + diffSeconds;
        } catch (Exception e) {
            ExceptionHandler.logException(e);
        }

        return "NaN";
    }

    /**
     * helper method to set the error message alert in a separate GUI thread
     */
    private void setErrorMessage() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Es ist ein Fehler im AdminGUIController aufgetreten");
            alert.setHeaderText("Fehlerbehandlung (Fehlercode = " + 90 + ")");
            alert.setContentText("Bei der Analayse ist ein Fehler aufgetreten. Bitte Vorgang wiederholen");
            alert.showAndWait();
        });
    }


}
