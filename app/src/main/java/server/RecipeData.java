package server;

import com.sun.net.httpserver.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;
import org.json.JSONObject;
import org.json.JSONTokener;
import utils.Recipe;

/** Interface of object which stores data for Recipes */
public interface RecipeData {
  public JSONObject toJSON();

  public Recipe put(String key, Recipe value);

  public Recipe get(String key);

  public Recipe remove(String key);
}

class SaveableRecipeData implements RecipeData {
  public static final String path = "server_data.json";
  private Map<String, Recipe> data;

  public JSONObject toJSON() {
    JSONObject recipeList = new JSONObject();
    for (Recipe recipe : data.values()) {
      recipeList.put(recipe.getTitle(), recipe.toJSON());
    }
    return recipeList;
  }

  /** persistently save recipe data */
  public void write() {
    try {
      FileWriter file = new FileWriter(path);
      file.write(toJSON().toString());
      file.close();
    } catch (Exception e) {
      System.err.println("could not write server_data to file");
    }
  }

  /** read recipe data from persistent storage */
  public void read() {
    try {
      validateJsonFile(path);
      FileReader fileReader = new FileReader(path);
      JSONTokener tokener = new JSONTokener(fileReader);
      JSONObject jsonObject = new JSONObject(tokener);
      fileReader.close();
      Iterator<String> titles = jsonObject.keys();
      while (titles.hasNext()) {
        String title = titles.next();
        JSONObject recipeJson = jsonObject.getJSONObject(title);
        System.err.println(recipeJson.toString());
        data.put(title, new Recipe(recipeJson));
      }
    } catch (Exception e) {
      System.err.println("could not read server_data from file with error " + e.getMessage());
    }
  }

  /** Helper method to ensure that the JSON file at `path` exists, and if not, create it. */
  public void validateJsonFile(String path) {
    try {
      FileWriter file = new FileWriter(path, true);
      file.write("{}");
      file.close();
    } catch (Exception e) {
      System.err.println("could not create file: " + path);
    }
  }

  SaveableRecipeData() {
    this.data = new HashMap<String, Recipe>();
    read();
  }

  public Recipe put(String key, Recipe value) {
    Recipe r = data.put(key, value);
    write();
    return r;
  }

  public Recipe get(String key) {
    return data.get(key);
  }

  public Recipe remove(String key) {
    Recipe r = data.remove(key);
    write();
    return r;
  }
}
