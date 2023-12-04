/* Code initially adapted from Lab1 */

package PantryPal;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import utils.Account;
import utils.Recipe;
import utils.RecipeListFactory;

/** Communication model for making API requests to get Recipe List. */
public class RecipeListModel {
  HttpModel httpModel;
  Account account;

  RecipeListModel(HttpModel httpModel, Account account) {
    this.httpModel = httpModel;
    httpModel.setPath("recipes/" + account.getUsername() + "/");
  }

  /**
   * Get a list of recipes from the server
   *
   * @return
   */
  public List<Recipe> getRecipeList(String sortSelection) {
    String response = httpModel.performRequest("GET", sortSelection, null);
    try {
      return processResponse(response);
    } catch (Exception e) {
      System.err.println("HTTP request failed with error " + e.getMessage());
      return new ArrayList<Recipe>();
    }
  }

  /**
   * Get a list of recipes from the server filtered by meal type
   *
   * @param mealtype
   * @return
   */
  public List<Recipe> getMealTypeRecipeList(String mealtype, String sortSelection) {
    if (mealtype == "No Filter") {
      return getRecipeList(sortSelection);
    }
    httpModel.performRequest("POST", mealtype, null);
    List<Recipe> recipeList = getRecipeList(sortSelection);
    // Display filtered recipes list
    System.out.println("Filtered Recipes: ");
    for (Recipe recipe : recipeList) {
      System.out.println("    " + recipe.getTitle());
    }
    // Delete filter
    httpModel.performRequest("DELETE", null, null);
    return recipeList;
  }

  /**
   * Process the response from the server
   *
   * @param response the response from the server
   * @return
   * @throws IllegalArgumentException
   */
  public List<Recipe> processResponse(String response) throws IllegalArgumentException {
    try {
      return (new RecipeListFactory(new JSONObject(response).getJSONArray("recipes"))).buildList();
    } catch (Exception ex) {
      throw new IllegalArgumentException("response: \"" + response + "\" was invalid");
    }
  }
}
