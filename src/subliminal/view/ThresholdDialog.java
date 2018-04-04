package subliminal.view;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import subliminal.MainApp;
import subliminal.model.Referented;

import java.util.Optional;

public class ThresholdDialog {

    private Stage dialogStage;
    private Referented threshold = Referented.TEST;

    @FXML
    Label lblCurrentVolume;


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                handleCancel();
            }
        });
    }

    public void initialize() {
        threshold.startTest();
        update();
    }

    @FXML
    private void handleOk() {
        // кнопка "готово"
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        MainApp.primaryStage.setAlwaysOnTop(true);
                alert.initOwner(MainApp.primaryStage);
        alert.setTitle("Завершить");
        alert.setHeaderText("Завершение настройки порога");
        alert.setContentText("Вы уверены, что хотите начать работу с текущим порогом?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            dialogStage.close();
            threshold.stopTest();
        }
        MainApp.primaryStage.setAlwaysOnTop(false);
    }

    @FXML
    private void handleCancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        MainApp.primaryStage.setAlwaysOnTop(true);
        alert.initOwner(MainApp.primaryStage);
        alert.setTitle("Отмена");
        alert.setHeaderText("Отмена настройки порога");
        alert.setContentText("Вы уверены, что хотите отменить настройку порога слышимости?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            dialogStage.close();
            threshold.stopTest();
        }
        MainApp.primaryStage.setAlwaysOnTop(false);
    }

    @FXML
    private void handleYes() {
        threshold.decreaseVolume();
        update();
        isEnded();
    }

    @FXML
    private void handleNo() {
        threshold.increaseVolume();
        update();
        isEnded();
    }

    private void isEnded() {
        if (threshold.isTestEnded()) {
            dialogStage.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            MainApp.primaryStage.setAlwaysOnTop(true);
            alert.initOwner(MainApp.primaryStage);
            alert.setTitle("Настройка порога");
            alert.setHeaderText("Настройка порога была успешно проведена");
            alert.setContentText("Вы можете начать эксперимент");
            alert.showAndWait();
            MainApp.primaryStage.setAlwaysOnTop(false);
        }
    }

    private void update() {
        lblCurrentVolume.setText(threshold.getStringVolume());
    }
}
