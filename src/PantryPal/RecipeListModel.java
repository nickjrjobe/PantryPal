/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import org.json.JSONObject;

/** Communication model for making API requests to get Recipe List. */
public class RecipeListModel extends AbstractModel {
  RecipeListModel() {
    super("/recipes");
  }

  public List<String> getRecipeList() {
    String response = super.performRequest("GET", null, null);
    try {
      return processResponse(response);
    } catch (Exception e) {
      System.err.println("HTTP request failed with error " + e.getMessage());
      return new ArrayList<String>();
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
