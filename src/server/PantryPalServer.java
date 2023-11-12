package server;
import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class PantryPalServer {
  private static final int SERVER_PORT = 8100;
  private static final String SERVER_HOSTNAME = "localhost";

  public static void main(String[] args) throws IOException {
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    // create a map to store data
    Map<String, Recipe> data = new HashMap<>();
    data.put("brocolli", new Recipe("brocolli", "brocolli ingredients"));

    // create a server
    HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), 0);
    HttpContext recipeListContext = server.createContext("/recipes", new RecipeListAPI(data));
    HttpContext DetailedRecipeContext =
        server.createContext("/recipe", new DetailedRecipeAPI(data));
    server.setExecutor(threadPoolExecutor);
    server.start();
  }
}
