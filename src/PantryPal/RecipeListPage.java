/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import org.json.JSONObject;

class RecipeListModel {
  public List<String> performRequest() {
    try {
      String urlString = "http://localhost:8100/recipes";
      URL url = new URI(urlString).toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setDoOutput(true);
      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String response = in.readLine();
      System.out.println("response " + response);
      in.close();
      return processResponse(response);
    } catch (Exception e) {
      System.err.println("HTTP request failed with error " + e.getMessage());
      return null;
    }
  }

  /** Synchronize recipe List UI element with application's internal recipe list */
  public List<String> processResponse(String response) throws IllegalArgumentException {
    try {
      JSONObject json = new JSONObject(response);
      List<String> title = new ArrayList<String>();
      Iterator<String> ititles = json.keys();
      while (ititles.hasNext()) {
        title.add(ititles.next());
      }
      return title;
    } catch (Exception ex) {
      throw new IllegalArgumentException("response: \"" + response + "\" was invalid");
    }
  }
}

/** UI element which displays list of recipes */
class RecipeListUI extends VBox {
  RecipeListUI(List<RecipeEntryUI> entries) {
    this.getChildren().addAll(entries);
    format();
  }

  private void format() {
    this.setSpacing(5); // sets spacing between tasks
    this.setPrefSize(500, 560);
    this.setStyle("-fx-background-color: #F0F8FF;");
  }
}

/** UI Page containing recipe list, and accompanying header and footer */
public class RecipeListPage extends ScrollablePage {
  RecipeListPage(List<RecipeEntryUI> entries) {
    super("Recipe List", new RecipeListUI(entries));
  }
}
