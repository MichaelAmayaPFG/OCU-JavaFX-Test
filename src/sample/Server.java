package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    private int port;
    private boolean state = true;
    private Controller controller;

    public Server(int port, Controller controller){
        this.port = port;
        this.controller = controller;

        System.out.println("Started OCU Program");
        //showData("Hello");
        try {
            server = new ServerSocket(this.port);
        } catch (Exception e){
            state = false;
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (state) {

                //System.out.println("SHOULD NOT OUTPUT");
                String msg = null;
                StringBuilder completeXML = new StringBuilder();
                StringBuilder dataToShow = new StringBuilder();
                System.out.println("Waiting");
                socket = server.accept();
                System.out.println("Got something");
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                while ((msg = reader.readLine()) != null) {
                    completeXML.append(msg);
                }

                Document doc = convertStringToXMLDocument(completeXML.toString());

                NodeList orderHeaderNL = doc.getElementsByTagName("OrderHeader");
                Node orderHeaderN = orderHeaderNL.item(0);
                Element orderHeaderE = (Element) orderHeaderN;
                String orderState = orderHeaderE.getElementsByTagName("OrderState").item(0).getTextContent();

                if(orderState.equals("Open")) {

                    NodeList items = doc.getElementsByTagName("Item");

                    for (int i = 0; i < items.getLength(); i++) {
                        Node nNode = items.item(i);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            /*System.out.println("You ordered " +
                                    eElement.getElementsByTagName("Quantity").item(0).getTextContent() + " " +
                                    eElement.getElementsByTagName("Name").item(0).getTextContent() + " at " +
                                    eElement.getElementsByTagName("Price").item(0).getTextContent()
                            );*/
                            String quantity = eElement.getElementsByTagName("Quantity").item(0).getTextContent();
                            String name = eElement.getElementsByTagName("Name").item(0).getTextContent();
                            String price = eElement.getElementsByTagName("Price").item(0).getTextContent();
                            dataToShow.append(quantity + " " + name + "\t");
                            dataToShow.append((price.equals("0.00") ? "\n" : (price + "\n")));
                        }
                    }
                } else {
                    String subTotal = orderHeaderE.getElementsByTagName("Subtotal").item(0).getTextContent();
                    String total = orderHeaderE.getElementsByTagName("Total").item(0).getTextContent();
                    String tax = orderHeaderE.getElementsByTagName("Tax").item(0).getTextContent();
                    String discount = orderHeaderE.getElementsByTagName("Discount").item(0).getTextContent();
                    /*System.out.println("Your subtotal: " + subTotal);
                    System.out.println("Your tax: " + tax);
                    System.out.println("Your discount: " + discount);
                    System.out.println("Your total: " + total);*/
                    dataToShow.append("Subtotal: " + subTotal + "\n");
                    dataToShow.append("Tax: " + tax + "\n");
                    dataToShow.append("Discount: " + discount + "\n");
                    dataToShow.append("Total: " + total + "\n");
                }

                //System.out.println(dataToShow.toString());
                showData(dataToShow.toString());

                completeXML.delete(0, completeXML.length());
                dataToShow.delete(0,dataToShow.length());

            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void showData(String data){
        try {
            controller.showOnScreen(data);


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setState(boolean state){
        this.state = state;
        System.out.println("Setting to: " + state);
    }

    public boolean getState(){
        return state;
    }

    private static Document convertStringToXMLDocument(String xmlString){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder;

        try{
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
