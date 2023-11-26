package utils;

import java.io.*;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import org.json.JSONObject;

/** IMMUTABLE representation of a recipe */
public class Recipe {
  private final String title;
  private final String description;

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
      return rother.getTitle() == title && rother.getDescription() == description;
    }
    return false;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public Recipe(String title, String description) {
    this.title = title;
    this.description = description;
  }

  public JSONObject toJSON() {
    return new JSONObject().put("title", title).put("description", description);
  }

  public Recipe(JSONObject j) throws IllegalArgumentException {
    System.out.println("object: " + j.toString());
    try {
      this.title = j.getString("title");
      this.description = j.getString("description");
    } catch (Exception e) {
      throw new IllegalArgumentException("JSON Object did not have required fields");
    }
  }

  Recipe() {
    this("default title", "default description");
  }
}
