package subliminal.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import subliminal.MainApp;
import subliminal.model.Referented;
import subliminal.model.Test;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

public class MainAppController {

    private static ArrayList<Test> tests;
    public static ArrayList<Test> getTests() {
        return tests;
    }
    private static Clip noise;

    Referented enigmated;


    @FXML
    GridPane testPane;

    @FXML
    public void openThresholdDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainAppController.class.getResource("ThresholdDialog.fxml"));
            AnchorPane thresholdDialog = (AnchorPane) loader.load();
            Scene scene = new Scene(thresholdDialog);
            Stage dialogStage = new Stage();
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Сублиминал: настройка значения порога");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setAlwaysOnTop(true);
            ThresholdDialog controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void exit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Выход");
        alert.setHeaderText("Выход");
        alert.setContentText("Вы уверены, что хотите выйти из программы?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    @FXML
    public void startExperiment() {
        if (!Referented.TEST.isTested()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Необходимо определить порог");
            alert.setHeaderText("Для проведения эксперимента необходимо провести процедуру определения порога слышимости");
            alert.setContentText("Проведите процедуру определения порога");
            MainApp.primaryStage.setAlwaysOnTop(true);
            alert.initOwner(MainApp.primaryStage);
            alert.showAndWait();
            MainApp.primaryStage.setAlwaysOnTop(false);
            return;
        }
        tests = new ArrayList<>();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        MainApp.primaryStage.setAlwaysOnTop(true);
        alert.initOwner(MainApp.primaryStage);
        alert.setTitle("Начать тестирование");
        alert.setHeaderText("Пожалуйста, прочитайте инструкцию:");
        alert.setContentText("Выбирайте одно из нескольких животных, делая перед каждым выбором паузу длительностью несколько" +
                " (4-6) секунд. Для получения достоверных результатов рекомендуется большое количество произвольных выборов.");
        alert.showAndWait();
        MainApp.primaryStage.setAlwaysOnTop(false);
        noise.loop(Clip.LOOP_CONTINUOUSLY);



        enigmated = Referented.enigmate();

        testPane.setVisible(true);
        testPane.setDisable(false);
    }

    public static void stopNoise() {
        if (noise!=null) noise.stop();
    }

    @FXML public void onMouseEntered() {
        MainApp.scene.setCursor(Cursor.HAND);
    }

    @FXML public void onMouseExited() {
        MainApp.scene.setCursor(Cursor.DEFAULT);
    }

    @FXML public void slonClicked() {
        tests.add(new Test(enigmated, Referented.SLON));
        enigmated = Referented.enigmate();
    }
    @FXML public void pingvinClicked() {
        tests.add(new Test(enigmated, Referented.PINGVIN));
        enigmated = Referented.enigmate();
    }
    @FXML public void levClicked() {
        tests.add(new Test(enigmated, Referented.LEV));
        enigmated = Referented.enigmate();
    }
    @FXML public void jirafClicked() {
        tests.add(new Test(enigmated, Referented.JIRAF));
        enigmated = Referented.enigmate();    }
    @FXML public void zebraClicked() {
        tests.add(new Test(enigmated, Referented.ZEBRA));
        enigmated = Referented.enigmate();
    }

    @FXML
    public void ShowStatistics() {
        try {
            for (Referented r : Referented.values()) {
                r.stopTest();
            }
            stopNoise();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainAppController.class.getResource("Statistics.fxml"));
            AnchorPane statisticsDialog = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Сублиминал: статистика");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setAlwaysOnTop(true);
            dialogStage.setResizable(false);
            Scene scene = new Scene(statisticsDialog);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
            dialogStage.setAlwaysOnTop(false);
            if (tests!=null) {
                enigmated = Referented.enigmate();
                noise.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        File file = new File("sounds\\noise.wav");
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(file);
            noise = AudioSystem.getClip();
            noise.open(ais);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

}
