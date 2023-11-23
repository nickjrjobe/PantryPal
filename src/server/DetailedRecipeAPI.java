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
  String handleGet(String query, String request) throws IOException {
    String response = "404 Not Found";
    if (query != null) {
      try {
        response = data.read(Recipe.desanitizeTitle(query)).toString(); // get recipe
      } catch(IOException e){
        System.err.println("Query for nonexistiant recipe " + Recipe.desanitizeTitle(query));
      }
    }
    return response;
  }

  /** Create a new recipe */
  String handlePost(String query, String request) throws IOException {
    /* interpret request as json */
    JSONObject json = getJSONRequest(request);
    try {
      data.create(json);
    } catch (Exception e) {
      throw new IOException(e.getMessage());
    }
    String response = "200 Ok";

    return response;
  }

  /** Update recipe */
  String handlePut(String query, String request) throws IOException {
    return handlePost(query, request);
  }

  /** Delete recipe */
  String handleDelete(String query, String request) throws IOException {
    String response = "404 Not Found";
    if (query != null) {
      String value = query.substring(query.indexOf("=") + 1);
      data.delete(Recipe.desanitizeTitle(value)); // Retrieve data from hashmap
      if (recipe != null) {
        response = "200 OK";
      }
    }
    return response;
  }
}
