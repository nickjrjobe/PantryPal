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
public class RecipeEntryUI extends HBox {
  private Label titleField;
  private Label mealTypeField;
  private static final String buttonStyle =
      "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11"
          + " arial;";

  private void format() {
    this.setPrefSize(500, 20); // sets size of task
    this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
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

  RecipeEntryUI(String title) {
    titleField = new Label();
    mealTypeField = new Label();
    titleField.setTextAlignment(TextAlignment.LEFT);
    titleField.setText(title);
    mealTypeField.setTextAlignment(TextAlignment.LEFT);
    mealTypeField.setText("mealType");
    this.getChildren().add(titleField);
    format();
  }
}
