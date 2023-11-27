package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

class RecipeListAPI extends HttpAPI {
  private RecipeData data;

  RecipeListAPI(RecipeData map) {
    this.data = map;
  }

  String handleGet(String query, String request) throws IOException {
    return data.toJSON().toString();
  }
}
