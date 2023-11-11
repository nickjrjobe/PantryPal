package server;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.JSONObject;

abstract class HttpAPI implements HttpHandler {

  public void handle(HttpExchange httpExchange) throws IOException {
    String response = "Request Received";
    String method = httpExchange.getRequestMethod();
    try {
      if (method.equals("GET")) {
        response = handleGet(httpExchange);
      } else if (method.equals("POST")) {
        response = handlePost(httpExchange);
      } else if (method.equals("DELETE")) {
        response = handleDelete(httpExchange);
      } else if (method.equals("PUT")) {
        response = handlePut(httpExchange);
      } else {
        throw new Exception("Not Valid Request Method");
      }
    } catch (Exception e) {
      System.out.println("An erroneous request");
      response = e.toString();
      e.printStackTrace();
    }
    // Sending back response to the client
    httpExchange.sendResponseHeaders(200, response.length());
    OutputStream outStream = httpExchange.getResponseBody();
    outStream.write(response.getBytes());
    outStream.close();
  }
  String handleGet(HttpExchange httpExchange) throws IOException {
    throw new IOException("Request type not supported");
  };
  String handlePost(HttpExchange httpExchange) throws IOException {
    throw new IOException("Request type not supported");
  }
  String handlePut(HttpExchange httpExchange) throws IOException {
    throw new IOException("Request type not supported");
  }
  String handleDelete(HttpExchange httpExchange) throws IOException{
    throw new IOException("Request type not supported");
  }
}
class DetailedRecipeAPI extends HttpAPI {
  private Map<String, Recipe> data;
  DetailedRecipeAPI(Map<String, Recipe> map) {
    this.data = map;
  }
  /**
   * Get a specific recipe
   */
  String handleGet(HttpExchange httpExchange) throws IOException {
    URI uri = httpExchange.getRequestURI();
    String query = uri.getRawQuery();
    String response = "404 Not Found";
    if (query != null) {
      Recipe recipe = data.get(query); // get recipe
      if (recipe != null) {
        response = recipe.toJSON().toString();
      }
    }
    return response;
  }

  /**
   * Create a new recipe
   */
  String handlePost(HttpExchange httpExchange) throws IOException {
    InputStream inStream = httpExchange.getRequestBody();
    Scanner scanner = new Scanner(inStream);
    String postData = scanner.nextLine();
    JSONObject json;
    Recipe recipe;
    try {
      json = new JSONObject(httpExchange);
    } catch (Exception e) {
      throw new IOException("Response was not JSON");
    }
    try {
      recipe = new Recipe(json);
    } catch (Exception e) {
      throw new IOException(e.getMessage());
    }
    // Store data in hashmap
    data.put(recipe.getTitle(), recipe);

    String response = "200 Ok";
    scanner.close();

    return response;
  }

  /**
   * Update recipe
   */
  String handlePut(HttpExchange httpExchange) throws IOException {
    return handlePost(httpExchange);
  }
  /**
   * Delete recipe
   */
    String handleDelete(HttpExchange httpExchange) throws IOException {
      URI uri = httpExchange.getRequestURI();
      String query = uri.getRawQuery();
      String response = "404 Not Found";
      if (query != null) {
        String value = query.substring(query.indexOf("=") + 1);
        Recipe recipe = data.remove(value); // Retrieve data from hashmap
        if (recipe != null) {
        response = "200 OK";
        }
      }
      return response;
  }
}
class RecipeListAPI extends HttpAPI {
  private Map<String, Recipe> data;
  RecipeListAPI(Map<String, Recipe> map) {
    this.data = map;
  }
  private JSONObject makeRecipeList() {
    JSONObject recipeList = new JSONObject();
    for(Recipe recipe : data.values()){
          recipeList.put(recipe.getTitle(), recipe.toJSON());
    }
    return recipeList;
  }
  String handleGet(HttpExchange httpExchange) throws IOException {
    return makeRecipeList().toString();
  }
}
