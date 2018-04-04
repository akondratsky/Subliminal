package subliminal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.*;

public class MainApp extends Application {

    public static BorderPane rootLayout;
    public static Scene scene;
    public static Stage primaryStage;

    // а оно вообще надо???
    //Referented sounds;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MainApp.primaryStage = primaryStage;
        MainApp.primaryStage.setTitle("Сублиминал");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/main.fxml"));
            rootLayout = (BorderPane) loader.load();
            scene = new Scene(rootLayout);
            MainApp.primaryStage.setScene(scene);
            MainApp.primaryStage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
