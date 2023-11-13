/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import java.util.List;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

/** UI element which displays IMMUTABLE list of recipes */
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
