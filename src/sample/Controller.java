package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.Socket;

public class Controller {
    public TextArea outPutTxt;
    public Button newBtn;

    public Controller(){
        //serverThread.start();
        //System.out.println("Hello world");
    }

    public void initialize(){
        //outPutTxt.setText("Hello");
    }

    public void pressBtn(ActionEvent actionEvent) {
        outPutTxt.setText("Goodbye");
        //server.setState(false);
    }

    public void showOnScreen(String data){
        outPutTxt.setText(data);
        //System.out.println(data);
    }
}
