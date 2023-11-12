package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
class DetailedRecipeAPI extends HttpAPI {
  private SaveableRecipeData data;

  DetailedRecipeAPI(SaveableRecipeData map) {
    this.data = map;
  }

  /** Get a specific recipe */
  String handleGet(HttpExchange httpExchange) throws IOException {
    URI uri = httpExchange.getRequestURI();
    String query = uri.getRawQuery();
    String response = "404 Not Found";
    if (query != null) {
      Recipe recipe = data.get(Recipe.desanitizeTitle(query)); // get recipe
      if (recipe != null) {
        response = recipe.toJSON().toString();
      }
    }
    return response;
  }

  /** Create a new recipe */
  String handlePost(HttpExchange httpExchange) throws IOException {
    InputStream inStream = httpExchange.getRequestBody();
    Scanner scanner = new Scanner(inStream);
    String postData = scanner.nextLine();
    JSONObject json;
    Recipe recipe;
    System.out.println("request: " + postData);
    try {
      json = new JSONObject(postData);
    } catch (Exception e) {
      throw new IOException("Response was not JSON");
    }
    try {
      System.out.println("object :" + json.toString());
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

  /** Update recipe */
  String handlePut(HttpExchange httpExchange) throws IOException {
    return handlePost(httpExchange);
  }

  /** Delete recipe */
  String handleDelete(HttpExchange httpExchange) throws IOException {
    URI uri = httpExchange.getRequestURI();
    String query = uri.getRawQuery();
    String response = "404 Not Found";
    if (query != null) {
      String value = query.substring(query.indexOf("=") + 1);
      Recipe recipe = data.remove(Recipe.desanitizeTitle(value)); // Retrieve data from hashmap
      if (recipe != null) {
        response = "200 OK";
      }
    }
    return response;
  }
}
