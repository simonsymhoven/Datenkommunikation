package edu.hm.dako.chat.adminTool;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ProgressIndicator;

/**
 * Class to handle the load animation while tool analyses the log-file
 */
public class ProgressStage {
    /**
     * default Stage
     */
    static final Stage stageForProgressIndicator = new Stage();

    /**
     * default progressIndicator
     */
    static final ProgressIndicator progressIndicator = new ProgressIndicator(-1);

    /**
     * starts the load animation in a new stage with a infinity progressbar
     */
    public static void startProgress() {
        FlowPane root = new FlowPane();
        stageForProgressIndicator.setResizable(false);

        HBox hb = new HBox();
        hb.setSpacing(5);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(progressIndicator);

        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(hb);

        Scene scene = new Scene(root, 250, 150);
        scene.setRoot(vb);
        stageForProgressIndicator.setTitle("Please wait..");

        stageForProgressIndicator.setScene(scene);
        stageForProgressIndicator.setAlwaysOnTop(true);
        stageForProgressIndicator.show();
    }

    /**
     * closes the stage and hide it
     */
    public void stopProgress(){
        stageForProgressIndicator.close();
    }
}
