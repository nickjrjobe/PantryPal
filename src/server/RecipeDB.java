package server;

import org.json.JSONObject;
import utils.Recipe;

class RecipeDB implements RecipeData {
  JSONDB db;

  RecipeDB(JSONDB db) {
    this.db = db;
  }

  public Recipe put(String key, Recipe value) {
    db.create(value.toJSON());
    return get(key);
  }

  public Recipe get(String key) {
    JSONObject j = db.read(key);
    if (j == null) {
      return null;
    } else {
      return new Recipe(j);
    }
  }

  public Recipe remove(String key) {
    JSONObject j = db.remove(key);
    if (j == null) {
      return null;
    } else {
      return new Recipe(j);
    }
  }

  public JSONObject toJSON() {
    return db.toJSON();
  }
}
class UserRecipeDB implements RecipeData {
  private JSONDB db;
  private String user;

  UserRecipeDB(JSONDB db, String user) {
    this.db = db;
    this.user = user;
    db.addFilter("username", user);
  }
  public void encapsulate(JSONObject json) {
    json.put("username", this.user);
  }
  public void decapsulate(JSONObject json) {
    json.remove("username");
  }

  public Recipe put(String key, Recipe value) {
    JSONObject json = value.toJSON();
    encapsulate(json);
    db.create(value.toJSON());
    return get(key);
  }

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
