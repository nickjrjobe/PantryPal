/* Code initially adapted from Lab1 */

package server;

import java.io.*;
import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.text.TextAlignment;
import org.json.JSONObject;

/** internal representation of recipe */
public class Recipe {
  private String title;
  private String description;
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

  public void setDescription(String description) {
    this.description = description;
  }

  Recipe(String title, String description) {
    this.title = title;
    this.description = description;
  }
  public JSONObject toJSON() {
    return new JSONObject().put("title", title).put("description", description);
  }
  Recipe(JSONObject j) throws IllegalArgumentException {
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
