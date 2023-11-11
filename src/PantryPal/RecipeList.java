/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

/*
 * Object which specifies a specific way to read recipe list in
 * from persistent storage
 */
interface ReadBehavior {
  public List<Recipe> read();
}

/** Internal representation of recipes */
class RecipeList {
  private ArrayList<Recipe> recipes;
  private ReadBehavior readbehavior;

  RecipeList(ReadBehavior readbehavior) {
    this.readbehavior = readbehavior;
    this.recipes = new ArrayList<Recipe>();
  }

  public void read() {
    if (readbehavior == null) {
      System.err.println("Read behavior not provided");
      return;
    }
    List<Recipe> recipes = readbehavior.read();
    for (Recipe recipe : recipes) {
      addRecipe(recipe);
    }
  }

  public int size() {
    return recipes.size();
  }

  public void addRecipe(Recipe recipe) {
    recipes.add(recipe);
  }

  public List<Recipe> getRecipes() {
    return recipes;
  }

  public Recipe getRecipe(int index) {
    return recipes.get(index);
  }

  // delete a recipe from the list
  public void deleteRecipe(Recipe recipe) {
    recipes.remove(recipe);
  }
}

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
class RecipeListPage extends ScrollablePage {
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
