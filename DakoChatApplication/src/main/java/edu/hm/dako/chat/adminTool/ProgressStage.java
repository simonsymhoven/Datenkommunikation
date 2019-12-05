package edu.hm.dako.chat.adminTool;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ProgressIndicator;

public class ProgressStage {
    public static Stage stageForProgressIndicator;
    public static ProgressIndicator progressIndicator;

    public ProgressStage(){
        this.stageForProgressIndicator = new Stage();
        this.progressIndicator = new ProgressIndicator();
    }

    public static void startProgress() {
        FlowPane root = new FlowPane();
        stageForProgressIndicator.setResizable(false);
        progressIndicator.setProgress(-1);

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

    public void stopProgress(){
        stageForProgressIndicator.close();
    }
}
