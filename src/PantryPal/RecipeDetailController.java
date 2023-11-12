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
import javafx.scene.layout.*;
import javafx.scene.text.*;
import org.json.JSONObject;

public class RecipeDetailController extends AbstractModel {
  RecipeController() {
    super("recipe");
  }

  public void create(Recipe r) {
    super.performRequest("POST", null, r.toJSON().toString());
  }

  public Recipe read(String title) {
    return new Recipe(new JSONObject(performRequest("GET", Recipe.sanitizeTitle(title), null)));
  }

  public void update(Recipe r) {
    super.performRequest("PUT", null, r.toJSON().toString());
  }

  public void delete(String title) {
    super.performRequest("DELETE", Recipe.sanitizeTitle(title), null);
  }
}
