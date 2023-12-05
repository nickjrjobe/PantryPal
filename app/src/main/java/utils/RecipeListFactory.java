package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import org.json.JSONArray;
import org.json.JSONObject;

/** representation of a recipeList */
public class RecipeListFactory {
  List<Recipe> recipes;

  public RecipeListFactory sort(String sortSelection) {
    // Apply sorting
    if (sortSelection.equals("A-Z")) {
      recipes.sort((a, b) -> a.getTitle().compareTo(b.getTitle()));
    } else if (sortSelection.equals("Z-A")) {
      recipes.sort((a, b) -> b.getTitle().compareTo(a.getTitle()));
    } else if (sortSelection.equals("Oldest")) {
      recipes.sort((a, b) -> Integer.compare(a.getCreationTimestamp(), b.getCreationTimestamp()));
    } else if (sortSelection.equals("Newest")) {
      recipes.sort((a, b) -> Integer.compare(b.getCreationTimestamp(), a.getCreationTimestamp()));
    } else {
      System.err.println("WARN: invalid sorting method \"" + sortSelection + "\"");
    }
    System.err.println("sorted by " + sortSelection);
    return this;
  }

  /**
   * Process the response from the server
   *
   * @param response the response from the server
   * @return
   * @throws IllegalArgumentException
   */
  public RecipeListFactory(JSONObject json) throws IllegalArgumentException {
    recipes = new ArrayList<Recipe>();
    try {
      Iterator<String> iRecipes = json.keys();
      while (iRecipes.hasNext()) {
        String title = iRecipes.next();
        JSONObject recipe = json.getJSONObject(title);
        Recipe r = new Recipe(recipe);
        recipes.add(r);
      }
    } catch (Exception ex) {
      System.out.println("ERROR: " + ex.getMessage());
      throw new IllegalArgumentException("json was invalid");
    }
  }

  public RecipeListFactory(JSONArray json) throws IllegalArgumentException {
    recipes = new ArrayList<Recipe>();
    try {
      for (int i = 0; i < json.length(); i++) {
        recipes.add(new Recipe(json.getJSONObject(i)));
      }
    } catch (Exception ex) {
      throw new IllegalArgumentException("json was invalid");
    }
  }

  public List<Recipe> buildList() {
    return recipes;
  }

  public JSONObject buildJSON() {
    JSONObject jsonObject = new JSONObject();
    JSONArray json = new JSONArray();
    for (Recipe recipe : recipes) {
      json.put(recipe.toJSON());
    }
    return jsonObject.put("recipes", json);
  }
}
