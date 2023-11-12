/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import org.json.JSONObject;

/**
 * CRUD model for server's detailed recipe data
 */
public class RecipeDetailModel extends AbstractModel {
  RecipeDetailModel() {
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
