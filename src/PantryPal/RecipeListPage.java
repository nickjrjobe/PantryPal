/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

/** UI element which displays list of recipes */
class RecipeListUI extends VBox {
  private RecipeList recipeList;
  private PageTracker pageTracker;

  RecipeListUI(RecipeList recipelist, PageTracker pt) {
    this.recipeList = recipelist;
    this.pageTracker = pt;
    format();
  }

  private void format() {
    this.setSpacing(5); // sets spacing between tasks
    this.setPrefSize(500, 560);
    this.setStyle("-fx-background-color: #F0F8FF;");
  }

  public void readDetails(Recipe recipe) {
    RecipeDetailUI recipedetails = new RecipeDetailUI(recipe);
    RecipeDetailPage recipepage = new RecipeDetailPage(recipedetails);
    recipepage.footer.addButton(
        "delete",
        e -> {
          recipeList.deleteRecipe(recipe);
          update();
          pageTracker.goHome();
        });
    recipepage.footer.addButton(
        "exit",
        e -> {
          pageTracker.goHome();
        });
    recipepage.footer.addButton(
        "edit",
        e -> {
          /* make editable */
          recipedetails.setDescriptionEditable(true);

          /* delete old buttons */
          recipepage.footer.deleteButton("delete");
          recipepage.footer.deleteButton("edit");

          /* add save button */
          recipepage.footer.addButton(
              "save",
              eprime -> {
                recipe.setDescription(recipedetails.getRecipeDescriptionFieldText());
                update();
                pageTracker.goHome();
              });
        });
    pageTracker.swapToPage(recipepage);
  }

  /** Synchronize recipe List UI element with application's internal recipe list */
  private void update() {
    this.getChildren().clear();
    for (Recipe recipe : this.recipeList.getRecipes()) {
      RecipeEntryUI entry = new RecipeEntryUI(recipe);
      entry.addButton(
          "Details",
          e -> {
            readDetails(recipe);
          });
      this.getChildren().add(entry);
    }
  }

  public void read() {
    recipeList.read();
    update();
  }

  public void saveNewRecipe(NewRecipeUI newRecipeUI) {
    Recipe recipe = newRecipeUI.getRecipe();
    if (recipe == null) {
      return;
    }
    NewDetailedRecipeUI newdetailedrecipeui = new NewDetailedRecipeUI(recipe);
    RecipeDetailPage newdetailedrecipepage = new RecipeDetailPage(newdetailedrecipeui);
    newdetailedrecipepage.footer.addButton(
        "save",
        e -> {
          recipeList.addRecipe(recipe);
          update();
          pageTracker.goHome();
        });
    pageTracker.swapToPage(newdetailedrecipepage);
  }
}

/** UI Page containing recipe list, and accompanying header and footer */
public class RecipeListPage extends ScrollablePage {
  private RecipeListUI recipeList;

  RecipeListPage(PageTracker pt) {
    super("Recipe List", new RecipeListUI(new RecipeList(new FileReadBehavior("recipes.txt")), pt));
    this.recipeList = (RecipeListUI) this.center;
    footer.addButton(
        "Read",
        e -> {
          recipeList.read();
        });
  }

  public void saveNewRecipe(NewRecipeUI newRecipeUI) {
    recipeList.saveNewRecipe(newRecipeUI);
  }
}
