package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.JSONObject;
import utils.Recipe;

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
    /* interpret request as json */
    JSONObject json = getJSONRequest(httpExchange);
    Recipe recipe;
    try {
      recipe = new Recipe(json);
    } catch (Exception e) {
      throw new IOException(e.getMessage());
    }
    // Store data in hashmap
    data.put(recipe.getTitle(), recipe);

    String response = "200 Ok";

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
