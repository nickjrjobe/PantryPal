/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.text.TextAlignment;
import org.json.JSONObject;

/*
 * Object which specifies a specific way to read recipe list in
 * from persistent storage
 */
interface ReadBehavior {
  public List<Recipe> read();
}
abstract class AbstractModel {
  private static final String port = "8100";
  private static final String ip = "localhost";
  private String urlString;
  AbstractModel(String path) {
    this.urlString = "http://" + ip + ":" + port + "/" + path;
  }
  protected String performRequest(String method, String query, String request) {
    // Implement your HTTP request logic here and return the response
    if (request != null) {
      System.out.println("Request :" + request);
    }
    try {
      if (query != null) {
        urlString += "?" + query;
      }
      URL url = new URI(urlString).toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod(method);
      conn.setDoOutput(true);

      if (method.equals("POST") || method.equals("PUT")) {
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        out.write(request);
        out.flush();
        out.close();
      }

      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String response = in.readLine();
      in.close();
      System.out.println("Response :" + response);
      return response;
    } catch (Exception ex) {
      ex.printStackTrace();
      return "Error: " + ex.getMessage();
    }
  }
}
class RecipeController extends AbstractModel {
  RecipeController() {
    super("recipe");
  }
  public void create(Recipe r) {
    super.performRequest("POST", null, r.toJSON().toString());
  }
  public Recipe read(String title) {
    return new Recipe(new JSONObject(performRequest("GET", title, null)));
  }
  public void update(Recipe r) {
    super.performRequest("PUT", null, r.toJSON().toString());
  }
  public void delete(String title) {
    super.performRequest("DELETE", title, null);
  }
}

/** internal representation of recipe */
public class Recipe {
  private final String title;
  private final String description;

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }
  Recipe(String title, String description) {
    this.title = title;
    this.description = description;
  }
  public JSONObject toJSON() {
    return new JSONObject().put("title", title).put("description", description);
  }
  Recipe(JSONObject j) throws IllegalArgumentException {
    System.out.println("object: " + j.toString());
    try {
      this.title = j.getString("title");
      this.description = j.getString("description");
    } catch (Exception e) {
      throw new IllegalArgumentException("JSON Object did not have required fields");
    }
  }

  Recipe() {
    this("default title", "default description");
  }
}
