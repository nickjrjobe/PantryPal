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
public class RecipeList {
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
