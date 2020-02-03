package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.naming.ldap.Control;

public class Main extends Application {

    static Controller controller;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("OCU Program");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();


        controller = (Controller) loader.getController();
        Server server = new Server(5901, controller);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }


    public static void main(String[] args) {
        //Controller.initialize();
        launch(args);
        //Controller.setOutPutTxt("Wow");
    }
}
