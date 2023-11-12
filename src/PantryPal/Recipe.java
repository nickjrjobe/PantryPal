/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.text.TextAlignment;

/** UI representation of Recipe on RecipeList page */
class RecipeEntryUI extends HBox {
  private Recipe recipe;
  private Label titleField;
  private static final String buttonStyle =
      "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11"
          + " arial;";

  public Recipe getRecipe() {
    return recipe;
  }

  private void format() {
    this.setPrefSize(500, 20); // sets size of task
    this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
  }

  /** Updates data in Recipe UI element based on data in owned recipe */
  private void update() {
    titleField.setText(recipe.getTitle());
  }

  /**
   * add a button to footer
   *
   * @param buttontext Text which button should display
   * @param callback event handler to call when button is pressed
   */
  public void addButton(String buttontext, EventHandler<ActionEvent> callback) {
    Button button = new Button(buttontext);
    button.setStyle(buttonStyle);
    this.getChildren().add(button);
    button.setOnAction(callback);
  }

  RecipeEntryUI(Recipe recipe) {
    this.recipe = recipe;
    titleField = new Label();
    titleField.setPrefSize(380, 20);
    titleField.setStyle("-fx-background-color: #dae5ea; -fx-border-width: 0;");
    titleField.setTextAlignment(TextAlignment.LEFT);
    this.getChildren().add(titleField);
    update();
    format();
  }
}

/** internal representation of recipe */
class Recipe {
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
