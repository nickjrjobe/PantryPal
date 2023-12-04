package utils;

import java.io.*;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import org.json.JSONObject;

/** IMMUTABLE representation of a recipe */
public class Recipe {
  private final String title;
  private final String mealType;
  private final String description;
  private final int creationTimestamp;

  /** convert title to URI friendly string */
  public static String sanitizeTitle(String title) {
    return title.replace(" ", "-");
  }

  /** convert URI friendly string back to title format */
  public static String desanitizeTitle(String title) {
    return title.replace("-", " ");
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Recipe) {
      Recipe rother = (Recipe) other;
      return rother.getTitle() == title
          && (rother.getMealType() == mealType && rother.getDescription() == description);
    }
    return false;
  }

  public String getTitle() {
    return title;
  }

  public String getMealType() {
    return mealType;
  }

  public String getDescription() {
    return description;
  }

  public Recipe(String title, String mealType, String description) {
    this.title = title;
    this.mealType = mealType;
    this.description = description;
    this.creationTimestamp = -1;
  }

  public Recipe(String title, String mealType, String description, int creationTimestamp) {
    this.title = title;
    this.mealType = mealType;
    this.description = description;
    this.creationTimestamp = creationTimestamp;
  }

  public JSONObject toJSON() {
    return new JSONObject()
        .put("title", title)
        .put("mealtype", mealType)
        .put("description", description)
        .put("creationTimestamp", creationTimestamp);
  }

  public Recipe(JSONObject j) throws IllegalArgumentException {
    System.out.println("object: " + j.toString());
    try {
      this.title = j.getString("title");
      this.mealType = j.getString("mealtype");
      this.description = j.getString("description");
      this.creationTimestamp = j.getInt("creationTimestamp");
    } catch (Exception e) {
      throw new IllegalArgumentException("JSON Object did not have required fields");
    }
  }

  Recipe() {
    this("default title", "breakfast", "default description");
  }

  public int getCreationTimestamp() {
    return this.creationTimestamp;
  }
}
