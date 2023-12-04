package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import utils.RecipeListFactory;

class RecipeListAPIFactory implements HttpUserAPIFactory {
  public HttpAPI makeAPI(String username) {
    return new RecipeListAPI(new UserRecipeDB(new MongoJSONDB("recipes", "title"), username));
  }
}

/** API for getting a list of recipes from the database. Visible on the /recipes/ endpoint. */
class RecipeListAPI extends HttpAPI {
  private RecipeData data;

  /**
   * Create a new RecipeListAPI.
   *
   * @param map the RecipeData object to interface with
   */
  RecipeListAPI(RecipeData map) {
    this.data = map;
  }

  /**
   * Get all available recipes
   *
   * @param query the query string (unused)
   * @param request the request body (unused)
   * @return a JSON formatted string of a list of recipes
   */
  String handleGet(String query, String request) throws IOException {
    query = query.substring(query.indexOf("?") + 1); // Removes the ? from the query
    return (new RecipeListFactory(data.toJSON())).sort(query).buildJSON().toString();
  }

  /**
   * Filter the list of recipes by meal type.
   *
   * @param query the query string (unused)
   * @param request the request body (unused)
   * @return a 200 OK response if successful, 400 Bad Request otherwise
   */
  String handlePost(String query, String request) throws IOException {
    query = query.substring(query.indexOf("?") + 1); // Removes the ? from the query
    String result = "200 OK";
    if (query.equals("breakfast")) {
      data.filterByMealType("breakfast");
    } else if (query.equals("lunch")) {
      data.filterByMealType("lunch");
    } else if (query.equals("dinner")) {
      data.filterByMealType("dinner");
    } else {
      result = "400 Bad Request";
    }
    return result;
  }

  /**
   * Clear the filters being used to access recipes
   *
   * @param query the query string (unused)
   * @param request the request body (unused)
   * @return a 200 OK response
   */
  String handleDelete(String query, String request) throws IOException {
    data.clearFilters();
    return "200 OK";
  }
}
