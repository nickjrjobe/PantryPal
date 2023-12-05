package server;

import org.json.JSONObject;
import utils.Recipe;

/** Class for storing and accessing data for multiple Recipes for a specific user */
public class UserRecipeDB implements RecipeData {
  private JSONDB db;
  private String user;

  UserRecipeDB(JSONDB db, String user) {
    this.db = db;
    this.user = user;
    clearFilters();
  }

  /** Adds the given string as a filter for mealtypes */
  public void filterByMealType(String mealtype) {
    db.addFilter("mealtype", mealtype);
  }

  /** Removes all existing filters */
  public void clearFilters() {
    db.clearFilters();
    db.addFilter("username", user);
  }

  public void encapsulate(JSONObject json) {
    json.put("username", this.user);
  }

  public void decapsulate(JSONObject json) {
    json.remove("username");
  }

  public Recipe put(String key, Recipe value) {
    System.out.println("Key " + key);
    JSONObject json = value.toJSON();
    encapsulate(json);
    db.remove(key);
    db.create(json);
    System.err.println("Created: " + json.toString());
    return get(key);
  }

  /** Get a specific recipe, given a title */
  public Recipe get(String key) {
    JSONObject j = db.read(key);
    Recipe r;
    if (j == null) {
      return null;
    } else {
      decapsulate(j);
      return new Recipe(j);
    }
  }

  /** Remove a specific recipe, given a title */
  public Recipe remove(String key) {
    JSONObject j = db.remove(key);
    if (j == null) {
      return null;
    } else {
      decapsulate(j);
      return new Recipe(j);
    }
  }

  public JSONObject toJSON() {
    // TODO decapsulate each field
    return db.toJSON();
  }
}
