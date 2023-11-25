package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
class RecipeListAPIFactory implements HttpUserAPIFactory {
  public HttpAPI makeAPI(String username) {
    return new RecipeListAPI(new RecipeDB(new JSONDB("recipes", "title")));
  }
}

class RecipeListAPI extends HttpAPI {
  private RecipeData data;

  RecipeListAPI(RecipeData map) {
    this.data = map;
  }

  String handleGet(String query, String request) throws IOException {
    return data.toJSON().toString();
  }
}
