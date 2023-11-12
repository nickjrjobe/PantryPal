package server;

import com.sun.net.httpserver.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;
import org.json.JSONObject;
import org.json.JSONTokener;
import utils.Recipe;

class SaveableRecipeData {
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

/**
 * Main class for PantryPal's server.
 *
 * <p>In charge of setting up APIs and data structures
 */
public class PantryPalServer {
  private static final int SERVER_PORT = 8100;
  private static final String SERVER_HOSTNAME = "localhost";

  public static void main(String[] args) throws IOException {
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    // create a map to store data
    SaveableRecipeData data = new SaveableRecipeData();

    // create a server
    HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), 0);
    HttpContext recipeListContext = server.createContext("/recipes", new RecipeListAPI(data));
    HttpContext detailedRecipeContext =
        server.createContext("/recipe", new DetailedRecipeAPI(data));
    NewRecipeCreator creator = new NewRecipeCreator(new ChatGPTBot());
    HttpContext newRecipeContext = server.createContext("/newrecipe", new NewRecipeAPI(creator));
    server.setExecutor(threadPoolExecutor);
    server.start();
  }
}
