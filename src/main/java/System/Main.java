package System;

import Component.LoginComponent;
import javafx.application.Application;
import javafx.stage.Stage;
import System.Sistema;

public class Main extends Application {
    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Sistema sys = Sistema.getInstance();
        sys.setupUI(primaryStage);

        sys.setInterface("login", LoginComponent.loginTitle);
        primaryStage.show();
    }

}
