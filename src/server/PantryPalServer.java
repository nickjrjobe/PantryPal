package server;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

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
    RecipeData data = new RecipeDB(new JSONDB("recipes", "title"));

    // create a server
    HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), 0);
    // HttpContext recipeListContext = server.createContext("/recipes", new RecipeListAPI(data));
    HttpContext recipeListUsersContext =
        server.createContext("/recipes", new UserHandler(new RecipeListAPIFactory()));
    HttpContext detailedRecipeContext =
        server.createContext("/recipe", new UserHandler(new DetailedRecipeAPIFactory()));
    /* setup APIs */
    // HttpContext detailedRecipeContext =
    //     server.createContext("/recipe", new DetailedRecipeAPI(data));
    RecipeCreator creator = new ChatGPTBot();
    // HttpContext newRecipeContext = server.createContext("/newrecipe", new NewRecipeAPI(creator));
    HttpContext newRecipeContext =
        server.createContext("/newrecipe", new UserHandler(new NewRecipeCreatorFactory(creator)));
    server.setExecutor(threadPoolExecutor);
    /* start server */
    server.start();
  }
}
