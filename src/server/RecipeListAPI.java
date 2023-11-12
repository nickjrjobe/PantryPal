package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

class RecipeListAPI extends HttpAPI {
  private SaveableRecipeData data;

  RecipeListAPI(SaveableRecipeData map) {
    this.data = map;
  }

  String handleGet(String query, String request) throws IOException {
    return data.toJSON().toString();
  }
}
