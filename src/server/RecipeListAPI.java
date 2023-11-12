package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

class RecipeListAPI extends HttpAPI {
  private SaveableRecipeData data;

  RecipeListAPI(SaveableRecipeData map) {
    this.data = map;
  }

  String handleGet(HttpExchange httpExchange) throws IOException {
    return data.toJSON().toString();
  }
}
