package utils;

import java.io.*;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import org.json.JSONObject;

/** IMMUTABLE representation of a recipe */
public class Account {
  private final String username;
  private final String password;

  public static boolean isValidUsername(String username) {
    boolean invalid = username.contains(" ");
    return !invalid;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Account) {
      Account rother = (Account) other;
      return rother.getUsername().equals(username) && rother.getPassword().equals(password);
    }
    return false;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public Account(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public JSONObject toJSON() {
    return new JSONObject().put("username", username).put("password", password);
  }

  public Account(JSONObject j) throws IllegalArgumentException {
    System.out.println("object: " + j.toString());
    try {
      this.username = j.getString("username");
      this.password = j.getString("password");
    } catch (Exception e) {
      throw new IllegalArgumentException("JSON Object did not have required fields");
    }
  }
}
