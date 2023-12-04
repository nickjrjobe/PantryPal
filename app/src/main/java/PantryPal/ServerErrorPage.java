package PantryPal;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

class ServerErrorUI extends VBox {
  private Text errorMessage;

  ServerErrorUI() {
    errorMessage = new Text("Server is temporarily unavailable, please check later");
    errorMessage.setStyle("-fx-font-size: 14pt;");
    this.getChildren().addAll(errorMessage);
  }

  public boolean tryConnect() {
    HttpModel httpModel = new HttpRequestModel();
    System.out.println("try connect");
    boolean connected = httpModel.tryConnect();
    System.out.println("Connection Status: " + connected);
    return connected;
  }
}

public class ServerErrorPage extends ScrollablePage {
  private ServerErrorUI serverErrorUI;

  ServerErrorPage(ServerErrorUI serverErrorUI) {
    super("Server Error", serverErrorUI);
    this.serverErrorUI = serverErrorUI;
  }
}
