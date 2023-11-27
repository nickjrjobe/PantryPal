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
