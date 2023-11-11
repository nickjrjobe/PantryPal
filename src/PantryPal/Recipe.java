/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.text.TextAlignment;

/** internal representation of recipe */
public class Recipe {
  private String title;
  private String description;

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

  Recipe() {
    this("default title", "default description");
  }
}
