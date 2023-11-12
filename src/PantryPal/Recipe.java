/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import java.util.List;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import org.json.JSONObject;

/*
 * Object which specifies a specific way to read recipe list in
 * from persistent storage
 */
interface ReadBehavior {
  public List<Recipe> read();
}

/** internal representation of recipe */
public class Recipe {
  private final String title;
  private final String description;

  public static String sanitizeTitle(String title) {
    return title.replace(" ", "-");
  }

  public static String desanitizeTitle(String title) {
    return title.replace("-", " ");
  }

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
