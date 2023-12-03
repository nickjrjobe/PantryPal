/* Code initially adapted from Lab1 */

package PantryPal;

import org.json.JSONObject;
import utils.Account;
import utils.Recipe;

/** CRUD model for server's detailed recipe data */
public class RecipeDetailModel {
  HttpModel httpModel;

  RecipeDetailModel(HttpModel httpModel, Account account) {
    this.httpModel = httpModel;
    httpModel.setPath("recipe/" + account.getUsername() + "/");
  }

  public void create(Recipe r) {
    httpModel.performRequest("POST", null, r.toJSON().toString());
  }

  public Recipe read(String title) {
    return new Recipe(
        new JSONObject(httpModel.performRequest("GET", Recipe.sanitizeTitle(title), null)));
  }

  public void update(Recipe r) {
    httpModel.performRequest("PUT", null, r.toJSON().toString());
  }

  public void delete(String title) {
    httpModel.performRequest("DELETE", Recipe.sanitizeTitle(title), null);
  }
}
