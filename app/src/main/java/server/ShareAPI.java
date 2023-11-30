package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import utils.Recipe;

interface RecipePageMaker {
  public String makePage(Recipe r);
}

class ShareRecipePageMaker implements RecipePageMaker {
  public String makePage(Recipe r) {
    String content = "<html>\n";
    content += "<h1> " + r.getTitle() + "</h1>\n";
    content += "<p> <b> Meal Type: </b> " + r.getMealType() + "</p>\n";
    content += "<p> <b> Description: </b> " + r.getDescription() + "</p>\n";
    content += "<html>";
    return content;
  }
}

class ShareAPIFactory implements HttpUserAPIFactory {
  public HttpAPI makeAPI(String username) {
    return new ShareAPI(
        new UserRecipeDB(new JSONDB("recipes", "title"), username), new ShareRecipePageMaker());
  }
}

/** API which creates Http pages for sharing */
class ShareAPI extends HttpAPI {
  private RecipeData data;
  private RecipePageMaker recipePageMaker;

  ShareAPI(RecipeData map, RecipePageMaker recipePageMaker) {
    this.data = map;
    this.recipePageMaker = recipePageMaker;
  }

  /** Get a specific recipe */
  String handleGet(String query, String request) throws IOException {
    String response = "404 Not Found";
    query = query.substring(query.indexOf("?") + 1);
    if (query != null) {
      Recipe recipe = data.get(Recipe.desanitizeTitle(query)); // get recipe
      if (recipe != null) {
        response = recipePageMaker.makePage(recipe);
      }
    }
    return response;
  }
}
