package System;

import State.StringCommand;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.HashMap;

public class InterfacesController {
    private HashMap<String, Pane> interfaces = new HashMap<>();
    private Stage main;

    public InterfacesController(Stage stage) {
        this.main = stage;
    }

    public void addInterface(String name, Pane pane){
        interfaces.put(name, pane);
    }

    public Pane getInterface(String name) {
        return this.interfaces.get(name);
    }

    public void activate(String name, String title){
        if(main.getScene() != null)
            main.getScene().setRoot(interfaces.get(name));
        else main.setScene(new Scene(interfaces.get(name)));

        main.setResizable(false);
        main.setTitle(title);
        main.sizeToScene();
        main.toFront();
        main.setX(100);
        main.setY(100);
        main.show();

        main.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                Sistema.getInstance().getStore().update(new StringCommand("LOGOUT"));
            }
        });
    }

    public void deactivate() {
        main.close();
    }

    public void toFront() {
        main.toFront();
        main.show();
    }
}
